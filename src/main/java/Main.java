import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        final String dbUrl = "jdbc:sqlite:./hospital_db";

        try (Connection connection = JdbcUtils.getNewConnection(dbUrl)) {


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
