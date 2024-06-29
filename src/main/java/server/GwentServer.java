package server;

import util.DatabaseConnection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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
        String[] parts = request.split(" ");
        String command = parts[0];

        try {
            switch (command) {
                case "sendMessage":
                    if (parts.length >= 4) {
                        String sender = parts[1];
                        String recipient = parts[2];
                        String message = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));
                        if (DatabaseConnection.saveMessage(sender, recipient, message)) {
                            return "Message sent";
                        } else {
                            return "Failed to send message";
                        }
                    }
                    break;
                case "getMessages":
                    if (parts.length == 2) {
                        String username = parts[1];
                        List<String> messages = DatabaseConnection.getMessages(username);
                        return String.join("\n", messages);
                    }
                    break;
                case "sendGameRequest":
                    if (parts.length == 3) {
                        String sender = parts[1];
                        String recipient = parts[2];
                        if (DatabaseConnection.saveGameRequest(sender, recipient)) {
                            return "Game request sent";
                        } else {
                            return "Failed to send game request";
                        }
                    }
                    break;
                case "getRequests":
                    if (parts.length == 2) {
                        String username = parts[1];
                        List<String> requests = DatabaseConnection.getGameRequests(username);
                        return String.join("\n", requests);
                    }
                    break;
                default:
                    return "Unknown command.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
        return "Invalid request format.";
    }
}
