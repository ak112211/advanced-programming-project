package server;

import model.Game;
import model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
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

            try {
                switch (command) {
                    case "register":
                        if (parts.length == 5) {
                            String username = parts[1];
                            String nickname = parts[2];
                            String email = parts[3];
                            String password = parts[4];
                            if (DatabaseConnection.isUsernameTaken(username)) {
                                return "Username is already taken.";
                            }
                            DatabaseConnection.saveUser(new User(username, nickname, email, password));
                            return "User registered successfully.";
                        }
                        break;
                    case "login":
                        if (parts.length == 3) {
                            String username = parts[1];
                            String password = parts[2];
                            if (DatabaseConnection.checkPassword(username, password)) {
                                return "Login successful.";
                            } else {
                                return "Invalid username or password.";
                            }
                        }
                        break;
                    case "updatePassword":
                        if (parts.length == 3) {
                            String username = parts[1];
                            String newPassword = parts[2];
                            DatabaseConnection.updatePassword(username, newPassword);
                            return "Password updated successfully.";
                        }
                        break;
                    case "updateProfile":
                        if (parts.length == 5) {
                            String currentUsername = parts[1];
                            String newUsername = parts[2];
                            String nickname = parts[3];
                            String email = parts[4];
                            DatabaseConnection.updateUserProfile(currentUsername, newUsername, nickname, email);
                            return "Profile updated successfully.";
                        }
                        break;
                    case "securityQuestion":
                        if (parts.length == 2) {
                            String username = parts[1];
                            String question = DatabaseConnection.getSecurityQuestion(username);
                            if (question != null) {
                                return "Security question: " + question;
                            } else {
                                return "Username not found.";
                            }
                        }
                        break;
                    case "validateAnswer":
                        if (parts.length == 3) {
                            String username = parts[1];
                            String answer = parts[2];
                            if (DatabaseConnection.validateSecurityAnswer(username, answer)) {
                                return "Security answer validated.";
                            } else {
                                return "Invalid security answer.";
                            }
                        }
                        break;
                    case "stats":
                        if (parts.length == 2) {
                            String username = parts[1];
                            int draws = DatabaseConnection.getDrawsCount(username);
                            int wins = DatabaseConnection.getWinsCount(username);
                            int losses = DatabaseConnection.getLossesCount(username);
                            return String.format("Draws: %d, Wins: %d, Losses: %d", draws, wins, losses);
                        }
                        break;
                    case "recentGames":
                        if (parts.length == 3) {
                            String username = parts[1];
                            int n = Integer.parseInt(parts[2]);
                            List<String> recentGames = DatabaseConnection.getRecentGames(username, n);
                            return String.join("\n", recentGames);
                        }
                        break;
                    case "saveGame":
                        if (parts.length == 2) {
                            Game game = deserializeGame(parts[1]);
                            DatabaseConnection.saveGame(game);
                            return "Game saved successfully.";
                        }
                        break;
                    case "getGame":
                        if (parts.length == 2) {
                            int gameId = Integer.parseInt(parts[1]);
                            Game game = DatabaseConnection.getGame(gameId);
                            return serializeGame(game);
                        }
                        break;
                    default:
                        return "Unknown command.";
                }
            } catch (SQLException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return "Database error: " + e.getMessage();
            }

            return "Invalid request format.";
        }

        private Game deserializeGame(String serializedGame) throws IOException, ClassNotFoundException {
            ByteArrayInputStream bis = new ByteArrayInputStream(serializedGame.getBytes());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Game) ois.readObject();
        }

        private String serializeGame(Game game) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(game);
            oos.flush();
            return bos.toString();
        }
    }
}
