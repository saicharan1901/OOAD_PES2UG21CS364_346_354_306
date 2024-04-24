import java.sql.*;

public class PoliceLogin {
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "sql@1234";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: PoliceLogin <admin_name> <password>");
            return;
        }

        String adminName = args[0];
        String password = args[1];

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT id FROM police_station WHERE admin_name = ? AND password = ?")) {
            stmt.setString(1, adminName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int policeStationId = rs.getInt("id");
                System.out.println("Login successful!");
                System.out.println("Police Station ID: " + policeStationId);
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
