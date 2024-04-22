import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLConnector {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/cms";
        String username = "root";
        String userPassword = "sql@1234";

        // Load the MySQL Connector/J driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load MySQL Connector/J driver.");
            e.printStackTrace();
            return;
        }

        try (Connection connection = DriverManager.getConnection(url, username, userPassword)) {
            String name = args[0]; // Read name from command-line arguments
            String userPasswordArg = args[1]; // Read password from command-line arguments

            String sql = "INSERT INTO trial (name, password) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setString(2, userPasswordArg);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new user was inserted successfully!");
                } else {
                    System.out.println("Failed to insert user.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
}
