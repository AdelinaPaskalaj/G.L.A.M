package lawoffice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import lawoffice.model.Case;
import lawoffice.service.ClientDashboardService;
import lawoffice.session.Session;
import lawoffice.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ClientCaseHistoryController {

    @FXML private TableView<Case> caseTable;
    @FXML private TableColumn<Case, Number> caseIdCol;
    @FXML private TableColumn<Case, String> titleCol;
    @FXML private TableColumn<Case, String> typeCol;
    @FXML private TableColumn<Case, String> statusCol;
    @FXML private TableColumn<Case, String> startDateCol;
    @FXML private TableColumn<Case, String> timeCol;

    private final ClientDashboardService service = new ClientDashboardService();

    @FXML
    public void initialize() {
        caseIdCol.setCellValueFactory(cell -> cell.getValue().idProperty());
        titleCol.setCellValueFactory(cell -> cell.getValue().titleProperty());
        typeCol.setCellValueFactory(cell -> cell.getValue().typeProperty());
        statusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());
        startDateCol.setCellValueFactory(cell -> cell.getValue().startDateProperty());
        timeCol.setCellValueFactory(cell -> cell.getValue().appointmentTimeProperty());

        loadCaseHistory();
    }

    private void loadCaseHistory() {
        int userId = Session.getCurrentUser().getUserId();
        int clientId = getClientIdByUserId(userId);
        if (clientId == -1) return;

        ObservableList<Case> caseList = FXCollections.observableArrayList();
        String sql = "SELECT case_id, case_type, status, start_date, appointment_time FROM cases WHERE client_id = ?";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Case c = new Case();
                c.setId(rs.getInt("case_id"));
                c.setTitle("Case #" + rs.getInt("case_id"));
                c.setType(rs.getString("case_type"));
                c.setStatus(rs.getString("status"));
                if (rs.getDate("start_date") != null)
                    c.setStartDate(rs.getDate("start_date").toLocalDate().toString());
                if (rs.getTime("appointment_time") != null)
                    c.setAppointmentTime(rs.getTime("appointment_time").toLocalTime().toString());

                caseList.add(c);
            }

            caseTable.setItems(caseList);

        } catch (Exception e) {
            e.printStackTrace();
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
