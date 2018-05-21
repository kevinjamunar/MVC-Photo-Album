package controller;

import java.io.IOException;
import model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * 
 * 
 * This is the controller method for the admin view.
 * 
 * It has a list view that shows all of the accounts that have been created.
 * 
 * It also has buttons to add users, to delete users, and to log out of the admin account.
 * 
 * @author Brian Schillaci
 * @author Kevin Jamunar
 * 	
 */
public class AdminViewController {

	//logs the current user out
	@FXML
	private Button logoutButton;

	//adds the user to the users list
	@FXML
	private Button addUserButton;

	//deletes a user from the users list
	@FXML
	private Button deleteUserButton;

	//holds a list of the users in a String format in a ListView
	@FXML
	private ListView<String> userListView;

	//holds an observable list of users
	private ObservableList<String> usersList;

	//holds all the users in a users object
	private Users users;

	/**
	 * Loads the CreateAccountController and associates the delete user button 
	 * and logout button with click events. If the delete user button is clicked,
	 * the user is removed from the users object and the ListView is refreshed.
	 * If the logout button is clicked the current user is logged out and the 
	 * loginViewController is loaded
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param mainstage - loads the main stage of the program
	 * 
	 */
	public void start(Stage mainstage){
		getAllUsers();
		refreshList();

		//Event that happens when the user clicks the X to close album view
		mainstage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				// Save the user's changes
				DataController.getInstance().saveUsers();
			}
		});

		// Creating a new account in the Admin View
		addUserButton.setOnMouseClicked((e)->{
			try {
				Stage stage;
				Parent root;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateAccountView.fxml"));
				root = loader.load();
				stage = new Stage();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				CreateAccountController ac = loader.getController();
				ac.start(stage);
				stage.centerOnScreen();
				stage.showAndWait();
				refreshList();
			} catch (IOException f) {
				f.printStackTrace();
			}

		});

		// Deleting an account from the Admin View
		deleteUserButton.setOnMouseClicked((e)->{
			int selectedIndex = userListView.getSelectionModel().getSelectedIndex();
			if(selectedIndex!=-1){
				this.users.users.remove(selectedIndex);
				DataController.getInstance().saveUsers();
				getAllUsers();
				refreshList();
				userListView.getSelectionModel().clearSelection();
			}
		});

		// Logging out the Admin
		logoutButton.setOnMouseClicked((e)->{
			DataController.getInstance().saveUsers();

			try {
				Stage stage;
				Parent root;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
				root = loader.load();
				stage = (Stage)this.logoutButton.getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				LoginViewController lc = loader.getController();
				lc.start(stage);
				stage.centerOnScreen();
				stage.show();				
			} catch (IOException i) {
				i.printStackTrace();
			}
		});

	}

	/**
	 * sets the users object to the list of all users
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 */
	public void getAllUsers(){
		this.users = DataController.getInstance().getUsers();
	}
	/**
	 * refreshes the list of users and sets the refreshed list to the userListView
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	public void refreshList(){
		usersList=FXCollections.observableArrayList(users.getUserNames());
		userListView.setItems(usersList);
	}

}
