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
import java.sql.ResultSet;

public class LawyerProfileController {

    @FXML private Label nameLabel;
    @FXML private Label specializationLabel;
    @FXML private Label titleLabel;

    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML private Label statusMessage;

    private User user;

    @FXML
    public void initialize() {
        user = Session.getCurrentUser();
        if (user != null) {
            nameLabel.setText(user.getName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhone());
            addressField.setText(user.getAddress());
            loadLawyerData(user.getUserId());
        }
    }

    private void loadLawyerData(int userId) {
        String sql = "SELECT specialization, title FROM lawyers WHERE user_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                specializationLabel.setText(rs.getString("specialization"));
                titleLabel.setText(rs.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onUpdate() {
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();

        if (email.isEmpty()) {
            statusMessage.setText("Email is required.");
            return;
        }

        try (Connection conn = DBUtil.getInstance().getConnection()) {
            String sql = "UPDATE users SET email = ?, phone = ?, address = ? WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, phone);
                stmt.setString(3, address);
                stmt.setInt(4, user.getUserId());
                int rows = stmt.executeUpdate();

                if (rows > 0) {
                    statusMessage.setText("Profile updated successfully.");
                    user.setEmail(email);
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
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/LawyerDashboard.fxml"));
            Stage stage = (Stage) nameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Lawyer Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
