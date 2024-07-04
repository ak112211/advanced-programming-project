package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class MessagingController {

    @FXML
    private ListView<String> messageListView;
    @FXML
    private TextField messageInputField;
    private User currentUser;
    private String currentChatUser;

    @FXML
    public void initialize() {
        currentUser = User.getCurrentUser();
        loadMessages();
        new Thread(new IncomingMessageHandler()).start();
    }

    @FXML
    private void handleSendButtonAction() {
        String message = messageInputField.getText().trim();
        if (!message.isEmpty() && currentChatUser != null) {
            try {
                DatabaseConnection.saveMessage(currentUser.getUsername(), currentChatUser, message);
                messageInputField.clear();
                loadMessages();
                App.getServerConnection().sendMessage(currentChatUser + ":send message:" + message);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadMessages() {
        if (currentChatUser != null) {
            try {
                List<String> messages = DatabaseConnection.getMessagesBetweenUsers(currentUser.getUsername(), currentChatUser);
                messageListView.getItems().setAll(messages);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCurrentChatUser(String username) {
        this.currentChatUser = username;
        loadMessages();
    }

    private class IncomingMessageHandler implements Runnable {
        @Override
        public void run() {
            try {
                String incomingMessage;
                BufferedReader in = new BufferedReader(new InputStreamReader(App.getServerConnection().getSocket().getInputStream()));
                while ((incomingMessage = in.readLine()) != null) {
                    System.out.println("hello");
                    handleServerEvent(incomingMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleServerEvent(String input) {
            Platform.runLater(() -> {
                System.out.println("Server Event Received: " + input);
                if (input.startsWith("Message from ")) {
                    loadMessages();
                }
            });
        }
    }

}
