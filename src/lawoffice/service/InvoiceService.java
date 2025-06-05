package lawoffice.service;

import lawoffice.model.Invoice;
import lawoffice.util.DBUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceService {

    public int countPendingInvoices(int clientId) {
        String query = "SELECT COUNT(*) FROM invoices WHERE client_id = ? AND status = 'Pending'";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Invoice> getInvoicesForClient(int clientId) {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT invoice_id, amount, status, due_date FROM invoices WHERE client_id = ? ORDER BY due_date DESC";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("invoice_id"));
                invoice.setAmount(rs.getDouble("amount"));
                invoice.setStatus(rs.getString("status"));
                Date dueDate = rs.getDate("due_date");
                if (dueDate != null) {
                    invoice.setDueDateFromLocalDate(dueDate.toLocalDate());
                }
                invoices.add(invoice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invoices;
    }
} 
