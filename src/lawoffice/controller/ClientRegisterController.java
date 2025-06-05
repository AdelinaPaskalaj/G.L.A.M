package lawoffice.controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import lawoffice.model.User;
import lawoffice.service.UserService;

public class ClientRegisterController {
    @FXML private TextField personalIdField;   
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private Label statusLabel;

    private final UserService svc = new UserService();

    @FXML
    private void onRegister(ActionEvent e) {
        if (personalIdField.getText().trim().isEmpty() ||
            firstNameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            passwordField.getText().trim().isEmpty()) {
            statusLabel.setText("Please fill in all required fields.");
            return;
        }

        User u = new User();
        u.setPersonalId(personalIdField.getText().trim());
        u.setName(firstNameField.getText().trim() + " " + lastNameField.getText().trim());
        u.setEmail(emailField.getText().trim());
        u.setPassword(passwordField.getText());
        u.setPhone(phoneField.getText().trim());
        u.setAddress(addressField.getText().trim());

        try {
            if (svc.registerClient(u)) {
                statusLabel.setText("Register successfully!");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> navigate(e, "/lawoffice/view/ClientLogin.fxml"));
                pause.play();
            } else {
                statusLabel.setText("Registration failed: Personal ID or Email may already exist.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Unexpected error during registration.");
        }
    }

    @FXML
    private void onBack(ActionEvent e) {
        navigate(e, "/lawoffice/view/WelcomePage.fxml");
    }

    private void navigate(ActionEvent e, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Navigation error.");
        }
    }
}
