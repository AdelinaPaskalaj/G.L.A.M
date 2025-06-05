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

import java.time.LocalDate;
import lawoffice.model.Invoice;
import lawoffice.service.AdminInvoiceService;

public class AdminInvoicesController {

    @FXML private TableView<Invoice> invoiceTable;
    @FXML private TableColumn<Invoice, Number> invoiceIdCol;
    @FXML private TableColumn<Invoice, String> clientCol;
    @FXML private TableColumn<Invoice, String> caseCol;
    @FXML private TableColumn<Invoice, Number> amountCol;
    @FXML private TableColumn<Invoice, LocalDate> dueDateCol;
    @FXML private TableColumn<Invoice, String> statusCol;

    private final AdminInvoiceService invoiceService = new AdminInvoiceService();

    @FXML
    public void initialize() {
        invoiceIdCol.setCellValueFactory(cell -> cell.getValue().idProperty());
        clientCol.setCellValueFactory(cell -> cell.getValue().clientNameProperty());
        caseCol.setCellValueFactory(cell -> cell.getValue().caseTitleProperty());
        amountCol.setCellValueFactory(cell -> cell.getValue().amountProperty());
        dueDateCol.setCellValueFactory(cell -> cell.getValue().dueDateProperty());
        statusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());

        loadInvoices();
    }

    private void loadInvoices() {
        ObservableList<Invoice> invoices = FXCollections.observableArrayList(invoiceService.getAllInvoices());
        invoiceTable.setItems(invoices);
    }

    @FXML
    private void markPaid() {
        Invoice selected = invoiceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            invoiceService.updateStatus(selected.getId(), "paid");
            loadInvoices();
            showAlert("Success", "Invoice marked as paid.");
        } else {
            showAlert("Error", "Please select an invoice.");
        }
    }

    @FXML
    private void deleteInvoice() {
        Invoice selected = invoiceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            invoiceService.deleteInvoice(selected.getId());
            loadInvoices();
            showAlert("Deleted", "Invoice has been deleted.");
        } else {
            showAlert("Error", "Please select an invoice.");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/AdminDashboard.fxml"));
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load dashboard.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
