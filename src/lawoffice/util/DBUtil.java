package lawoffice.util;

import java.sql.*;

public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/law_office_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static DBUtil instance;

    private DBUtil() {}

    public static DBUtil getInstance() {
        if (instance == null) {
            instance = new DBUtil();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public ResultSet readData(String table, String[] columns, String whereClause) {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(String.join(", ", columns));
        query.append(" FROM ").append(table);
        if (whereClause != null && !whereClause.isEmpty()) {
            query.append(" WHERE ").append(whereClause);
        }

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return stmt.executeQuery(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertData(String table, String[] columns, Object[] values) {
        if (columns.length != values.length) return false;

        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(table).append(" (").append(String.join(", ", columns)).append(") VALUES (");
        query.append("?,".repeat(values.length));
        query.setLength(query.length() - 1);
        query.append(")");

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
