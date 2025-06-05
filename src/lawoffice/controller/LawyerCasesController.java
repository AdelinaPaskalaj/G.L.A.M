package lawoffice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import lawoffice.model.Case;
import lawoffice.model.User;
import lawoffice.service.LawyerDashboardService;
import lawoffice.service.LawyerCaseService;
import lawoffice.session.Session;

import java.io.IOException;
import java.util.List;

public class LawyerCasesController {

    @FXML private TableView<Case> caseTable;
    @FXML private TableColumn<Case, Integer> caseIdCol;
    @FXML private TableColumn<Case, String> caseTitleCol;
    @FXML private TableColumn<Case, String> caseStatusCol;
    @FXML private TableColumn<Case, String> caseDescriptionCol;

    @FXML private ChoiceBox<String> statusChoiceBox;
    @FXML private TextArea descriptionArea;

    private final LawyerDashboardService dashboardService = new LawyerDashboardService();
    private final LawyerCaseService caseService = new LawyerCaseService();

    @FXML
    public void initialize() {
        caseIdCol.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        caseTitleCol.setCellValueFactory(cell -> cell.getValue().titleProperty());
        caseStatusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());
        caseDescriptionCol.setCellValueFactory(cell -> cell.getValue().descriptionProperty());

        statusChoiceBox.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Closed"));

        loadCases();
    }

    private void loadCases() {
        User lawyer = Session.getCurrentUser();
        if (lawyer != null) {
            List<Case> cases = dashboardService.getAssignedCases(lawyer.getUserId());
            caseTable.setItems(FXCollections.observableArrayList(cases));
        }
    }

    @FXML
    private void updateCaseStatus() {
        Case selected = caseTable.getSelectionModel().getSelectedItem();
        String newStatus = statusChoiceBox.getValue();

        if (selected != null && newStatus != null) {
            caseService.updateCaseStatus(selected.getId(), newStatus);
            showAlert("Success", "Case status updated.");
            loadCases();
        } else {
            showAlert("Error", "Please select a case and a status.");
        }
    }

    @FXML
    private void updateCaseDescription() {
        Case selected = caseTable.getSelectionModel().getSelectedItem();
        String newDescription = descriptionArea.getText().trim();

        if (selected != null && !newDescription.isEmpty()) {
            caseService.updateCaseDescription(selected.getId(), newDescription);
            showAlert("Success", "Case description updated.");
            loadCases();
        } else {
            showAlert("Error", "Please select a case and write a description.");
        }
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        navigate(event, "/lawoffice/view/LawyerDashboard.fxml");
    }

    @FXML
    private void goToAppointments(ActionEvent event) {
        navigate(event, "/lawoffice/view/LawyerAppointments.fxml");
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
