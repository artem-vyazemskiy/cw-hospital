import java.sql.*;

public class JdbcUtils {

    public static Connection getNewConnection(String dbUrl) throws SQLException {
        Connection connection = DriverManager.getConnection(dbUrl);
        if (!connection.isValid(1)) {
            throw new RuntimeException("Connection failed");
        }
        if (!checkDb(connection)) {
            initDb(connection);
        }
        return connection;
    }

    private static boolean checkDb(Connection connection) throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE name = 'PEOPLE'";
        try(PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
        ) {
            return rs.next();
        }
    }

    private static void initDb(Connection connection) throws SQLException {
        String sqlCreateTables[] = {
                "CREATE TABLE DIAGNOSIS (\n" +
                        "    ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                        "    NAME TEXT(50)\n" +
                        ");",
                "CREATE TABLE WARDS (\n" +
                        "    ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                        "    NAME TEXT(50),\n" +
                        "    MAX_COUNT INTEGER\n" +
                        ");",
                "CREATE TABLE PEOPLE (\n" +
                        "    ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                        "    FIRST_NAME TEXT(20),\n" +
                        "    LAST_NAME TEXT(20),\n" +
                        "    PARENT_NAME TEXT(20),\n" +
                        "    DIAGNOSIS_ID INTEGER,\n" +
                        "    WARD_ID INTEGER,\n" +
                        "\n" +
                        "    FOREIGN KEY (DIAGNOSIS_ID) REFERENCES DIAGNOSIS(ID),\n" +
                        "    FOREIGN KEY (WARD_ID) REFERENCES WARDS(ID)\n" +
                        ");"
        };

        for(String sql : sqlCreateTables) {
            try(PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.executeUpdate();
            }
        }
    }

}
