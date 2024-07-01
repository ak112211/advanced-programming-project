package util;

import enums.Menu;
import model.App;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class ServerConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5555;

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

    public void sendMessage(String targetClientId, String message) {
        out.println(targetClientId + ":" + message);
    }

    public void setLogin(String userID) {
        out.println(userID);
    }

    private class IncomingMessageHandler implements Runnable {
        @Override
        public void run() {
            try {
                String incomingMessage;
                while ((incomingMessage = in.readLine()) != null) {
                    if (incomingMessage.endsWith("sent friend request")
                            || incomingMessage.endsWith("sent game request")
                            || incomingMessage.startsWith("accepted friend request")
                            || incomingMessage.startsWith("accepted game request")) {
                        showAlert(incomingMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void showAlert(String message) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, message, "Message from Server", JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

    public BufferedReader getIn() {
        return in;
    }
}
