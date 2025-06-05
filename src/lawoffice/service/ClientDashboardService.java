package lawoffice.service;

import lawoffice.model.Appointment;
import lawoffice.model.Case;
import lawoffice.model.Invoice;
import lawoffice.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientDashboardService {

    public int getTotalCases(int clientId) {
        String sql = "SELECT COUNT(*) FROM cases WHERE client_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getActiveCases(int clientId) {
        String sql = "SELECT COUNT(*) FROM cases WHERE client_id = ? AND status = 'Active'";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getPendingInvoices(int clientId) {
        String sql = "SELECT COUNT(*) FROM invoices WHERE client_id = ? AND status = 'Pending'";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getUpcomingAppointments(int clientId) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE client_id = ? AND appointment_date >= CURDATE()";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getRecentActivities(int clientId) {
        List<String> activities = new ArrayList<>();
        String sql = "SELECT description FROM activities WHERE client_id = ? ORDER BY activity_date DESC LIMIT 5";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                activities.add(rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }

    public List<Appointment> getUpcomingAppointmentsDetails(int clientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.appointment_date, u.full_name AS lawyerName, a.status FROM appointments a JOIN lawyers l ON a.lawyer_id = l.lawyer_id JOIN users u ON l.user_id = u.user_id WHERE a.client_id = ? AND a.appointment_date >= CURDATE() ORDER BY a.appointment_date";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appt = new Appointment();
                Timestamp ts = rs.getTimestamp("appointment_date");
                if (ts != null) {
                    appt.setDate(ts.toLocalDateTime().toLocalDate());
                    appt.setTime(ts.toLocalDateTime().toLocalTime());
                }
                appt.setLawyerName(rs.getString("lawyerName"));
                appt.setStatus(rs.getString("status"));
                appointments.add(appt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public List<Invoice> getInvoicesByClientId(int clientId) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = """
            SELECT i.invoice_id, i.amount, i.due_date, i.status,
                   c.case_type AS case_title
            FROM invoices i
            JOIN cases c ON i.case_id = c.case_id
            WHERE i.client_id = ?
            ORDER BY i.due_date DESC
        """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice inv = new Invoice();
                inv.setId(rs.getInt("invoice_id"));
                inv.setAmount(rs.getDouble("amount"));
                Date due = rs.getDate("due_date");
                if (due != null) {
                    inv.setDueDateFromLocalDate(due.toLocalDate());
                }
                inv.setStatus(rs.getString("status"));
                inv.setCaseTitle(rs.getString("case_title"));
                invoices.add(inv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }


    public List<String> getCaseHistory(int caseId) {
        List<String> history = new ArrayList<>();
        String sql = "SELECT notes FROM hearings WHERE case_id = ? AND notes IS NOT NULL";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, caseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                history.add(rs.getString("notes"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public List<Case> getCasesForClient(int clientId) {
        List<Case> cases = new ArrayList<>();
        String sql = "SELECT case_id, case_type, status, details FROM cases WHERE client_id = ?";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Case c = new Case();
                c.setId(rs.getInt("case_id"));                c.setTitle(rs.getString("case_type"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("details"));
                cases.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cases;
    }
    public List<Case> getCaseHistoryByClientId(int clientId) {
        List<Case> caseHistory = new ArrayList<>();
        String sql = "SELECT case_id, case_type, status, details, start_date FROM cases WHERE client_id = ? ORDER BY start_date DESC";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Case c = new Case();
                c.setId(rs.getInt("case_id"));
                c.setTitle(rs.getString("case_type"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("details"));
                if (rs.getDate("start_date") != null) {
                    c.setStartDate(rs.getDate("start_date").toLocalDate().toString());
                }
                caseHistory.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return caseHistory;
    }
    public int getClientIdByUserId(int userId) {
        String sql = "SELECT client_id FROM clients WHERE user_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("client_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


}
