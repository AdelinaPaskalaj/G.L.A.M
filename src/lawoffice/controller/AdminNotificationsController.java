package lawoffice.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lawoffice.service.AdminNotificationService;

public class AdminNotificationsController {

    @FXML private TextArea messageField;

    private final AdminNotificationService service = new AdminNotificationService();

    @FXML
    private void sendNotification() {
        String msg = messageField.getText().trim();
        if (msg.isEmpty()) {
            showAlert("Warning", "Please write a message before sending.");
            return;
        }

        boolean success = service.sendToAllUsers(msg);
        if (success) {
            messageField.clear();
            showAlert("Success", "Notification sent to all users.");
        } else {
            showAlert("Error", "Failed to send notification.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void goBackToDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/AdminDashboard.fxml"));
            Stage stage = (Stage) messageField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not return to Dashboard.");
        }
    }

}
