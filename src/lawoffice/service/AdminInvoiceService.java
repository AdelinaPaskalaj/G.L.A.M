package lawoffice.service;

import lawoffice.model.Invoice;
import lawoffice.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminInvoiceService {

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = """
            SELECT i.invoice_id, i.amount, i.due_date, i.status,
                   u.full_name AS client_name, c.case_type AS case_title
            FROM invoices i
            JOIN clients cl ON i.client_id = cl.client_id
            JOIN users u ON cl.user_id = u.user_id
            JOIN cases c ON i.case_id = c.case_id
            ORDER BY i.due_date DESC
        """;

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Invoice inv = new Invoice();
                inv.setId(rs.getInt("invoice_id"));
                inv.setAmount(rs.getDouble("amount"));
                inv.setDueDate(rs.getDate("due_date").toLocalDate());
                inv.setStatus(rs.getString("status"));
                inv.setClientName(rs.getString("client_name"));
                inv.setCaseTitle(rs.getString("case_title"));
                invoices.add(inv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoices;
    }

    public void updateStatus(int invoiceId, String status) {
        String sql = "UPDATE invoices SET status = ? WHERE invoice_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, invoiceId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteInvoice(int invoiceId) {
        String sql = "DELETE FROM invoices WHERE invoice_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
