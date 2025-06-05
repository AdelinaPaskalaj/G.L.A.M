package lawoffice.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import lawoffice.model.Appointment;
import lawoffice.service.AdminScheduleService;

import java.util.List;

public class AdminSchedulesController {

    @FXML private TableView<Appointment> scheduleTable;
    @FXML private TableColumn<Appointment, Number> idCol;
    @FXML private TableColumn<Appointment, String> clientCol;
    @FXML private TableColumn<Appointment, String> lawyerCol;
    @FXML private TableColumn<Appointment, String> dateCol;
    @FXML private TableColumn<Appointment, String> statusCol;

    private final AdminScheduleService service = new AdminScheduleService();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(cell -> cell.getValue().idProperty());
        clientCol.setCellValueFactory(cell -> cell.getValue().clientNameProperty());
        lawyerCol.setCellValueFactory(cell -> cell.getValue().lawyerNameProperty());
        dateCol.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getFormattedDate() + " " + cell.getValue().getFormattedTime()));
        statusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());

        loadAppointments();
    }

    private void loadAppointments() {
        List<Appointment> appointments = service.getAllAppointments();
        ObservableList<Appointment> list = FXCollections.observableArrayList(appointments);
        scheduleTable.setItems(list);
    }

    @FXML
    private void markCompleted() {
        Appointment selected = scheduleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (service.updateAppointmentStatus(selected.getId(), "completed")) {
                showAlert("Success", "Appointment marked as completed.");
                loadAppointments();
            } else {
                showAlert("Error", "Could not update appointment status.");
            }
        }
    }

    @FXML
    private void markPaid() {
        Appointment selected = scheduleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (service.updateAppointmentStatus(selected.getId(), "paid")) {
                showAlert("Success", "Appointment marked as paid.");
                loadAppointments();
            } else {
                showAlert("Error", "Could not update appointment status.");
            }
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/AdminDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not go back to dashboard.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
