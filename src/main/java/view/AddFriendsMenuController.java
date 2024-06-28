package view;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.User;
import util.DatabaseConnection;

import java.sql.SQLException;

public class AddFriendsMenuController {

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
    private void handleAddFriend() throws SQLException {
        String friendUsername = friendUsernameField.getText();
        if (friendUsername.isEmpty()) {
            Tools.showAlert("Error", "Input Error", "Please enter a friend's username.");
            return;
        }

        // Check if the friend exists in the database
        boolean isFriendExist = DatabaseConnection.isUsernameTaken(friendUsername);
        if (!isFriendExist) {
            Tools.showAlert("Error", "User Not Found", "The entered friend's username does not exist.");
            return;
        }

        // Add friend to the database
        boolean isFriendAdded = DatabaseConnection.addFriend(currentUser.getUsername(), friendUsername);
        if (isFriendAdded) {
            currentUser.addFriend(friendUsername);
            friendsListView.getItems().add(friendUsername);
            Tools.showAlert("Success", "Friend Added", "Friend added successfully.");
        } else {
            Tools.showAlert("Error", "Database Error", "An error occurred while adding the friend. Please try again.");
        }
    }


}
