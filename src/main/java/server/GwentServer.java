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
                case "register":
                    return handleRegister(parts);
                case "login":
                    return handleLogin(parts);
                case "updatePassword":
                    return handleUpdatePassword(parts);
                case "updateProfile":
                    return handleUpdateProfile(parts);
                case "securityQuestion":
                    return handleSecurityQuestion(parts);
                case "validateAnswer":
                    return handleValidateAnswer(parts);
                case "stats":
                    return handleStats(parts);
                case "recentGames":
                    return handleRecentGames(parts);
                case "saveGame":
                    return handleSaveGame(parts);
                case "getGame":
                    return handleGetGame(parts);
                case "updateGame":
                    return handleUpdateGame(request);
                default:
                    return "Unknown command.";
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }

    }

    private String handleRegister(String[] parts) throws SQLException, IOException {
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
        return "Invalid request format.";
    }

    private String handleLogin(String[] parts) throws SQLException {
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];
            if (DatabaseConnection.checkPassword(username, password)) {
                return "Login successful.";
            } else {
                return "Invalid username or password.";
            }
        }
        return "Invalid request format.";
    }

    private String handleUpdatePassword(String[] parts) throws SQLException {
        if (parts.length == 3) {
            String username = parts[1];
            String newPassword = parts[2];
            DatabaseConnection.updatePassword(username, newPassword);
            return "Password updated successfully.";
        }
        return "Invalid request format.";
    }

    private String handleUpdateProfile(String[] parts) throws SQLException {
        if (parts.length == 5) {
            String currentUsername = parts[1];
            String newUsername = parts[2];
            String nickname = parts[3];
            String email = parts[4];
            DatabaseConnection.updateUserProfile(currentUsername, newUsername, nickname, email);
            return "Profile updated successfully.";
        }
        return "Invalid request format.";
    }

    private String handleSecurityQuestion(String[] parts) throws SQLException {
        if (parts.length == 2) {
            String username = parts[1];
            String question = DatabaseConnection.getSecurityQuestion(username);
            if (question != null) {
                return "Security question: " + question;
            } else {
                return "Username not found.";
            }
        }
        return "Invalid request format.";
    }

    private String handleValidateAnswer(String[] parts) throws SQLException {
        if (parts.length == 3) {
            String username = parts[1];
            String answer = parts[2];
            if (DatabaseConnection.validateSecurityAnswer(username, answer)) {
                return "Security answer validated.";
            } else {
                return "Invalid security answer.";
            }
        }
        return "Invalid request format.";
    }

    private String handleStats(String[] parts) throws SQLException {
        if (parts.length == 2) {
            String username = parts[1];
            int draws = DatabaseConnection.getDrawsCount(username);
            int wins = DatabaseConnection.getWinsCount(username);
            int losses = DatabaseConnection.getLossesCount(username);
            return String.format("Draws: %d, Wins: %d, Losses: %d", draws, wins, losses);
        }
        return "Invalid request format.";
    }

    private String handleRecentGames(String[] parts) throws SQLException {
        if (parts.length == 3) {
            String username = parts[1];
            int n = Integer.parseInt(parts[2]);
            List<String> recentGames = DatabaseConnection.getRecentGames(username, n);
            return String.join("\n", recentGames);
        }
        return "Invalid request format.";
    }

    private String handleSaveGame(String[] parts) throws SQLException, IOException, ClassNotFoundException {
        if (parts.length == 2) {
            Game game = deserializeGame(parts[1]);
            DatabaseConnection.saveGame(game);
            return "Game saved successfully.";
        }
        return "Invalid request format.";
    }

    private String handleGetGame(String[] parts) throws SQLException, IOException, ClassNotFoundException {
        if (parts.length == 2) {
            int gameId = Integer.parseInt(parts[1]);
            Game game = DatabaseConnection.getGame(gameId);
            return serializeGame(game);
        }
        return "Invalid request format.";
    }

    private String handleUpdateGame(String request) throws IOException, SQLException, ClassNotFoundException {
        // Assume the request is of the format: "updateGame gameData"
        String gameData = request.substring(request.indexOf(" ") + 1);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(gameData.getBytes());
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        Game game = (Game) objIn.readObject();

        // Process the game state
        game.calculatePoints();
        game.nextTurn();

        // Serialize the updated game state and return it to the client
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(game);
        objOut.flush();
        return byteOut.toString();
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

