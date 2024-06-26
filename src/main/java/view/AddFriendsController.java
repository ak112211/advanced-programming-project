package view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import model.App;
import model.User;

public class AddFriendsController {

    @FXML
    private TextField friendUsernameField;
    @FXML
    private ListView<String> friendsListView;

    private User currentUser;

    @FXML
    private void initialize() {
        currentUser = User.getCurrentUser();
        friendsListView.getItems().addAll(currentUser.getFriends());
    }

    @FXML
    private void handleAddFriend() {
        String friendUsername = friendUsernameField.getText();
        if (friendUsername.isEmpty()) {
            showAlert("Please enter a friend's username.");
            return;
        }

        // Send request to server to add friend
        String request = String.format("addFriend %s %s", currentUser.getUsername(), friendUsername);
        String response = App.getServerConnection().sendRequest(request);

        if (response.equals("Friend added successfully.")) {
            currentUser.addFriend(friendUsername);
            friendsListView.getItems().add(friendUsername);
        }
        showAlert(response);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
