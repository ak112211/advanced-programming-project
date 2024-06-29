package view;

import enums.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import model.App;
import model.User;
import util.DatabaseConnection;
import util.ServerConnection;

import java.sql.SQLException;
import java.util.List;

public class ChatController {

    @FXML
    private TextField friendUsernameField;
    @FXML
    private ListView<String> friendsListView;
    @FXML
    private ListView<String> pendingRequestsListView;
    @FXML
    private ListView<String> messagesListView;
    @FXML
    private TextField messageField;
    @FXML
    private ListView<String> gameRequestsListView;
    @FXML
    private TextField requestUsernameField;

    private User currentUser;
    private ServerConnection serverConnection;

    @FXML
    private void initialize() throws SQLException {
        currentUser = User.getCurrentUser();
        serverConnection = new ServerConnection();

        // Load friends and pending requests
        friendsListView.getItems().addAll(currentUser.getFriends());
        pendingRequestsListView.getItems().addAll(currentUser.getPendingRequests());

        // Add listener for pending requests
        pendingRequestsListView.setOnMouseClicked(this::handlePendingRequestSelection);

        // Load messages and game requests
        loadMessages();
        loadGameRequests();
    }

    private void loadMessages() throws SQLException {
        // Load messages from the server or database
        List<String> messages = DatabaseConnection.getMessages(currentUser.getUsername());
        messagesListView.getItems().addAll(messages);
    }

    private void loadGameRequests() throws SQLException {
        // Load game requests from the server or database
        List<String> gameRequests = DatabaseConnection.getGameRequests(currentUser.getUsername());
        gameRequestsListView.getItems().addAll(gameRequests);
    }

    @FXML
    private void handlePendingRequestSelection(MouseEvent event) {
        String selectedRequest = pendingRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedRequest == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Accept Friend Request");
        alert.setHeaderText("Do you want to accept the friend request from " + selectedRequest + "?");
        alert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                try {
                    handleAcceptFriendRequest(selectedRequest);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Tools.showAlert("Error", "Database Error", "An error occurred while accepting the friend request. Please try again.");
                }
            }
        });
    }

    private void handleAcceptFriendRequest(String selectedRequest) throws SQLException {
        // Accept friend request
        boolean isFriendAdded = DatabaseConnection.addFriend(currentUser.getUsername(), selectedRequest);
        if (isFriendAdded) {
            currentUser.addFriend(selectedRequest);
            currentUser.removePendingRequest(selectedRequest);
            friendsListView.getItems().add(selectedRequest);
            pendingRequestsListView.getItems().remove(selectedRequest);
            Tools.showAlert("Success", "Friend Request Accepted", "Friend request accepted successfully.");
            startGameSetup(selectedRequest);
        } else {
            Tools.showAlert("Error", "Database Error", "An error occurred while accepting the friend request. Please try again.");
        }
    }

    private void startGameSetup(String opponentUsername) throws SQLException {
        // Set up the start game menu and navigate to ChooseDeckMenu with isMulti set to true
        User opponent = DatabaseConnection.getUser(opponentUsername);
        assert opponent != null;
        ChooseDeckMenuController.opponent = opponent.getUsername();
        ChooseDeckMenuController.isMulti = true;
        App.loadScene(Menu.DECK_MENU.getPath());
    }


    @FXML
    private void handleAddFriend() throws SQLException {
        String friendUsername = friendUsernameField.getText();
        if (friendUsername.isEmpty()) {
            Tools.showAlert("Error", "Input Error", "Please enter a friend's username.");
            return;
        }

        // Check if the friend exists in the database
        if (DatabaseConnection.getUser(friendUsername) == null) {
            Tools.showAlert("Error", "User Not Found", "The entered friend's username does not exist.");
            return;
        }

        // Send friend request
        boolean isFriendRequestSent = DatabaseConnection.sendFriendRequest(currentUser.getUsername(), friendUsername);
        if (isFriendRequestSent) {
            currentUser.addPendingRequest(friendUsername);
            pendingRequestsListView.getItems().add(friendUsername);
            Tools.showAlert("Success", "Friend Request Sent", "Friend request sent successfully.");
        } else {
            Tools.showAlert("Error", "Database Error", "An error occurred while sending the friend request. Please try again.");
        }
    }

    @FXML
    private void handleSendMessage() {
        String message = messageField.getText();
        if (message.isEmpty()) {
            Tools.showAlert("Error", "Input Error", "Please enter a message.");
            return;
        }

        // Send message to server and update the messages list
        String response = serverConnection.sendRequest("sendMessage " + currentUser.getUsername() + " " + message);
        if ("Success".equals(response)) {
            messagesListView.getItems().add("Me: " + message);
            messageField.clear();
        } else {
            Tools.showAlert("Error", "Message Error", "An error occurred while sending the message. Please try again.");
        }
    }

    @FXML
    private void handleSendGameRequest() {
        String username = requestUsernameField.getText();
        if (username.isEmpty()) {
            Tools.showAlert("Error", "Input Error", "Please enter a username to send a game request.");
            return;
        }

        // Send game request to server and update the game requests list
        String response = serverConnection.sendRequest("sendGameRequest " + currentUser.getUsername() + " " + username);
        if ("Success".equals(response)) {
            gameRequestsListView.getItems().add("Game request sent to: " + username);
            requestUsernameField.clear();
        } else {
            Tools.showAlert("Error", "Game Request Error", "An error occurred while sending the game request. Please try again.");
        }
    }

    @FXML
    private void goBack() {
        App.loadScene(Menu.MAIN_MENU.getPath());
    }

}
