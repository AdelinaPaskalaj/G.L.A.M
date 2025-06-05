package lawoffice.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;

public class AdminInvoiceGeneratorController {

    @FXML private TextField clientIdField;
    @FXML private TextField caseIdField;
    @FXML private TextField amountField;
    @FXML private DatePicker dueDatePicker;
    @FXML private Button generateButton;

    @FXML
    public void initialize() {
        generateButton.setOnAction(e -> {
            String clientId = clientIdField.getText();
            String caseId = caseIdField.getText();
            String amount = amountField.getText();
            String dueDate = dueDatePicker.getValue() != null ? dueDatePicker.getValue().toString() : "";

            clientIdField.clear();
            caseIdField.clear();
            amountField.clear();
            dueDatePicker.setValue(null);
        });
    }
}
