package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.App;
import util.DatabaseConnection;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MessagingController {

    @FXML
    private ListView<String> messageListView;

    @FXML
    private TextField messageInputField;

    @FXML
    private Button sendButton;

    private User currentUser;
    private String currentChatUser;

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
                App.getServerConnection().sendMessage(currentChatUser, "message from " + currentUser.getUsername());
                String input;
                while ((input = App.getServerConnection().getIn().readLine()) != null) {
                    if (input.endsWith("message from " + currentChatUser)) {
                        loadMessages();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
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
}
