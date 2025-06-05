package lawoffice.service;

import lawoffice.model.User;
import lawoffice.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminUserService {

    // Fetches all users from the database
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT user_id, personal_id, full_name, email, phone, address, password, role, status FROM users";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("personal_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("status")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }

        return users;
    }

    // Sets the user's status to Active/Inactive
    public boolean setUserStatus(int userId, String status) {
        String query = "UPDATE users SET status = ? WHERE user_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Deletes a user from both users and clients tables
    public void deleteUser(int userId) {
        String deleteClientSQL = "DELETE FROM clients WHERE user_id = ?";
        String deleteUserSQL = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBUtil.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteClientStmt = conn.prepareStatement(deleteClientSQL);
                 PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserSQL)) {

                deleteClientStmt.setInt(1, userId);
                deleteClientStmt.executeUpdate();

                deleteUserStmt.setInt(1, userId);
                deleteUserStmt.executeUpdate();

                conn.commit();

            } catch (SQLException ex) {
                conn.rollback();
                System.err.println("Error deleting user: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Database connection error during delete: " + e.getMessage());
        }
    }

	public void updateUserProfile(User user) {
    String sql = "UPDATE users SET full_name = ?, email = ?, phone = ?, address = ?" +
                 (user.getPassword() != null ? ", password = ?" : "") +
                 " WHERE user_id = ?";
    try (Connection conn = DBUtil.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, user.getName());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPhone());
        stmt.setString(4, user.getAddress());

        int paramIndex = 5;
        if (user.getPassword() != null) {
            stmt.setString(paramIndex++, user.getPassword());
        }

        stmt.setInt(paramIndex, user.getUserId());
        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
