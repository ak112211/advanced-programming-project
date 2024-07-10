package view;

import enums.DefaultMessages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
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
    @FXML
    private HBox defaultMessagesHBox;
    @FXML
    private HBox emojiButtonsHBox;
    @FXML
    private Label replyContextLabel;

    private User currentUser;
    private String currentChatUser;
    private String replyContextMessage;
    private ServerConnection serverConnection;

    public MessagingController() {
        this.serverConnection = App.getServerConnection();
        this.serverConnection.addMessageListener(this);
    }

    @FXML
    public void initialize() {
        currentUser = User.getCurrentUser();
        loadMessages();
        initializeDefaultMessages();
        initializeEmojiButtons();
        setupMessageListClickListener();
    }

    @FXML
    private void handleSendButtonAction() {
        sendMessage(messageInputField.getText().trim());
    }

    private void sendMessage(String message) {
        if (!message.isEmpty() && currentChatUser != null) {
            try {
                String fullMessage = message;
                if (replyContextMessage != null) {
                    fullMessage = "Reply to: " + replyContextMessage + "\n" + message;
                }
                DatabaseConnection.saveMessage(currentUser.getUsername(), currentChatUser, fullMessage);
                messageInputField.clear();
                replyContextMessage = null;
                replyContextLabel.setVisible(false);
                loadMessages();
                serverConnection.sendMessage(currentChatUser + ":send message:" + fullMessage);
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

    private void initializeDefaultMessages() {
        for (DefaultMessages defaultMessage : DefaultMessages.values()) {
            Button button = new Button(defaultMessage.getMessage());
            button.setOnAction(e -> sendMessage(defaultMessage.getMessage()));
            defaultMessagesHBox.getChildren().add(button);
        }
    }

    private void initializeEmojiButtons() {
        String[] emojis = {"😊", "😂", "😍", "😢", "👍", "❤️", "🔥", "🎉", "😎", "👏"};
        for (String emoji : emojis) {
            Button button = new Button(emoji);
            button.setOnAction(e -> sendMessage(emoji));
            emojiButtonsHBox.getChildren().add(button);
        }
    }

    private void setupMessageListClickListener() {
        messageListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                String selectedMessage = messageListView.getSelectionModel().getSelectedItem();
                if (selectedMessage != null) {
                    replyContextMessage = selectedMessage;
                    replyContextLabel.setText("Replying to: " + selectedMessage);
                    replyContextLabel.setVisible(true);
                }
            }
        });
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
