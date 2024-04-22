import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLCrimeConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "sql@1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<PoliceStation> getPoliceStations() throws SQLException {
        List<PoliceStation> policeStations = new ArrayList<>();
        String sql = "SELECT id, name FROM police_station";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                policeStations.add(new PoliceStation(id, name));
            }
        }
        return policeStations;
    }

    public static void insertCrime(int policeStationId, String crimeType, String description, String criminalDescription) throws SQLException {
        String sql = "INSERT INTO crimes (police_station_id, crime_type, description, criminal_description) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, policeStationId);
            pstmt.setString(2, crimeType);
            pstmt.setString(3, description);
            pstmt.setString(4, criminalDescription);

            pstmt.executeUpdate();
        }
    }

    static class PoliceStation {
        private int id;
        private String name;

        public PoliceStation(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
