package lawoffice.service;

import lawoffice.model.Appointment;
import lawoffice.model.Case;
import lawoffice.model.User;
import lawoffice.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LawyerDashboardService {

    public int getLawyerIdByUserId(int userId) {
        String sql = "SELECT lawyer_id FROM lawyers WHERE user_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("lawyer_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int countAssignedCases(int lawyerId) {
        String sql = "SELECT COUNT(*) FROM cases WHERE lawyer_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lawyerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countActiveCases(int lawyerId) {
        String sql = "SELECT COUNT(*) FROM cases WHERE lawyer_id = ? AND status = 'Active'";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lawyerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countUpcomingAppointments(int lawyerId) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE lawyer_id = ? AND appointment_date >= CURDATE()";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lawyerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Case> getAssignedCases(int lawyerId) {
        List<Case> cases = new ArrayList<>();
        String sql = """
            SELECT c.case_id, c.case_type, c.status, c.details, u.full_name AS client_name
            FROM cases c
            JOIN clients cl ON c.client_id = cl.client_id
            JOIN users u ON cl.user_id = u.user_id
            WHERE c.lawyer_id = ?
            """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lawyerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Case c = new Case();
                c.setId(rs.getInt("case_id"));
                c.setType(rs.getString("case_type"));
                c.setStatus(rs.getString("status"));
                c.setDescription(rs.getString("details"));
                c.setClientName(rs.getString("client_name"));
                cases.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cases;
    }

    public List<Appointment> getUpcomingAppointments(int lawyerId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = """
            SELECT a.id, a.appointment_date, a.status, u.full_name AS client_name
            FROM appointments a
            JOIN clients cl ON a.client_id = cl.client_id
            JOIN users u ON cl.user_id = u.user_id
            WHERE a.lawyer_id = ? AND a.appointment_date >= CURDATE()
            ORDER BY a.appointment_date ASC
            """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lawyerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("appointment_date");
                if (ts != null) {
                    Appointment appt = new Appointment();
                    appt.setId(rs.getInt("id"));
                    LocalDateTime dt = ts.toLocalDateTime();
                    appt.setDate(dt.toLocalDate());
                    appt.setTime(dt.toLocalTime());
                    appt.setStatus(rs.getString("status"));
                    appt.setClientName(rs.getString("client_name"));
                    appointments.add(appt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public boolean updateLawyerProfile(User lawyer) {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lawyer.getName());
            stmt.setString(2, lawyer.getEmail());
            stmt.setString(3, lawyer.getPhone());
            stmt.setString(4, lawyer.getAddress());
            stmt.setInt(5, lawyer.getUserId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Appointment> getAppointmentsForLawyer(int lawyerId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = """
            SELECT a.id, a.appointment_date, a.status, u.full_name AS client_name
            FROM appointments a
            JOIN clients cl ON a.client_id = cl.client_id
            JOIN users u ON cl.user_id = u.user_id
            WHERE a.lawyer_id = ?
            ORDER BY a.appointment_date DESC
        """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lawyerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appt = new Appointment();
                appt.setId(rs.getInt("id"));
                Timestamp ts = rs.getTimestamp("appointment_date");
                if (ts != null) {
                    appt.setDate(ts.toLocalDateTime().toLocalDate());
                    appt.setTime(ts.toLocalDateTime().toLocalTime());
                }
                appt.setStatus(rs.getString("status"));
                appt.setClientName(rs.getString("client_name"));
                appointments.add(appt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public List<Appointment> getAppointmentsByDate(int lawyerId, LocalDate selectedDate) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.appointment_date, a.status, a.notes, u.full_name AS clientName " +
                     "FROM appointments a " +
                     "JOIN clients c ON a.client_id = c.client_id " +
                     "JOIN users u ON c.user_id = u.user_id " +
                     "WHERE a.lawyer_id = ? AND DATE(a.appointment_date) = ? " +
                     "ORDER BY a.appointment_date";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lawyerId);
            stmt.setDate(2, java.sql.Date.valueOf(selectedDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appt = new Appointment();

                Timestamp ts = rs.getTimestamp("appointment_date");
                if (ts != null) {
                    appt.setDate(ts.toLocalDateTime().toLocalDate());
                    appt.setTime(ts.toLocalDateTime().toLocalTime());
                }

                appt.setStatus(rs.getString("status"));
                appt.setClientName(rs.getString("clientName"));

                appointments.add(appt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }
    public int getTotalCases(int lawyerId) {
        String sql = "SELECT COUNT(*) FROM cases WHERE lawyer_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lawyerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getActiveCases(int lawyerId) {
        String sql = "SELECT COUNT(*) FROM cases WHERE lawyerId = ? AND status = 'Active'";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lawyerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public boolean updateCaseStatus(int caseId, String newStatus) {
        String sql = "UPDATE cases SET status = ? WHERE case_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, caseId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
