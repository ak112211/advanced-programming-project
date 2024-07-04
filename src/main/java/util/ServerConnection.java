package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ServerConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static final String SERVER_ADDRESS = "37.152.188.83";
    private static final int SERVER_PORT = 5555;
    private final List<Consumer<String>> listeners = new ArrayList<>();

    public ServerConnection() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void addMessageListener(Consumer<String> listener) {
        listeners.clear();
        listeners.add(listener);
    }

    public void removeMessageListener(Consumer<String> listener) {
        listeners.remove(listener);
    }

    private void listenForMessages() {
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
        for (Consumer<String> listener : listeners) {
            listener.accept(message);
        }
    }

    public BufferedReader getIn() {
        return in;
    }

    public Socket getSocket() {
        return socket;
    }
}
