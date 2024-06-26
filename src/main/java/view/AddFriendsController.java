package view;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.User;
import java.util.List;

public class AddFriendsController {

    @FXML
    private TextField usernameField;
    @FXML
    private ListView<String> friendsListView;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = User.getCurrentUser();
        loadFriends();
    }

    @FXML
    private void addFriend() {
        String username = usernameField.getText();
        if (username.isEmpty()) {
            showError("Username cannot be empty.");
            return;
        }

        if (username.equals(currentUser.getUsername())) {
            showError("You cannot add yourself as a friend.");
            return;
        }
        /*
        if (currentUser.addFriend(username)) {
            friendsListView.getItems().add(username);
            usernameField.clear();
        } else {
            showError("Failed to add friend. Username may be invalid or already a friend.");
        }
        */
    }

    @FXML
    private void goBackToMainMenu() {
        // Implement navigation back to main menu
    }

    private void loadFriends() {
        List<String> friends = currentUser.getFriends();
        friendsListView.getItems().setAll(friends);
    }

    private void showError(String message) {
        // Implement error display (e.g., using an alert dialog)
    }
}
