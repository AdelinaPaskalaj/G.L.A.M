package lawoffice.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import lawoffice.service.AdminReportService;

public class AdminReportsController {

    @FXML private ComboBox<String> reportComboBox;
    @FXML private TextArea reportArea;

    private final AdminReportService reportService = new AdminReportService();

    @FXML
    public void initialize() {
        reportComboBox.getItems().addAll(
            "All Users",
            "All Cases",
            "All Invoices",
            "Appointments Overview"
        );
    }

    @FXML
    private void generateReport() {
        String selected = reportComboBox.getValue();
        if (selected == null) {
            reportArea.setText("Please select a report to generate.");
            return;
        }

        String report = reportService.generate(selected);
        reportArea.setText(report);
    }
}
