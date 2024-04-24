import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetCrimeIDs {
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "sql@1234";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: GetCrimeIDs <police_station_id>");
            return;
        }

        int policeStationId = Integer.parseInt(args[0]);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT id FROM crimes WHERE police_station_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, policeStationId);
            ResultSet resultSet = statement.executeQuery();

            List<Integer> crimeIds = new ArrayList<>();
            while (resultSet.next()) {
                crimeIds.add(resultSet.getInt("id"));
            }

            // Print crime IDs
            for (int crimeId : crimeIds) {
                System.out.println(crimeId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
