package lawoffice.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lawoffice.model.User;
import lawoffice.service.AdminUserService;

public class AdminUserEditController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;

    private User user;
    private final AdminUserService userService = new AdminUserService();

    public void setUser(User user) {
        this.user = user;
        fullNameField.setText(user.getName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
        addressField.setText(user.getAddress());
    }

    @FXML
    private void saveUserChanges() {
        if (user != null) {
            user.setName(fullNameField.getText());
            user.setEmail(emailField.getText());
            user.setPhone(phoneField.getText());
            user.setAddress(addressField.getText());

            String newPassword = passwordField.getText();
            if (!newPassword.isBlank()) {
                user.setPassword(newPassword);
            }

            userService.updateUserProfile(user);
            closeWindow();
        }
    }

    @FXML
    private void cancelEdit() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.close();
    }
}
