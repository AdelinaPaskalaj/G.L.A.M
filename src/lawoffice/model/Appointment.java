package lawoffice.model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Appointment {

    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(this, "date");

    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(this, "time");

    private final StringProperty lawyerName = new SimpleStringProperty(this, "lawyerName");

    private final StringProperty clientName = new SimpleStringProperty(this, "clientName");

    private final StringProperty status = new SimpleStringProperty(this, "status");



    public Appointment() {}



    public Appointment(int id, LocalDate date, LocalTime time, String lawyerName, String clientName, String status) {
        this.id.set(id);
        this.date.set(date);
        this.time.set(time);
        this.lawyerName.set(lawyerName);
        this.clientName.set(clientName);
        this.status.set(status);
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



    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(String dateStr) {
        try {
            this.date.set(LocalDate.parse(dateStr));
        } catch (DateTimeParseException e) {
            this.date.set(null);
        }
    }



    public LocalTime getTime() {
        return time.get();
    }

    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public void setTime(String timeStr) {
        try {
            this.time.set(LocalTime.parse(timeStr));
        } catch (DateTimeParseException e) {
            this.time.set(null);
        }
    }



    public String getLawyerName() {
        return lawyerName.get();
    }

    public void setLawyerName(String lawyerName) {
        this.lawyerName.set(lawyerName);
    }

    public StringProperty lawyerNameProperty() {
        return lawyerName;
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



    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }



    public String getFormattedDate() {
        return date.get() != null ? date.get().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) : "";
    }

    public String getFormattedTime() {
        return time.get() != null ? time.get().format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }
}
