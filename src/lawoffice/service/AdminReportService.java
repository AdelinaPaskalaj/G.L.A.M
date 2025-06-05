package lawoffice.service;

import lawoffice.util.DBUtil;

import java.sql.*;

public class AdminReportService {

    public String generate(String type) {
        return switch (type) {
            case "All Users" -> buildReport("SELECT full_name, email, role FROM users ORDER BY role");
            case "All Cases" -> buildReport("SELECT case_id, case_type, status FROM cases ORDER BY status");
            case "All Invoices" -> buildReport("SELECT invoice_id, amount, status FROM invoices ORDER BY status");
            case "Appointments Overview" -> buildReport("SELECT id, appointment_date, status FROM appointments ORDER BY appointment_date DESC");
            default -> "Report type not supported.";
        };
    }

    private String buildReport(String query) {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();

            // Header
            for (int i = 1; i <= cols; i++) {
                sb.append(meta.getColumnLabel(i)).append("\t\t");
            }
            sb.append("\n").append("-".repeat(60)).append("\n");

            // Rows
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    sb.append(rs.getString(i)).append("\t\t");
                }
                sb.append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error generating report.";
        }

        return sb.toString();
    }
}
