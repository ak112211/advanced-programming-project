package view;

import enums.Menu;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.App;
import model.User;
import util.DatabaseConnection;
import util.ServerConnection;

import java.sql.SQLException;
import java.util.List;

import static view.Tools.openMessagingWindow;
import static view.Tools.showAlert;

public class ChatController implements ServerConnection.ServerEventListener {

    @FXML
    private ListView<String> friendRequestsListView;
    @FXML
    private TextField friendUsernameField;
    @FXML
    private ListView<String> friendsListView;
    @FXML
    private ListView<String> gameRequestsListView;
    @FXML
    private Button sendFriendRequestButton;
    @FXML
    private Button sendGameRequestButton;
    @FXML
    private Button acceptRequestButton;
    @FXML
    private Button declineRequestButton;

    private User currentUser;

    @FXML
    private void initialize() {
        currentUser = User.getCurrentUser();

        loadFriendsList();
        loadGameRequests();
        loadFriendRequests();

        sendFriendRequestButton.setOnAction(event -> sendFriendRequest());
        sendGameRequestButton.setOnAction(event -> sendGameRequest());
        acceptRequestButton.setOnAction(event -> acceptGameRequest());
        declineRequestButton.setOnAction(event -> declineGameRequest());

        App.getServerConnection().addMessageListener(this);
    }

    private void loadFriendsList() {
        try {
            List<String> friends = DatabaseConnection.getFriendsList(currentUser.getUsername());
            Platform.runLater(() -> friendsListView.getItems().setAll(friends));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadGameRequests() {
        try {
            List<String> requests = DatabaseConnection.getGameRequests(currentUser.getUsername());
            Platform.runLater(() -> gameRequestsListView.getItems().setAll(requests));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadFriendRequests() {
        try {
            List<String> requests = DatabaseConnection.getFriendRequests(currentUser.getUsername());
            Platform.runLater(() -> friendRequestsListView.getItems().setAll(requests));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendFriendRequest() {
        String friendUsername = friendUsernameField.getText();
        if (friendUsername.isEmpty()) {
            showAlert("Error", "Input Error", "Please enter a friend's username.");
            return;
        }

        String userId = currentUser.getUsername();
        if (userId.equals(friendUsername)) {
            showAlert("Error", "Input Error", "You cannot send a friend request to yourself.");
            return;
        }

        App.getServerConnection().sendMessage(friendUsername + ":send friend request");

        try {
            if (DatabaseConnection.sendFriendRequest(userId, friendUsername)) {
                showAlert("Success", "Friend Request Sent", "Friend request sent successfully.");
            } else if (DatabaseConnection.getUser(friendUsername) == null) {
                showAlert("Error", "Request Failed", "No such user");
            } else {
                showAlert("Error", "Request Failed", "Failed to send friend request. The user might already be your friend or a request is pending.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while sending the friend request. Please try again.");
        }
    }

    @FXML
    private void sendGameRequest() {
        String senderId = currentUser.getUsername();
        String receiverId = friendsListView.getSelectionModel().getSelectedItem();
        if (receiverId == null) {
            showAlert("Error", "Input Error", "Please select a friend to send a game request.");
            return;
        }

        App.getServerConnection().sendMessage(receiverId + ":send game request");

        try {
            if (DatabaseConnection.saveGameRequest(senderId, receiverId)) {
                showAlert("Success", "Game Request Sent", "Game request sent successfully.");
                loadGameRequests();
            } else {
                showAlert("Error", "Request Failed", "Either sending request failed or already sent pending request.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "An error occurred while sending the game request. Please try again.");
        }
    }

    @FXML
    private void acceptGameRequest() {
        String selectedRequest = gameRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedRequest == null || !selectedRequest.split(" ")[7].equals("pending")) {
            showAlert("Error", "No Pending Request Selected", "Please select a pending game request to accept.");
            return;
        }

        System.out.println(selectedRequest);
        String sender = selectedRequest.split(" ")[3];
        try {
            DatabaseConnection.acceptGameRequest(sender, currentUser.getUsername());
            App.getServerConnection().sendMessage("accepted game request from:" + sender);
            showAlert("Success", "Game Request Accepted", "Game request accepted. Starting game...");
            ChooseDeckMenuController.player2 = DatabaseConnection.getUser(sender);
            startGame();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Request Failed", "Failed to accept game request. Please try again.");
        }
    }

    @FXML
    private void declineGameRequest() {
        String selectedRequest = gameRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedRequest == null || !selectedRequest.split(" ")[7].equals("pending")) {
            showAlert("Error", "No Pending Request Selected", "Please select a pending game request to decline.");
            return;
        }

        String sender = selectedRequest.split(" ")[3];
        try {
            DatabaseConnection.declineGameRequest(sender, currentUser.getUsername());
            showAlert("Success", "Game Request Declined", "Game request declined.");
            loadGameRequests();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Request Failed", "Failed to decline game request. Please try again.");
        }
    }

    private void startGame() {
        ChooseDeckMenuController.isMulti = true;
        App.loadScene("/fxml/ChooseDeckMenu.fxml");
    }

    @FXML
    private void goBack() {
        App.loadScene(Menu.MAIN_MENU.getPath());
    }

    public void acceptFriendRequest() {
        String selectedRequest = friendRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedRequest == null || !selectedRequest.split(" ")[7].equals("pending")) {
            showAlert("Error", "No Pending Request Selected", "Please select a pending friend request to accept.");
            return;
        }

        String sender = selectedRequest.split(" ")[3];
        try {
            DatabaseConnection.acceptFriendRequest(sender, currentUser.getUsername());
            App.getServerConnection().sendMessage("accepted friend request from:" + sender);
            showAlert("Success", "Friend Request Accepted", "Friend request accepted.");
            loadFriendRequests();
            loadFriendsList();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Request Failed", "Failed to accept friend request. Please try again.");
        }
    }

    public void declineFriendRequest() {
        String selectedRequest = friendRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedRequest == null || !selectedRequest.split(" ")[7].equals("pending")) {
            showAlert("Error", "No Pending Request Selected", "Please select a pending friend request to decline.");
            return;
        }

        String sender = selectedRequest.split(" ")[3];
        try {
            DatabaseConnection.declineFriendRequest(sender, currentUser.getUsername());
            showAlert("Success", "Friend Request Declined", "Friend request declined.");
            loadFriendRequests();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Request Failed", "Failed to decline friend request. Please try again.");
        }
    }

    public void startChat() {
        String selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend == null) {
            showAlert("Error", "No Friend Selected", "Please select a friend to chat with.");
            return;
        }

        openMessagingWindow(selectedFriend);
    }

    @Override
    public void handleServerEvent(String input) {
        Platform.runLater(() -> {
            if (input.startsWith("Friend request from ")
                    || input.startsWith("Game request from ")
                    || input.startsWith("Message from ")
                    || input.startsWith("Game request accepted by ")
                    || input.startsWith("Friend request accepted by ")) {
                loadFriendsList();
                loadFriendRequests();
                loadGameRequests();
                if (input.startsWith("Message from ")) {
                    showAlert(input);
                } else if (input.startsWith("Game request accepted by ")) {
                    try {
                        ChooseDeckMenuController.player2 = DatabaseConnection.getUser(input.split(" ")[4]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    startGame();
                }
            }
        });
    }

    public void cleanup() {
        App.getServerConnection().removeMessageListener(this);
    }
}