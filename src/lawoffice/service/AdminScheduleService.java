package lawoffice.service;

import lawoffice.model.Appointment;
import lawoffice.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminScheduleService {

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = """
            SELECT a.id, u.full_name AS client_name, u2.full_name AS lawyer_name,
                   a.appointment_date, a.status
            FROM appointments a
            JOIN clients c ON a.client_id = c.client_id
            JOIN users u ON c.user_id = u.user_id
            JOIN lawyers l ON a.lawyer_id = l.lawyer_id
            JOIN users u2 ON l.user_id = u2.user_id
        """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Appointment appt = new Appointment();
                appt.setId(rs.getInt("id"));
                appt.setClientName(rs.getString("client_name"));
                appt.setLawyerName(rs.getString("lawyer_name"));
                Timestamp timestamp = rs.getTimestamp("appointment_date");

                if (timestamp != null) {
                    LocalDateTime dt = timestamp.toLocalDateTime();
                    appt.setDate(dt.toLocalDate());
                    appt.setTime(dt.toLocalTime());
                }

                appt.setStatus(rs.getString("status"));
                list.add(appt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateAppointmentStatus(int appointmentId, String newStatus) {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, appointmentId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
