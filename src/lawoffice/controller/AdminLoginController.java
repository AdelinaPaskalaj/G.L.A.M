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

public class AdminLoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final UserService svc = new UserService();

    @FXML
    private void onLogin(ActionEvent e) {
        User u = svc.login(emailField.getText().trim(), passwordField.getText());
        if (u != null && "Admin".equals(u.getRole())) {
        	 Session.setCurrentUser(u);
            navigate(e, "/lawoffice/view/AdminDashboard.fxml");
        } else {
            statusLabel.setText("Invalid credentials");
        }
    }

    @FXML
    private void onBack(ActionEvent e) {
        navigate(e, "/lawoffice/view/WelcomePage.fxml");
    }

    private void navigate(ActionEvent e, String fxml) {
        try {
            Parent r = FXMLLoader.load(getClass().getResource(fxml));
            Stage s = (Stage)((Node)e.getSource()).getScene().getWindow();
            s.setScene(new Scene(r));
            s.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
