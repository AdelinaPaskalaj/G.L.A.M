package lawoffice.service;

import lawoffice.model.Case;
import lawoffice.model.User;
import lawoffice.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminCaseService {

    // Fetches all pending self-registered cases
    public List<Case> getPendingCases() {
        List<Case> list = new ArrayList<>();
        String sql = """
            SELECT c.case_id AS id, c.case_type AS title, c.status, c.details AS description,
                   u.full_name AS client_name, c.start_date, c.appointment_time
            FROM cases c
            JOIN clients cl ON c.client_id = cl.client_id
            JOIN users u ON cl.user_id = u.user_id
            WHERE c.status = 'Pending' AND c.is_self_registered = TRUE
        """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Case c = new Case();
                c.setId(rs.getInt("id"));
                c.setClientName(rs.getString("client_name"));
                c.setTitle(rs.getString("title"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("description"));
                c.setStartDate(rs.getDate("start_date") != null ? rs.getDate("start_date").toString() : "");
                c.setAppointmentTime(rs.getTime("appointment_time") != null ? rs.getTime("appointment_time").toString() : "");
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Gets all active lawyers from users table
    public List<User> getAllLawyers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, full_name FROM users WHERE role = 'Lawyer' AND status = 'Active'";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new User(rs.getInt("user_id"), rs.getString("full_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Assigns a lawyer and inserts corresponding appointment
    public void assignLawyerUsingCaseData(int caseId, int lawyerUserId) {
        String updateCaseSql = "UPDATE cases SET lawyer_id = ?, status = 'Assigned' WHERE case_id = ?";
        String insertAppointmentSql = """
            INSERT INTO appointments (case_id, lawyer_id, client_id, appointment_date, status)
            SELECT c.case_id, l.lawyer_id, c.client_id,
                   TIMESTAMP(c.start_date, c.appointment_time), 'pending'
            FROM cases c
            JOIN lawyers l ON l.user_id = ?
            WHERE c.case_id = ?
        """;

        try (Connection conn = DBUtil.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (
                PreparedStatement updateStmt = conn.prepareStatement(updateCaseSql);
                PreparedStatement insertStmt = conn.prepareStatement(insertAppointmentSql)
            ) {
                // Update case with assigned lawyer
                updateStmt.setInt(1, lawyerUserId);
                updateStmt.setInt(2, caseId);
                updateStmt.executeUpdate();

                // Insert into appointments table
                insertStmt.setInt(1, lawyerUserId);
                insertStmt.setInt(2, caseId);
                insertStmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Updates the case status
    public void updateStatus(int caseId, String status) {
        String sql = "UPDATE cases SET status = ? WHERE case_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, caseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
