package lawoffice.model;

import javafx.beans.property.*;

public class Case {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty clientName = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty startDate = new SimpleStringProperty();
    private final StringProperty appointmentTime = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    public Case() {}

    public Case(int id, String clientName, String title, String status, String description) {
        this.id.set(id);
        this.clientName.set(clientName);
        this.title.set(title);
        this.status.set(status);
        this.description.set(description);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getClientName() {
        return clientName.get();
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }

    public StringProperty clientNameProperty() {
        return clientName;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getStartDate() {
        return startDate.get();
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public String getAppointmentTime() {
        return appointmentTime.get();
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime.set(appointmentTime);
    }

    public StringProperty appointmentTimeProperty() {
        return appointmentTime;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
    public void setStartDate(java.time.LocalDate localDate) {
        this.startDate.set(localDate != null ? localDate.toString() : "");
    }
}
