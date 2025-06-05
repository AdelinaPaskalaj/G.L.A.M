package lawoffice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lawoffice.model.Appointment;
import lawoffice.service.LawyerDashboardService;
import lawoffice.session.Session;

import java.io.IOException;
import java.util.List;

public class LawyerAppointmentsController {

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> clientCol;
    @FXML private TableColumn<Appointment, String> dateCol;
    @FXML private TableColumn<Appointment, String> timeCol;
    @FXML private TableColumn<Appointment, String> statusCol;

    private final LawyerDashboardService service = new LawyerDashboardService();

    @FXML
    public void initialize() {
        clientCol.setCellValueFactory(data -> data.getValue().clientNameProperty());
        dateCol.setCellValueFactory(data -> data.getValue().dateProperty().asString());
        timeCol.setCellValueFactory(data -> data.getValue().timeProperty().asString());
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());

        loadAppointments();
    }

    private void loadAppointments() {
        int lawyerId = service.getLawyerIdByUserId(Session.getCurrentUser().getUserId());
        List<Appointment> list = service.getAppointmentsForLawyer(lawyerId);
        ObservableList<Appointment> obsList = FXCollections.observableArrayList(list);
        appointmentsTable.setItems(obsList);
    }

    @FXML
    private void goBack(ActionEvent event) {
        navigate(event, "/lawoffice/view/LawyerDashboard.fxml");
    }

    @FXML
    private void onLogout(ActionEvent event) {
        Session.clear();
        navigate(event, "/lawoffice/view/WelcomePage.fxml");
    }

    private void navigate(ActionEvent event, String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
