package util;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static final String SERVER_ADDRESS = "37.152.188.83";
    private static final int SERVER_PORT = 5555;
    private final List<ServerEventListener> listeners = new ArrayList<>();

    public ServerConnection() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Start a thread to listen for incoming messages
            new Thread(new IncomingMessageHandler()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void addMessageListener(ServerEventListener listener) {
        listeners.add(listener);
    }

    public void removeMessageListener(ServerEventListener listener) {
        listeners.remove(listener);
    }

    public BufferedReader getIn() {
        return in;
    }

    public Socket getSocket() {
        return socket;
    }

    private class IncomingMessageHandler implements Runnable {
        @Override
        public void run() {
            try {
                String incomingMessage;
                while ((incomingMessage = in.readLine()) != null) {
                    notifyListeners(incomingMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void notifyListeners(String message) {
            for (ServerEventListener listener : listeners) {
                listener.handleServerEvent(message);
            }
        }
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface ServerEventListener {
        void handleServerEvent(String message);
    }
}
