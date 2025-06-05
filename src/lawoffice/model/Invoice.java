package lawoffice.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Invoice {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> dueDate = new SimpleObjectProperty<>();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty clientName = new SimpleStringProperty();
    private final StringProperty caseTitle = new SimpleStringProperty();

    // ID
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Due Date
    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }

    public void setDueDateFromLocalDate(LocalDate date) {
        this.dueDate.set(date);
    }

    // Amount
    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    // Status
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    // Client Name
    public String getClientName() {
        return clientName.get();
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }

    public StringProperty clientNameProperty() {
        return clientName;
    }

    // Case Title
    public String getCaseTitle() {
        return caseTitle.get();
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle.set(caseTitle);
    }

    public StringProperty caseTitleProperty() {
        return caseTitle;
    }
}
