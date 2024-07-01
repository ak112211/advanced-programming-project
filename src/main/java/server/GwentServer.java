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


                        ClientHandler targetClientHandler;
                        synchronized (CLIENTS) {
                            targetClientHandler = CLIENTS.get(targetClientId);
                        }


                        if (command.endsWith("sent friend request")) {
                            targetClientHandler.sendMessage(clientId, "");
                        } else if (command.endsWith("sent game request")) {
                            targetClientHandler.sendMessage(clientId, command);
                        } else if (command.startsWith("accepted friend request from")) {
                            targetClientHandler.sendMessage(clientId, command);
                        } else if (command.startsWith("accepted game request from")) {
                            targetClientHandler.sendMessage(clientId, command);
                        } else {
                            out.println("Target client not found");
                        }

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

        private void sendMessage(String fromClientId, String message) {
            out.println(message);
        }

    }
}
