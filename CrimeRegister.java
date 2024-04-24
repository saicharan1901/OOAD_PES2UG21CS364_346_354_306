import java.sql.*;

public class CrimeRegister {
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "sql@1234";

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: CrimeRegister <police_station_id> <crime_type> <crime_description> <criminal_description>");
            return;
        }

        int policeStationId = Integer.parseInt(args[0]);
        String crimeType = args[1];
        String crimeDescription = args[2];
        String criminalDescription = args[3];

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO crimes (police_station_id, crime_type, description, criminal_description) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, policeStationId);
            stmt.setString(2, crimeType);
            stmt.setString(3, crimeDescription);
            stmt.setString(4, criminalDescription);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Crime registered successfully!");
            } else {
                System.out.println("Failed to register crime!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
