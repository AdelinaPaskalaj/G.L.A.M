package lawoffice.dao;

import lawoffice.model.Appointment;
import lawoffice.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public List<Appointment> getUpcomingAppointmentsByClientId(int clientId) {
        List<Appointment> appointments = new ArrayList<>();

        String sql = "SELECT a.id, a.date, a.time, u.name as lawyerName, a.status " +
                     "FROM appointments a " +
                     "JOIN users u ON a.lawyer_id = u.id " +
                     "WHERE a.client_id = ? AND a.date >= CURDATE() " +
                     "ORDER BY a.date, a.time";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setDate(rs.getDate("date").toLocalDate());    // LocalDate type
                    appointment.setTime(rs.getTime("time").toLocalTime());    // LocalTime type
                    appointment.setLawyerName(rs.getString("lawyerName"));
                    appointment.setStatus(rs.getString("status"));

                    appointments.add(appointment);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }
}