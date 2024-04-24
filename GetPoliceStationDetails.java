import java.sql.*;

public class GetPoliceStationDetails {
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "sql@1234";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: PoliceStationDetails <station_id>");
            return;
        }

        int stationId = Integer.parseInt(args[0]);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT name, address FROM police_station WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, stationId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                System.out.println(name);
                System.out.println(address);
            } else {
                System.out.println("Police station not found with ID: " + stationId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
