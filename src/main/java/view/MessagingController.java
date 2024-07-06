package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;
import util.ServerConnection;

import java.sql.SQLException;
import java.util.List;

public class MessagingController implements ServerConnection.ServerEventListener {

    @FXML
    private ListView<String> messageListView;
    @FXML
    private TextField messageInputField;
    private User currentUser;
    private String currentChatUser;
    private ServerConnection serverConnection;

    public MessagingController() {
        this.serverConnection = App.getServerConnection();
        this.serverConnection.addMessageListener(this);
    }

    @FXML
    public void initialize() {
        currentUser = User.getCurrentUser();
        loadMessages();
    }

    @FXML
    private void handleSendButtonAction() {
        String message = messageInputField.getText().trim();
        if (!message.isEmpty() && currentChatUser != null) {
            try {
                DatabaseConnection.saveMessage(currentUser.getUsername(), currentChatUser, message);
                messageInputField.clear();
                loadMessages();
                serverConnection.sendMessage(currentChatUser + ":send message:" + message);
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
                if (!messages.isEmpty()) {
                    messageListView.scrollTo(messages.size() - 1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCurrentChatUser(String username) {
        this.currentChatUser = username;
        loadMessages();
    }

    @Override
    public void handleServerEvent(String input) {
        Platform.runLater(() -> {
            if (input.startsWith("Message from " + currentChatUser)) {
                loadMessages();
            }
        });
    }

    public void cleanup() {
        App.getServerConnection().removeMessageListener(this);
    }
}
