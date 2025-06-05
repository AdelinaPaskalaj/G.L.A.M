package lawoffice.dao;

import lawoffice.util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO {

	public boolean emailExists(String email) {
	    String where = "email = '" + email.replace("'", "''") + "'";
	    String[] columns = {"1"};
	    try (ResultSet rs = DBUtil.getInstance().readData("clients", columns, where)) {
	        return rs != null && rs.next();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


    public boolean personalIdExists(String personalId) {
        String whereClause = "personal_id = '" + personalId.replace("'", "''") + "'";
        String[] columns = {"1"};
        try (ResultSet rs = DBUtil.getInstance().readData("clients", columns, whereClause)) {
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("existsByPersonalId error: " + e.getMessage());
        }
        return false;
    }

    public boolean insertClient(String name, String email, String personalId, String phone) {
        String tableName = "clients";
        String[] columns = {"name", "email", "personal_id", "phone"};
        Object[] values = {name, email, personalId, phone};
        return DBUtil.getInstance().insertData(tableName, columns, values);
    }
}
