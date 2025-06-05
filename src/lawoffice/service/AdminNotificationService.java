package lawoffice.service;

import lawoffice.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminNotificationService {

    public boolean sendToAllUsers(String message) {
        String sql = "INSERT INTO activities (client_id, description) " +
                     "SELECT c.client_id, ? FROM clients c";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, message);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
