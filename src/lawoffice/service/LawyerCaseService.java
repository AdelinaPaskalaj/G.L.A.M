package lawoffice.service;

import lawoffice.model.Case;
import lawoffice.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LawyerCaseService {

    // Gets all cases assigned to a specific lawyer
	public List<Case> getCasesForLawyer(int lawyerId) {
	    List<Case> cases = new ArrayList<>();
	    String sql = "SELECT case_id, case_type, status, details FROM cases WHERE lawyer_id = ?";

	    try (Connection conn = DBUtil.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, lawyerId);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            Case c = new Case();
	            c.setId(rs.getInt("case_id"));
	            c.setTitle(rs.getString("case_type"));
	            c.setStatus(rs.getString("status"));
	            c.setDescription(rs.getString("details"));
	            cases.add(c);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return cases;
	}

    // Updates the status of a case
    public void updateCaseStatus(int caseId, String newStatus) {
        String sql = "UPDATE cases SET status = ? WHERE case_id = ?";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, caseId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Updates the description of a case
    public void updateCaseDescription(int caseId, String newDescription) {
        String sql = "UPDATE cases SET details = ? WHERE case_id = ?";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newDescription);
            stmt.setInt(2, caseId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
