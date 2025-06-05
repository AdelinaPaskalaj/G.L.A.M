package lawoffice.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lawoffice.model.User;
import lawoffice.service.UserService;
import lawoffice.session.Session;

public class LawyerLoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final UserService svc = new UserService();

    @FXML
    private void onLogin(ActionEvent e) {
        User u = svc.login(emailField.getText().trim(), passwordField.getText());
        if (u != null && "Lawyer".equals(u.getRole())) {
            // Set the logged in user in session
            Session.setCurrentUser(u);

            // Navigate to Lawyer Dashboard
            navigate(e, "/lawoffice/view/LawyerDashboard.fxml");
        } else {
            statusLabel.setText("Invalid credentials");
            // Optionally clear password field
            passwordField.clear();
        }
    }

    @FXML
    private void onBack(ActionEvent e) {
        navigate(e, "/lawoffice/view/WelcomePage.fxml");
    }

    private void navigate(ActionEvent e, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false); 
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
