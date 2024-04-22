import java.sql.*;

public class RegisterCriminal {
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "sql@1234";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: RegisterCriminal <name> <crime_id>");
            return;
        }

        String name = args[0];
        int crimeId = Integer.parseInt(args[1]);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO criminals (name, crime_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, crimeId);
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Criminal registered successfully!");
            } else {
                System.out.println("Failed to register criminal.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
