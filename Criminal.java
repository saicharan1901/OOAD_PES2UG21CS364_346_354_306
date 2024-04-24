import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.sql.*;

public class Criminal {
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "sql@1234";

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver"); // Load JDBC driver explicitly
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/criminal-registration", new CriminalRegistrationHandler());
        server.start();
        System.out.println("Server started on port 8000");
    }

    static class CriminalRegistrationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = getCriminalRegistrationOptions(exchange.getRequestURI().getPath());
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String getCriminalRegistrationOptions(String path) {
            StringBuilder options = new StringBuilder();
            String[] parts = path.split("/");
            if (parts.length == 3) {
                int policeStationId = Integer.parseInt(parts[2]);
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                     PreparedStatement stmt = conn.prepareStatement("SELECT id, crime_type FROM crimes WHERE police_station_id = ?");
                ) {
                    stmt.setInt(1, policeStationId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String crimeType = rs.getString("crime_type");
                            options.append("<option value=\"").append(id).append("\">").append(crimeType).append("</option>");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Invalid URL path");
            }
            return options.toString();
        }
    }
}
