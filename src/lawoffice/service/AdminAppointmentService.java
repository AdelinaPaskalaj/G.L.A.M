package lawoffice.service;

import lawoffice.model.Appointment;
import lawoffice.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AdminAppointmentService {

    public List<Appointment> getRecentAppointments() {
        List<Appointment> list = new ArrayList<>();

        String sql = """
            SELECT a.id, a.appointment_date, a.status,
                   u1.full_name AS client_name,
                   u2.full_name AS lawyer_name
            FROM appointments a
            JOIN clients c ON a.client_id = c.client_id
            JOIN users u1 ON c.user_id = u1.user_id
            JOIN lawyers l ON a.lawyer_id = l.lawyer_id
            JOIN users u2 ON l.user_id = u2.user_id
            ORDER BY a.appointment_date DESC
            LIMIT 10
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
                    appt.setDate(timestamp.toLocalDateTime().toLocalDate());
                    appt.setTime(timestamp.toLocalDateTime().toLocalTime());
                }

                appt.setStatus(rs.getString("status"));
                list.add(appt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
