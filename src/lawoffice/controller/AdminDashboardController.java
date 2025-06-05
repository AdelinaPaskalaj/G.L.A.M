package lawoffice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import lawoffice.model.Appointment;
import lawoffice.service.AdminDashboardService;
import lawoffice.service.AdminAppointmentService;
import lawoffice.session.Session;

import java.io.IOException;
import java.util.List;

public class AdminDashboardController {

    @FXML private Label adminWelcomeLabel;
    @FXML private Label totalUsersLabel;
    @FXML private Label openCasesLabel;
    @FXML private Label clientSchedulesLabel;
    @FXML private Label invoicesLabel;

    @FXML private TableView<Appointment> recentAppointmentsTable;
    @FXML private TableColumn<Appointment, String> clientCol;
    @FXML private TableColumn<Appointment, String> lawyerCol;
    @FXML private TableColumn<Appointment, String> dateCol;
    @FXML private TableColumn<Appointment, String> timeCol;

    private final AdminDashboardService service = new AdminDashboardService();
    private final AdminAppointmentService appointmentService = new AdminAppointmentService();

    @FXML
    public void initialize() {
        String name = Session.getCurrentUser().getName();
        adminWelcomeLabel.setText("Welcome, " + name);
        loadDashboardData();
        setupAppointmentTable();
        loadRecentAppointments();
    }

    private void loadDashboardData() {
        totalUsersLabel.setText(String.valueOf(service.countAllUsers()));
        openCasesLabel.setText(String.valueOf(service.countOpenCases()));
        clientSchedulesLabel.setText(String.valueOf(service.countSchedules()));
        invoicesLabel.setText(String.valueOf(service.countAllInvoices()));
    }

    private void setupAppointmentTable() {
        if (clientCol != null && lawyerCol != null && dateCol != null && timeCol != null) {
            clientCol.setCellValueFactory(cell -> cell.getValue().clientNameProperty());
            lawyerCol.setCellValueFactory(cell -> cell.getValue().lawyerNameProperty());
            dateCol.setCellValueFactory(cell -> cell.getValue().dateProperty().asString());
            timeCol.setCellValueFactory(cell -> cell.getValue().timeProperty().asString());
        }
    }

    private void loadRecentAppointments() {
        if (recentAppointmentsTable != null) {
            List<Appointment> list = appointmentService.getRecentAppointments();
            ObservableList<Appointment> observableList = FXCollections.observableArrayList(list);
            recentAppointmentsTable.setItems(observableList);
        }
    }


    @FXML
    private void goToDashboard() {
        Stage stage = (Stage) adminWelcomeLabel.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/AdminDashboard.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        navigate(event, "/lawoffice/view/AdminDashboard.fxml");
    }

    @FXML
    private void goToUsers(ActionEvent event) {
        navigate(event, "/lawoffice/view/AdminUsers.fxml");
    }

    @FXML
    private void goToCases(ActionEvent event) {
        navigate(event, "/lawoffice/view/AdminCases.fxml");
    }

    @FXML
    private void goToSchedules(ActionEvent event) {
        navigate(event, "/lawoffice/view/AdminSchedules.fxml");
    }

    @FXML
    private void goToInvoices(ActionEvent event) {
        navigate(event, "/lawoffice/view/AdminInvoices.fxml");
    }

    @FXML
    private void goToReports(ActionEvent event) {
        navigate(event, "/lawoffice/view/AdminReports.fxml");
    }

    @FXML
    private void goToNotifications(ActionEvent event) {
        navigate(event, "/lawoffice/view/AdminNotifications.fxml");
    }

   

    @FXML
    private void onLogout(ActionEvent event) {
        Session.clear();
        navigate(event, "/lawoffice/view/WelcomePage.fxml");
    }

    private void navigate(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
