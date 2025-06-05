package lawoffice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lawoffice.model.User;
import lawoffice.session.Session;
import lawoffice.util.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class RegisterCaseController {

    @FXML private ComboBox<String> caseTypeComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private ComboBox<LocalTime> timeComboBox;
    @FXML private TextArea detailsArea;
    @FXML private TextField feeField;
    @FXML private Button submitButton;

    private final Map<String, Double> feesMap = new HashMap<>();

    @FXML
    public void initialize() {
        caseTypeComboBox.getItems().addAll("Visa", "Civil", "Family", "Criminal");
        caseTypeComboBox.setValue("Visa");

        feesMap.put("Visa", 150.0);
        feesMap.put("Civil", 75.0);
        feesMap.put("Family", 90.0);
        feesMap.put("Criminal", 300.0);

        updateFee(caseTypeComboBox.getValue());

        caseTypeComboBox.setOnAction(event -> updateFee(caseTypeComboBox.getValue()));

        // Populate available times (09:00–17:30 every 30 mins)
        ObservableList<LocalTime> times = FXCollections.observableArrayList();
        for (int hour = 9; hour <= 17; hour++) {
            times.add(LocalTime.of(hour, 0));
            times.add(LocalTime.of(hour, 30));
        }
        timeComboBox.setItems(times);
        timeComboBox.setValue(LocalTime.of(9, 0));
    }

    private void updateFee(String caseType) {
        Double fee = feesMap.get(caseType);
        feeField.setText(fee != null ? String.format("%.2f", fee) : "0.00");
    }

    @FXML
    private void submitCase() {
        User user = Session.getCurrentUser();
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Session Error", "You are not logged in. Please login again.");
            return;
        }

        String caseType = caseTypeComboBox.getValue();
        if (caseType == null || caseType.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a case type.");
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        if (startDate == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Start date must be filled.");
            return;
        }

        LocalTime time = timeComboBox.getValue();
        if (time == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Appointment time must be selected.");
            return;
        }

        String details = detailsArea.getText().trim();

        double fee;
        try {
            fee = Double.parseDouble(feeField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Fee must be a valid number.");
            return;
        }

        try (Connection conn = DBUtil.getInstance().getConnection()) {
            int clientId = getClientIdFromUserId(user.getUserId(), conn);
            if (clientId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Client not found.");
                return;
            }

            String sql = "INSERT INTO cases (client_id, case_type, start_date, appointment_time, status, details, estimated_fee, is_self_registered) " +
                         "VALUES (?, ?, ?, ?, 'Pending', ?, ?, true)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, clientId);
                stmt.setString(2, caseType);
                stmt.setDate(3, java.sql.Date.valueOf(startDate));
                stmt.setTime(4, java.sql.Time.valueOf(time));
                stmt.setString(5, details);
                stmt.setDouble(6, fee);

                stmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Case registered successfully.");
                switchToDashboard();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while saving the case.");
        }
    }

    private int getClientIdFromUserId(int userId, Connection conn) throws Exception {
        String query = "SELECT client_id FROM clients WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("client_id");
        }
        return -1;
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    private void switchToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lawoffice/view/ClientDashboard.fxml"));
            Parent root = loader.load();
            ClientDashboardController controller = loader.getController();
            controller.refreshDashboard();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Client Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load client dashboard.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
