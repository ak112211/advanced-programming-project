package server;

import model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GwentServer {
    private static final int PORT = 5555;

    // Command patterns
    private static final Pattern LOGIN_PATTERN = Pattern.compile("login:(\\w+)");
    private static final Pattern REGISTER_PATTERN = Pattern.compile("register:(\\w+)");
    private static final Pattern LOGOUT_PATTERN = Pattern.compile("logout");
    private static final Pattern LOADED_DECK_PATTERN = Pattern.compile("(\\w+):loaded new");
    private static final Pattern LOADED_AFTER_PATTERN = Pattern.compile("(\\w+):loaded after:(\\w+)");
    private static final Pattern SEND_VERIFICATION_PATTERN = Pattern.compile("send verification for:(.+):(.+)");
    private static final Pattern FRIEND_REQUEST_PATTERN = Pattern.compile("(\\w+):send friend request");
    private static final Pattern GAME_REQUEST_PATTERN = Pattern.compile("(\\w+):send game request");
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("(\\w+):send message:(.+)");
    private static final Pattern ACCEPT_FRIEND_REQUEST_PATTERN = Pattern.compile("accepted friend request from:(\\w+)");
    private static final Pattern ACCEPT_GAME_REQUEST_PATTERN = Pattern.compile("accepted game request from:(\\w+)");
    private static final Pattern MOVE_PATTERN = Pattern.compile("(\\w+):other player played move");
    private static final Pattern END_GAME_PATTERN = Pattern.compile("(\\w+):ended game");

    private static final Map<String, ClientHandler> clients = new HashMap<>();
    private static final Map<String, Player> players = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private Player currentPlayer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String input;
                while ((input = in.readLine()) != null) {
                    Matcher matcher = getMatcher(input);
                    System.out.println(input);

                    if (matcher != null) {
                        if (matcher.pattern() == LOGIN_PATTERN) {
                            handleLogin(matcher);
                        } else if (matcher.pattern() == REGISTER_PATTERN) {
                            handleRegister(matcher);
                        } else if (matcher.pattern() == LOGOUT_PATTERN) {
                            handleLogout();
                        } else if (matcher.pattern() == SEND_VERIFICATION_PATTERN) {
                            handleSendVerification(matcher);
                        } else if (matcher.pattern() == FRIEND_REQUEST_PATTERN) {
                            handleFriendRequest(matcher);
                        } else if (matcher.pattern() == GAME_REQUEST_PATTERN) {
                            handleGameRequest(matcher);
                        } else if (matcher.pattern() == MESSAGE_PATTERN) {
                            handleMessage(matcher);
                        } else if (matcher.pattern() == ACCEPT_FRIEND_REQUEST_PATTERN) {
                            handleAcceptFriendRequest(matcher);
                        } else if (matcher.pattern() == ACCEPT_GAME_REQUEST_PATTERN) {
                            handleAcceptGameRequest(matcher);
                        } else if (matcher.pattern() == MOVE_PATTERN) {
                            handleMove(matcher);
                        } else if (matcher.pattern() == END_GAME_PATTERN) {
                            handleEndGame(matcher);
                        } else if (matcher.pattern() == LOADED_DECK_PATTERN) {
                            handleDeckLoaded(matcher);
                        } else if (matcher.pattern() == LOADED_AFTER_PATTERN) {
                            handleDeckLoadedAfter(matcher);
                        }
                    } else {
                        out.println("Invalid command");
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                synchronized (clients) {
                    if (currentPlayer != null) {
                        clients.remove(currentPlayer.getId());
                    }
                }
            }
        }

        private Matcher getMatcher(String input) {
            Matcher matcher = LOGIN_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = REGISTER_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = LOGOUT_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = SEND_VERIFICATION_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = FRIEND_REQUEST_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = LOADED_DECK_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = LOADED_AFTER_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = GAME_REQUEST_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = MESSAGE_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = ACCEPT_FRIEND_REQUEST_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = ACCEPT_GAME_REQUEST_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = MOVE_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            matcher = END_GAME_PATTERN.matcher(input);
            if (matcher.matches()) return matcher;

            return null;
        }

        private void handleRegister(Matcher matcher) throws IOException {
            String id = matcher.group(1);
            synchronized (players) {
                if (players.containsKey(id)) {
                    out.println("Player with this ID already exists");
                } else {
                    players.put(id, new Player(id));
                    out.println("Player registered successfully");
                }
            }
        }
        
        private void handleLogin(Matcher matcher) throws IOException {
            String id = matcher.group(1);
            synchronized (players) {
             
                currentPlayer = players.get(id);
                synchronized (clients) {
                    clients.put(id, this);
                }
                out.println("Login successful");
               
            }
        }

        private void handleLogout() throws IOException {
            if (currentPlayer != null) {
                synchronized (clients) {
                    clients.remove(currentPlayer.getId());
                }
                currentPlayer = null;
                out.println("Logout successful");
            } else {
                out.println("No player is logged in");
            }
        }

        private void handleSendVerification(Matcher matcher) throws IOException {
            String email = matcher.group(1);
            String code = matcher.group(2);
            EmailSender.sendVerificationEmail(email, code);
            out.println("Verification email sent to " + email);
        }

        private void handleFriendRequest(Matcher matcher) throws IOException {
            String targetId = matcher.group(1);
            sendToClient(targetId, "Friend request from " + currentPlayer.getId());
        }

        private void handleGameRequest(Matcher matcher) throws IOException {
            String targetId = matcher.group(1);
            sendToClient(targetId, "Game request from " + currentPlayer.getId());
        }

        private void handleMessage(Matcher matcher) throws IOException {
            String targetId = matcher.group(1);
            String message = matcher.group(2);
            sendToClient(targetId, "Message from " + currentPlayer.getId() + ": " + message);
        }

        private void handleAcceptFriendRequest(Matcher matcher) throws IOException {
            String fromId = matcher.group(1);
            sendToClient(fromId, "Friend request accepted by " + currentPlayer.getId());
        }

        private void handleAcceptGameRequest(Matcher matcher) throws IOException {
            String fromId = matcher.group(1);
            sendToClient(fromId, "Game request accepted by " + currentPlayer.getId());
        }

        private void handleMove(Matcher matcher) throws IOException {
            String targetId = matcher.group(1);
            sendToClient(targetId, "Move from " + currentPlayer.getId());
        }

        private void handleEndGame(Matcher matcher) throws IOException {
            String targetId = matcher.group(1);
            sendToClient(targetId, "Game ended by " + currentPlayer.getId());
        }

        private void handleDeckLoaded(Matcher matcher) throws IOException {
            String targetId = matcher.group(1);
            sendToClient(targetId, currentPlayer.getId() + " loaded deck new");
        }

        private void handleDeckLoadedAfter(Matcher matcher) throws IOException {
            String targetId = matcher.group(1);
            String gameId = matcher.group(2);
            sendToClient(targetId, currentPlayer.getId() + " loaded deck after with id: " + gameId);
        }

        private void sendToClient(String clientId, String message) throws IOException {
            ClientHandler targetClientHandler;
            synchronized (clients) {
                targetClientHandler = clients.get(clientId);
            }
            if (targetClientHandler != null) {
                targetClientHandler.out.println(message);
            } else {
                out.println("Target client not found");
            }
        }
    }
}

class Player {
    private final String id;
    public Player(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
}
