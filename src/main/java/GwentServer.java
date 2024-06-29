
import util.DatabaseConnection;
import model.Game;
import model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class GwentServer {

    private static final int PORT = 5555;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Gwent server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                String response = handleRequest(inputLine);
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String handleRequest(String request) {
        String[] parts = request.split(" ", 2);
        String command = parts[0];

        try {
            switch (command) {
                case "chat":
                    return handleChat(parts[1]);
                case "requestGame":
                    return handleGameRequest(parts[1]);
                case "getMessages":
                    return handleGetMessages(parts[1]);
                case "getRequests":
                    return handleGetRequests(parts[1]);
                default:
                    return "Unknown command.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String handleChat(String data) throws SQLException {
        String[] parts = data.split(" ", 3);
        String sender = parts[0];
        String receiver = parts[1];
        String message = parts[2];
        DatabaseConnection.saveMessage(sender, receiver, message);
        return "Message sent.";
    }

    private String handleGameRequest(String data) throws SQLException {
        String[] parts = data.split(" ", 2);
        String sender = parts[0];
        String receiver = parts[1];
        DatabaseConnection.saveGameRequest(sender, receiver);
        return "Game request sent.";
    }

    private String handleGetMessages(String username) throws SQLException {
        return DatabaseConnection.getMessages(username);
    }

    private String handleGetRequests(String username) throws SQLException {
        return DatabaseConnection.getGameRequests(username);
    }
}
