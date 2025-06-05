package lawoffice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lawoffice.model.Case;
import lawoffice.model.User;
import lawoffice.service.ClientDashboardService;
import lawoffice.session.Session;
import lawoffice.util.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label totalCasesLabel;
    @FXML private Label activeCasesLabel;
    @FXML private Label pendingInvoicesLabel;
    @FXML private Label upcomingAppointmentsLabel;

    @FXML private TableView<Case> caseTable;
    @FXML private TableColumn<Case, Number> caseIdColumn;
    @FXML private TableColumn<Case, String> caseTitleColumn;
    @FXML private TableColumn<Case, String> caseTypeColumn;
    @FXML private TableColumn<Case, String> caseStatusColumn;
    @FXML private TableColumn<Case, String> startDateColumn;
    @FXML private TableColumn<Case, String> appointmentTimeColumn;

    private final ClientDashboardService dashboardService = new ClientDashboardService();

    @FXML
    public void initialize() {
        User client = Session.getCurrentUser();
        if (client != null) {
            welcomeLabel.setText("Welcome, " + client.getName() + "!");
            int clientId = getClientIdByUserId(client.getUserId());

            if (clientId != -1) {
                configureCaseTable();
                loadOverviewData(clientId);
                loadRecentCases(clientId);
            } else {
                showAlert("Error", "Client ID not found.");
            }
        }
    }

    private void configureCaseTable() {
        caseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        caseTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        caseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        caseStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        if (appointmentTimeColumn != null) {
            appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        }
    }

    private void loadOverviewData(int clientId) {
        totalCasesLabel.setText(String.valueOf(dashboardService.getTotalCases(clientId)));
        activeCasesLabel.setText(String.valueOf(dashboardService.getActiveCases(clientId)));
        pendingInvoicesLabel.setText(String.valueOf(dashboardService.getPendingInvoices(clientId)));
        upcomingAppointmentsLabel.setText(String.valueOf(dashboardService.getUpcomingAppointments(clientId)));
    }

    private void loadRecentCases(int clientId) {
        ObservableList<Case> caseList = FXCollections.observableArrayList();
        String sql = """
            SELECT case_id, case_type, status, start_date, appointment_time
            FROM cases
            WHERE client_id = ?
            ORDER BY start_date DESC
        """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Case c = new Case();
                int id = rs.getInt("case_id");
                c.setId(id);
                c.setTitle("Case #" + id);
                c.setType(rs.getString("case_type"));
                c.setStatus(rs.getString("status"));
                if (rs.getDate("start_date") != null)
                    c.setStartDate(rs.getDate("start_date").toString());
                if (rs.getTime("appointment_time") != null)
                    c.setAppointmentTime(rs.getTime("appointment_time").toLocalTime().toString());
                caseList.add(c);
            }

            caseTable.setItems(caseList);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Load Error", "Could not load recent cases.");
        }
    }

    private int getClientIdByUserId(int userId) {
        String sql = "SELECT client_id FROM clients WHERE user_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("client_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @FXML
    private void onLogout() {
        Session.clear();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/ClientLogin.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Logout Error", "Unable to load login screen.");
        }
    }

    private void openPopupWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open window: " + title);
        }
    }

    public void refreshDashboard() {
        User client = Session.getCurrentUser();
        if (client != null) {
            int clientId = getClientIdByUserId(client.getUserId());
            if (clientId != -1) {
                loadOverviewData(clientId);
                loadRecentCases(clientId);
            }
        }
    }

    private void showAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private void goToDashboard() {
        refreshDashboard();
    }

    @FXML private void onAddRegisterCase() {
        openPopupWindow("/lawoffice/view/RegisterCaseForm.fxml", "Register New Case");
    }

    @FXML private void goToSchedule() {
        openPopupWindow("/lawoffice/view/ClientSchedule.fxml", "My Schedule");
    }

    @FXML private void goToInvoices() {
        openPopupWindow("/lawoffice/view/ClientInvoices.fxml", "My Invoices");
    }

    @FXML private void goToProfile() {
        openPopupWindow("/lawoffice/view/ClientProfile.fxml", "My Profile");
    }

    @FXML private void goToCases() {
        openPopupWindow("/lawoffice/view/ClientCaseHistory.fxml", "My Cases");
    }
}
