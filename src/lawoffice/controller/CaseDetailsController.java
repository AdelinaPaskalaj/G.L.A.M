package lawoffice.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lawoffice.model.Case;

public class CaseDetailsController {

    @FXML private Label caseIdLabel;
    @FXML private Label titleLabel;
    @FXML private Label statusLabel;
    @FXML private TextArea descriptionArea;

    private Case selectedCase;

    public void setCase(Case selectedCase) {
        this.selectedCase = selectedCase;
        caseIdLabel.setText(String.valueOf(selectedCase.getId()));
        titleLabel.setText(selectedCase.getTitle());
        statusLabel.setText(selectedCase.getStatus());
        descriptionArea.setText(selectedCase.getDescription());
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) caseIdLabel.getScene().getWindow();
        stage.close();
    }
}
