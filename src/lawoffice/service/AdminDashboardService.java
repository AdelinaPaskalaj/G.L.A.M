package lawoffice.service;

import lawoffice.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboardService {

    public int countAllUsers() {
        return count("SELECT COUNT(*) FROM users");
    }

    public int countOpenCases() {
        return count("SELECT COUNT(*) FROM cases WHERE status IN ('Pending', 'Assigned', 'In Progress')");
    }


    public int countSchedules() {
        return count("SELECT COUNT(*) FROM appointments WHERE appointment_date >= CURDATE()");
    }


    public int countAllInvoices() {
        return count("SELECT COUNT(*) FROM invoices"); 
    }

    private int count(String sql) {
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
}
