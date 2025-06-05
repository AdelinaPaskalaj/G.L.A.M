package lawoffice.controller;

import javafx.beans.property.SimpleStringProperty;
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
import lawoffice.model.Invoice;
import lawoffice.service.ClientDashboardService;
import lawoffice.session.Session;

import java.util.List;

public class ClientInvoicesController {

    @FXML private TableView<Invoice> invoiceTable;
    @FXML private TableColumn<Invoice, Number> invoiceIdCol;
    @FXML private TableColumn<Invoice, String> caseTitleCol;
    @FXML private TableColumn<Invoice, Number> amountCol;
    @FXML private TableColumn<Invoice, String> dueDateCol;
    @FXML private TableColumn<Invoice, String> statusCol;

    private final ClientDashboardService service = new ClientDashboardService();

    @FXML
    public void initialize() {
        invoiceIdCol.setCellValueFactory(data -> data.getValue().idProperty());
        caseTitleCol.setCellValueFactory(data -> data.getValue().caseTitleProperty());
        amountCol.setCellValueFactory(data -> data.getValue().amountProperty());
        dueDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDueDate().toString()));
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());

        loadInvoices();
    }

    private void loadInvoices() {
        int clientId = service.getClientIdByUserId(Session.getCurrentUser().getUserId());
        List<Invoice> invoices = service.getInvoicesByClientId(clientId);
        ObservableList<Invoice> observableInvoices = FXCollections.observableArrayList(invoices);
        invoiceTable.setItems(observableInvoices);
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
