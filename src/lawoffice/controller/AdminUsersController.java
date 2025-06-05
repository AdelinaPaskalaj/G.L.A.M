package lawoffice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lawoffice.model.User;
import lawoffice.service.AdminUserService;

public class AdminUsersController {

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, String> statusColumn;

    private final AdminUserService userService = new AdminUserService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadUsers();
    }

    private void loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList(userService.getAllUsers());
        usersTable.setItems(users);
    }

    @FXML
    private void onActivateUser() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = userService.setUserStatus(selected.getUserId(), "Active");
            if (success) {
                showAlert("User activated.");
                loadUsers();
            } else {
                showAlert("Failed to activate user.");
            }
        } else {
            showAlert("Please select a user to activate.");
        }
    }

    @FXML
    private void onDeactivateUser() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = userService.setUserStatus(selected.getUserId(), "Inactive");
            if (success) {
                showAlert("User deactivated.");
                loadUsers();
            } else {
                showAlert("Failed to deactivate user.");
            }
        } else {
            showAlert("Please select a user to deactivate.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Status Update");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void updateUserProfile(ActionEvent event) {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lawoffice/view/AdminUserEdit.fxml"));
                Parent root = loader.load();

                AdminUserEditController controller = loader.getController();
                controller.setUser(selected);

                Stage stage = new Stage();
                stage.setTitle("Edit User Profile");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a user to update.");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lawoffice/view/AdminDashboard.fxml"));
            Stage stage = (Stage) usersTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
