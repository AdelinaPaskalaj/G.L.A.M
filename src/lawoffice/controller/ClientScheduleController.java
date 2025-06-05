package lawoffice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import lawoffice.model.Appointment;
import lawoffice.service.ClientDashboardService;
import lawoffice.session.Session;

import java.util.List;

public class ClientScheduleController {

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> lawyerNameCol;
    @FXML private TableColumn<Appointment, String> dateCol;
    @FXML private TableColumn<Appointment, String> timeCol;
    @FXML private TableColumn<Appointment, String> statusCol;

    private final ClientDashboardService service = new ClientDashboardService();

    @FXML
    public void initialize() {
        lawyerNameCol.setCellValueFactory(new PropertyValueFactory<>("lawyerName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("formattedTime"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadAppointments();
    }

    private void loadAppointments() {
        int clientId = Session.getCurrentUser().getUserId();
        List<Appointment> appointments = service.getUpcomingAppointmentsDetails(clientId);
        ObservableList<Appointment> list = FXCollections.observableArrayList(appointments);
        appointmentTable.setItems(list);
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/ClientDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
