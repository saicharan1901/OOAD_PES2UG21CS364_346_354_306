import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class GetCrimeInfo {
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "sql@1234";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: GetCrimeInfo <police_station_id>");
            return;
        }

        int policeStationId = Integer.parseInt(args[0]);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT crime_type, description, criminal_description FROM crimes WHERE police_station_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, policeStationId);
            ResultSet resultSet = statement.executeQuery();

            List<String[]> crimeInfoList = new ArrayList<>();
            while (resultSet.next()) {
                String crimeType = resultSet.getString("crime_type");
                String description = resultSet.getString("description");
                String criminalDescription = resultSet.getString("criminal_description");
                String[] crimeInfo = {crimeType, description, criminalDescription};
                crimeInfoList.add(crimeInfo);
            }

            // Convert List to 2D Array
            String[][] crimeInfoArray = new String[crimeInfoList.size()][];
            crimeInfoArray = crimeInfoList.toArray(crimeInfoArray);

            // Print 2D Array
            System.out.println(Arrays.deepToString(crimeInfoArray));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
