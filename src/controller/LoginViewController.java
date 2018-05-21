package controller;

import java.io.IOException;

import controller.UserViewController;
import model.User;
import model.Users;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This is the controller for the login screen.
 * 
 * It handles logging in and also opening the create account screen.
 * 
 * If the user logs in with admin, admin - they can access the admin account.
 * @author Brian Schillaci
 *
 */
public class LoginViewController {

	//takes in the username for a user
	@FXML
	private TextField usernameField;
	
	//logs in the user based on the values in the TextFields
	@FXML
	private Button loginButton;

	//takes in the password for a user
	@FXML
	private PasswordField passField;

	//displays an error message if the information in the TextFields are invalid
	@FXML
	private Text wrongPassText;

	//creates an account based on the information entered in the TextFields
	@FXML
	private Button createAccountButton;

	//NOT USED
	@FXML
	private Button forgotPasswordButton;


	/**
	 * This method opens the stage CreateAccountController when a person clicks the create account button
	 * 
	 * @author Brian Schillaci
	 * @author Kevin Jamunar
	 * 
	 * @param event Clicking on the create account button.
	 */
	@FXML
	void createAccount(ActionEvent event) {
		try {
			wrongPassText.setOpacity(0.0);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method opens the stage UserViewController when the login button is clicked
	 * and the user information is valid
	 * 
	 * @author Brian Schillaci
	 * @author Kevin Jamunar
	 *
	 * 
	 * @param event Clicking on the login button.
	 */
	@FXML
	void login(ActionEvent event) {
		User validUser;
		Users userList = DataController.getInstance().getUsers();
		for(User u: userList.users){
			if(u.getUsername().compareTo(usernameField.getText())==0){
				if(u.getPassword().compareTo(passField.getText())==0){
					validUser = u;
					try//to set up the stage 
					{
						Stage stage;
						Parent root;
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserView.fxml"));
						root = loader.load();
						stage = (Stage)this.loginButton.getScene().getWindow();
						Scene scene = new Scene(root);
						stage.setScene(scene);
						UserViewController uc = loader.getController();
						uc.setCurrentUser(validUser);
						uc.start(stage);
						stage.centerOnScreen();
						stage.show();

					}catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(usernameField.getText().compareTo("admin") == 0 && passField.getText().compareTo("admin") == 0){
			try{
				// Setting up the stage for the Admin View
				Stage stage;
				Parent root;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
				root = loader.load();
				stage = (Stage)this.loginButton.getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				AdminViewController ac = loader.getController();
				ac.start(stage);
				stage.centerOnScreen();
				stage.show();
			}catch (IOException e) {
				e.printStackTrace();
			}

		}
		else{
			wrongPassText.setOpacity(1);
		}	
	}

	/**
	 * Sets up the stage for the user to log in.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 *
	 * 
	 * @param mainstage stage that is initially loaded for login, has username and pass fields, submit button, and create account button
	 */
	public void start(Stage mainstage){
		mainstage.setTitle("Login");

		// Handles case where the user just clicks on X to close the window
		mainstage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				mainstage.close();
			}
		});

	}

}
