package lawoffice.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lawoffice.model.User;
import lawoffice.session.Session;
import lawoffice.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ClientProfileController {

    @FXML private Label nameLabel;
    @FXML private Label personalIdLabel;
    @FXML private Label roleLabel;
    @FXML private Label statusLabel;

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML private Label statusMessage;

    private User user;

    @FXML
    public void initialize() {
        user = Session.getCurrentUser();
        if (user != null) {
            nameLabel.setText(user.getName());
            personalIdLabel.setText(user.getPersonalId());
            roleLabel.setText(user.getRole());
            statusLabel.setText(user.getStatus());

            emailField.setText(user.getEmail());
            passwordField.setText(user.getPassword());
            phoneField.setText(user.getPhone());
            addressField.setText(user.getAddress());
        }
    }

    @FXML
    private void onUpdate() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            statusMessage.setText("Email and password are required.");
            return;
        }

        try (Connection conn = DBUtil.getInstance().getConnection()) {
            String sql = "UPDATE users SET email = ?, password = ?, phone = ?, address = ? WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                stmt.setString(3, phone);
                stmt.setString(4, address);
                stmt.setInt(5, user.getUserId());
                int rows = stmt.executeUpdate();

                if (rows > 0) {
                    statusMessage.setText("Profile updated successfully.");
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setPhone(phone);
                    user.setAddress(address);
                } else {
                    statusMessage.setText("Update failed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusMessage.setText("Database error.");
        }
    }

    @FXML
    private void onBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/ClientDashboard.fxml"));
            Stage stage = (Stage) nameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Client Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
