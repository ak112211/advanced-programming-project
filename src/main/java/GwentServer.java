package server;

import util.DatabaseConnection;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.List;

public class GwentServer {

    private static final int PORT = 5555;
    private static Connection connection;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/UserDB", "root", "your_password");
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Gwent server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                new ClientHandler(clientSocket).start();
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
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

            switch (command) {
                default:
                    return "Unknown command.";
            }

        }
    }
}

