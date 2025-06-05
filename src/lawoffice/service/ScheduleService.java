package lawoffice.service;

import lawoffice.model.Appointment;
import lawoffice.util.DBUtil;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleService {

    private final DBUtil dbUtil = DBUtil.getInstance();

    public List<Appointment> getAppointmentsForClient(int clientId) {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT id, date, time, lawyer_name, status FROM appointments WHERE client_id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("id"));
                appointment.setDate(LocalDate.parse(rs.getString("date")));
                appointment.setTime(LocalTime.parse(rs.getString("time")));
                appointment.setLawyerName(rs.getString("lawyer_name"));
                appointment.setStatus(rs.getString("status"));
                list.add(appointment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}