package lawoffice.model;

public class CaseRequest {
    private int caseId;
    private String clientName;
    private String caseType;
    private String status;
    private String details;

    public CaseRequest(int caseId, String clientName, String caseType, String status, String details) {
        this.caseId = caseId;
        this.clientName = clientName;
        this.caseType = caseType;
        this.status = status;
        this.details = details;
    }

    public int getCaseId() { return caseId; }
    public String getClientName() { return clientName; }
    public String getCaseType() { return caseType; }
    public String getStatus() { return status; }
    public String getDetails() { return details; }

    public void setCaseId(int caseId) { this.caseId = caseId; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public void setCaseType(String caseType) { this.caseType = caseType; }
    public void setStatus(String status) { this.status = status; }
    public void setDetails(String details) { this.details = details; }
}
