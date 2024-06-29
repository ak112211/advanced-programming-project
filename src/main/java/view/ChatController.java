package view;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.User;
import util.DatabaseConnection;
import util.ServerConnection;

import java.sql.SQLException;

public class ChatController {

    @FXML
    private TextField friendUsernameField;
    @FXML
    private ListView<String> friendsListView;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextField messageTextField;
    @FXML
    private ListView<String> messagesListView;
    @FXML
    private ListView<String> gameRequestsListView;

    private User currentUser;
    private ServerConnection serverConnection;

    @FXML
    private void initialize() {
        currentUser = User.getCurrentUser();
        friendsListView.getItems().addAll(currentUser.getFriends());
        serverConnection = new ServerConnection();
        updateMessages();
        updateGameRequests();
    }

    @FXML
    private void handleAddFriend() throws SQLException {
        String friendUsername = friendUsernameField.getText();
        if (friendUsername.isEmpty()) {
            Tools.showAlert("Error", "Input Error", "Please enter a friend's username.");
            return;
        }

        if (DatabaseConnection.getUser(friendUsername) == null) {
            Tools.showAlert("Error", "User Not Found", "The entered friend's username does not exist.");
            return;
        }

        boolean isFriendAdded = DatabaseConnection.addFriend(currentUser.getUsername(), friendUsername);
        if (isFriendAdded) {
            currentUser.addFriend(friendUsername);
            friendsListView.getItems().add(friendUsername);
            Tools.showAlert("Success", "Friend Added", "Friend added successfully.");
        } else {
            Tools.showAlert("Error", "Database Error", "An error occurred while adding the friend. Please try again.");
        }
    }

    @FXML
    private void handleSendMessage() {
        String message = messageTextField.getText();
        if (message.isEmpty()) {
            Tools.showAlert("Error", "Input Error", "Please enter a message.");
            return;
        }

        String recipient = friendsListView.getSelectionModel().getSelectedItem();
        if (recipient == null) {
            Tools.showAlert("Error", "Selection Error", "Please select a friend to send the message to.");
            return;
        }

        String response = serverConnection.sendRequest("sendMessage " + currentUser.getUsername() + " " + recipient + " " + message);
        if (response.equals("Message sent")) {
            chatTextArea.appendText(currentUser.getUsername() + ": " + message + "\n");
            messageTextField.clear();
        } else {
            Tools.showAlert("Error", "Message Error", "An error occurred while sending the message. Please try again.");
        }
    }

    @FXML
    private void handleSendGameRequest() {
        String recipient = friendsListView.getSelectionModel().getSelectedItem();
        if (recipient == null) {
            Tools.showAlert("Error", "Selection Error", "Please select a friend to send the game request to.");
            return;
        }

        String response = serverConnection.sendRequest("sendGameRequest " + currentUser.getUsername() + " " + recipient);
        if (response.equals("Game request sent")) {
            Tools.showAlert("Success", "Request Sent", "Game request sent successfully.");
        } else {
            Tools.showAlert("Error", "Request Error", "An error occurred while sending the game request. Please try again.");
        }
    }

    @FXML
    private void updateMessages() {
        messagesListView.getItems().clear();
        String messages = serverConnection.sendRequest("getMessages " + currentUser.getUsername());
        if (messages != null && !messages.isEmpty()) {
            for (String message : messages.split("\n")) {
                messagesListView.getItems().add(message);
            }
        }
    }

    @FXML
    private void updateGameRequests() {
        gameRequestsListView.getItems().clear();
        String requests = serverConnection.sendRequest("getRequests " + currentUser.getUsername());
        if (requests != null && !requests.isEmpty()) {
            for (String request : requests.split("\n")) {
                gameRequestsListView.getItems().add(request);
            }
        }
    }

}
