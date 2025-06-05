package lawoffice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import lawoffice.model.Case;
import lawoffice.model.User;
import lawoffice.service.AdminCaseService;

public class AdminCasesController {

    @FXML private TableView<Case> caseTable;
    @FXML private TableColumn<Case, Number> idCol;
    @FXML private TableColumn<Case, String> clientCol;
    @FXML private TableColumn<Case, String> titleCol;
    @FXML private TableColumn<Case, String> statusCol;
    @FXML private TableColumn<Case, String> startDateCol;
    @FXML private TableColumn<Case, String> appointmentTimeCol;
    @FXML private ComboBox<User> lawyerComboBox;

    private final AdminCaseService service = new AdminCaseService();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(data -> data.getValue().idProperty());
        clientCol.setCellValueFactory(data -> data.getValue().clientNameProperty());
        titleCol.setCellValueFactory(data -> data.getValue().titleProperty());
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());
        startDateCol.setCellValueFactory(data -> data.getValue().startDateProperty());
        appointmentTimeCol.setCellValueFactory(data -> data.getValue().appointmentTimeProperty());

        loadCases();
        loadLawyers();
    }

    private void loadCases() {
        ObservableList<Case> cases = FXCollections.observableArrayList(service.getPendingCases());
        caseTable.setItems(cases);
    }

    private void loadLawyers() {
        ObservableList<User> lawyers = FXCollections.observableArrayList(service.getAllLawyers());
        lawyerComboBox.setItems(lawyers);
    }

    @FXML
    private void assignLawyer() {
        Case selectedCase = caseTable.getSelectionModel().getSelectedItem();
        User selectedLawyer = lawyerComboBox.getSelectionModel().getSelectedItem();

        if (selectedCase != null && selectedLawyer != null) {
            service.assignLawyerUsingCaseData(selectedCase.getId(), selectedLawyer.getUserId());
            loadCases();
            showAlert("Success", "Lawyer assigned and appointment created.");
        } else {
            showAlert("Error", "Please select both a case and a lawyer.");
        }
    }

    @FXML
    private void updateStatus() {
        Case selectedCase = caseTable.getSelectionModel().getSelectedItem();
        if (selectedCase != null) {
            String newStatus = promptForStatus(selectedCase.getStatus());
            if (newStatus != null && !newStatus.isBlank()) {
                service.updateStatus(selectedCase.getId(), newStatus);
                loadCases();
                showAlert("Success", "Case status updated.");
            }
        }
    }

    @FXML
    private void goBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/AdminDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dashboard.");
        }
    }

    private String promptForStatus(String currentStatus) {
        TextInputDialog dialog = new TextInputDialog(currentStatus);
        dialog.setHeaderText("Update Case Status");
        dialog.setContentText("Enter new status (Pending, Assigned, In Progress, Closed):");
        return dialog.showAndWait().orElse(null);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
