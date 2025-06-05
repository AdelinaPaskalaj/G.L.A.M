package lawoffice.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private Button adminLoginButton;

    @FXML
    private Button lawyerLoginButton;

    @FXML
    private Button clientLoginButton;

    @FXML
    private Label registerLink;

    @FXML
    private void adminLoginButtonOnAction() {
        loadScene("/lawoffice/view/adminlogin.fxml");
    }

    @FXML
    private void lawyerLoginButtonOnAction() {
        loadScene("/lawoffice/view/lawyerlogin.fxml");
    }

    @FXML
    private void clientLoginButtonOnAction() {
        loadScene("/lawoffice/view/clientlogin.fxml");
    }

    @FXML
    private void registerLinkOnClick(MouseEvent event) {
        loadScene("/lawoffice/view/clientregister.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Law Office Management System");
            stage.show();

            // Optional: hide current window
            Stage currentStage = (Stage) adminLoginButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
