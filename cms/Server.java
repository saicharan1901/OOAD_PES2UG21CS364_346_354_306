import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.sql.*;

public class Server {
    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "sql@1234";

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver"); // Load JDBC driver explicitly
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/dashboard", new DashboardHandler());
        server.start();
        System.out.println("Server started on port 8000");
    }
    
    static class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = getPoliceStations();
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String getPoliceStations() {
            StringBuilder stations = new StringBuilder();
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id, name FROM police_station")) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    stations.append("<option value=\"").append(id).append("\">").append(name).append("</option>");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return stations.toString();
        }
    }
}
