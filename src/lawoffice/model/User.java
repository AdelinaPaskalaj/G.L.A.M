package lawoffice.model;

import javafx.beans.property.*;

public class User {

    private final IntegerProperty userId = new SimpleIntegerProperty(this, "userId");
    private final StringProperty personalId = new SimpleStringProperty(this, "personalId");
    private final StringProperty name = new SimpleStringProperty(this, "name");
    private final StringProperty email = new SimpleStringProperty(this, "email");
    private final StringProperty phone = new SimpleStringProperty(this, "phone");
    private final StringProperty address = new SimpleStringProperty(this, "address");
    private final StringProperty password = new SimpleStringProperty(this, "password");
    private final StringProperty role = new SimpleStringProperty(this, "role");
    private final StringProperty status = new SimpleStringProperty(this, "status");

    private final IntegerProperty clientId = new SimpleIntegerProperty(this, "clientId");

    public User() {}

    public User(int userId, String personalId, String name, String email, String phone,
                String address, String password, String role, String status) {
        this.userId.set(userId);
        this.personalId.set(personalId);
        this.name.set(name);
        this.email.set(email);
        this.phone.set(phone);
        this.address.set(address);
        this.password.set(password);
        this.role.set(role);
        this.status.set(status);
    }

    public User(int userId, String name) {
        this.userId.set(userId);
        this.name.set(name);
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public StringProperty personalIdProperty() {
        return personalId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty roleProperty() {
        return role;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public int getUserId() {
        return userId.get();
    }

    public String getPersonalId() {
        return personalId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getRole() {
        return role.get();
    }

    public String getStatus() {
        return status.get();
    }

    public int getClientId() {
        return clientId.get();
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public void setPersonalId(String personalId) {
        this.personalId.set(personalId);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setClientId(int clientId) {
        this.clientId.set(clientId);
    }

    @Override
    public String toString() {
        return getName() + " (ID: " + getUserId() + ")";
    }
}
