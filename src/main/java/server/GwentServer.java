package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class GwentServer {
    private static final Map<String, ClientHandler> CLIENTS = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            System.out.println("Server is listening on port 5555");
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientId;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                clientId = in.readLine();
                synchronized (CLIENTS) {
                    CLIENTS.put(clientId, this);
                }

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String[] parts = inputLine.split(":", 3);
                    if (parts.length == 2) {
                        String targetClientId = parts[0];
                        String command = parts[1];

                        if (command.equals("logout")) {
                            handleLogout();
                        } else {
                            handleCommand(targetClientId, command);
                        }
                    } else if (parts.length == 3) {
                        String fromClientId = parts[0];
                        String targetClientId = parts[1];
                        String command = parts[2];
                        handleCommand(fromClientId, targetClientId, command);
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
                synchronized (CLIENTS) {
                    CLIENTS.remove(clientId);
                }
            }
        }

        private void handleLogout() {
            synchronized (CLIENTS) {
                CLIENTS.remove(clientId);
            }
            clientId = null;
            out.println("Logged out. You can log in as a different user.");
        }

        private void handleCommand(String fromClientId, String targetClientId, String command) {
            ClientHandler targetClientHandler;
            synchronized (CLIENTS) {
                targetClientHandler = CLIENTS.get(targetClientId);
            }
            if (targetClientHandler != null) {
                targetClientHandler.sendMessage(fromClientId, command);
            } else {
                out.println("Target client not found");
            }
        }

        private void handleCommand(String targetClientId, String command) {
            ClientHandler targetClientHandler;
            synchronized (CLIENTS) {
                targetClientHandler = CLIENTS.get(targetClientId);
            }
            if (targetClientHandler != null) {
                if (command.endsWith("sent friend request")) {
                    targetClientHandler.sendMessage(clientId, "sent friend request");
                } else if (command.endsWith("sent game request")) {
                    targetClientHandler.sendMessage(clientId, "sent game request");
                } else if (command.startsWith("accepted friend request from")) {
                    targetClientHandler.sendMessage(clientId, "accepted friend request from");
                } else if (command.startsWith("accepted game request from")) {
                    targetClientHandler.sendMessage(clientId, "accepted game request from");
                } else if (command.endsWith("played move")) {
                    targetClientHandler.sendMessage(clientId, "played move");
                } else {
                    out.println("Unknown command");
                }
            } else {
                out.println("Target client not found");
            }
        }

        private void sendMessage(String fromClientId, String message) {
            out.println(fromClientId + ": " + message);
        }
    }
}
