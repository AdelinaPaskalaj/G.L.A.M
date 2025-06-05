// LawyerDashboardController.java
package lawoffice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lawoffice.model.Appointment;
import lawoffice.model.Case;
import lawoffice.model.User;
import lawoffice.service.LawyerDashboardService;
import lawoffice.session.Session;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LawyerDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label totalCasesLabel;
    @FXML private Label activeCasesLabel;
    @FXML private Label upcomingAppointmentsLabel;

    @FXML private VBox profilePane;
    @FXML private VBox dashboardPane;
    @FXML private StackPane contentPane;

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML private DatePicker calendar;
    @FXML private ListView<String> appointmentListView;

    @FXML private TableView<Case> recentCaseTable;
    @FXML private TableColumn<Case, Number> caseIdCol;
    @FXML private TableColumn<Case, String> caseTypeCol;
    @FXML private TableColumn<Case, String> caseStatusCol;

    private final LawyerDashboardService service = new LawyerDashboardService();
    private int lawyerId;

    @FXML
    public void initialize() {
        User lawyer = Session.getCurrentUser();
        if (lawyer != null) {
            welcomeLabel.setText("Welcome, " + lawyer.getName());
            lawyerId = service.getLawyerIdByUserId(lawyer.getUserId());
            loadDashboard(lawyerId);
            highlightAppointmentDays(lawyerId);
            setupCaseTable();
            loadRecentCases(lawyerId);
        }
    }

    private void loadDashboard(int lawyerId) {
        totalCasesLabel.setText(String.valueOf(service.countAssignedCases(lawyerId)));
        activeCasesLabel.setText(String.valueOf(service.countActiveCases(lawyerId)));
        upcomingAppointmentsLabel.setText(String.valueOf(service.countUpcomingAppointments(lawyerId)));
    }

    private void highlightAppointmentDays(int lawyerId) {
        List<Appointment> appointments = service.getUpcomingAppointments(lawyerId);
        Set<LocalDate> appointmentDates = new HashSet<>();
        for (Appointment appt : appointments) {
            appointmentDates.add(appt.getDate());
        }
        calendar.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (!empty && appointmentDates.contains(date)) {
                    setStyle("-fx-background-color: #c0e57a; -fx-border-color: #556B2F;");
                }
            }
        });
    }

    private void setupCaseTable() {
        caseIdCol.setCellValueFactory(cell -> cell.getValue().idProperty());
        caseTypeCol.setCellValueFactory(cell -> cell.getValue().typeProperty());
        caseStatusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());
    }

    private void loadRecentCases(int lawyerId) {
        List<Case> cases = service.getAssignedCases(lawyerId);
        ObservableList<Case> caseList = FXCollections.observableArrayList(cases);
        recentCaseTable.setItems(caseList);
    }

    @FXML
    private void onDateSelected() {
        LocalDate selectedDate = calendar.getValue();
        if (selectedDate == null) return;

        List<Appointment> appts = service.getAppointmentsByDate(lawyerId, selectedDate);
        ObservableList<String> items = FXCollections.observableArrayList();

        for (Appointment appt : appts) {
            String line = appt.getDate() + " - " + appt.getTime() + " with " + appt.getClientName();
            items.add(line);
        }
        appointmentListView.setItems(items);
    }

    @FXML
    private void markCaseAsComplete() {
        Case selected = recentCaseTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = service.updateCaseStatus(selected.getId(), "Completed");
            if (success) {
                showAlert("Success", "Case marked as completed.");
                loadRecentCases(lawyerId); 
            } else {
                showAlert("Error", "Could not update case status.");
            }
        } else {
            showAlert("Warning", "Please select a case first.");
        }
    }

    @FXML
    private void updateProfile() {
        User lawyer = Session.getCurrentUser();
        if (lawyer != null) {
            lawyer.setName(fullNameField.getText());
            lawyer.setEmail(emailField.getText());
            lawyer.setPhone(phoneField.getText());
            lawyer.setAddress(addressField.getText());

            boolean success = service.updateLawyerProfile(lawyer);
            if (success) {
                showAlert("Success", "Profile updated successfully.");
                goBackToDashboard();
                welcomeLabel.setText("Welcome, " + lawyer.getName());
            } else {
                showAlert("Error", "Failed to update profile.");
            }
        }
    }

    @FXML
    private void onDashboardClicked() {
        goBackToDashboard();
    }

    @FXML
    private void onProfileClicked() {
        dashboardPane.setVisible(false);
        dashboardPane.setManaged(false);
        profilePane.setVisible(true);
        profilePane.setManaged(true);

        User lawyer = Session.getCurrentUser();
        if (lawyer != null) {
            fullNameField.setText(lawyer.getName());
            emailField.setText(lawyer.getEmail());
            phoneField.setText(lawyer.getPhone());
            addressField.setText(lawyer.getAddress());
        }
    }

    @FXML
    private void onAppointmentsClicked() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/LawyerAppointments.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("My Appointments");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Appointments view.");
        }
    }

    @FXML
    private void onLogout() {
        Session.clear();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/WelcomePage.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goBackToDashboard() {
        profilePane.setVisible(false);
        profilePane.setManaged(false);
        dashboardPane.setVisible(true);
        dashboardPane.setManaged(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
