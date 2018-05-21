package controller;


import java.io.IOException;
import model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * 
 * 
 * This is the controller method for the account creation view.
 * 
 * It has text areas where the user can enter the username and password they want.
 * 
 * It also a confirm password spot so the user knows they entered their password correctly.
 * 	
 * @author Brian Schillaci
 * @author Kevin Jamunar
 */
public class CreateAccountController {

	//takes in the username entered by a new person
	@FXML
	private TextField userTextField;

	//takes in a password entered by a new person
	@FXML
	private PasswordField passWordField;

	//takes in the password confirmation entered by a new person
	@FXML
	private PasswordField confirmPasswordField;

	//displays an error message if the passwords do not match
	@FXML
	private Text noMatchPass;

	//displays an error message if the information entered is the same as an existing user
	@FXML
	private Text userExistsText;

	//displays an error message if a text box is empty
	@FXML
	private Text fillInBoxesText;

	//calls the createUser method
	@FXML
	private Button createUserButton;


	/**
	 * If all infromation entered by the new person is valid this method
	 * creates a user object and adds it to the users object.
	 * The userViewController is then loaded
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param event - in the event that the user clicks the create user button
	 * 
	 */
	@FXML
	void createUser(ActionEvent event) {
		noMatchPass.setOpacity(0);
		fillInBoxesText.setOpacity(0);
		userExistsText.setOpacity(0);

		if(userTextField.getText().isEmpty() || passWordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
			//System.out.println("Not all boxes are filled");
			fillInBoxesText.setOpacity(1);
			return;
		}

		if(passWordField.getText().compareTo(confirmPasswordField.getText()) != 0){
			noMatchPass.setOpacity(1);
		}
		else{
			if(userTextField.getText().compareTo("stock") != 0 || passWordField.getText().compareTo("stock") != 0) {
				Users userList = DataController.getInstance().getUsers();
				User newU = new User(userTextField.getText(),passWordField.getText());
				//System.out.println(newU.getUsername());
				boolean newUser = userList.addUser(newU);

				if(newUser == false) {
					//System.out.println("This user already exists.");
					userExistsText.setOpacity(1);
					return;
				}

			}
			else {
				User u = new User("stock");
				Users userList = DataController.getInstance().getUsers();
				boolean newUser = userList.addUser(u);

				if(newUser == false) {
					//System.out.println("This user already exists.");
					userExistsText.setOpacity(1);
					return;
				}
			}

			DataController.getInstance().saveUsers();
			try {
				Stage stage;
				Parent root;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserView.fxml"));
				root = loader.load();
				stage = (Stage)this.createUserButton.getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.close();
				// never opens the user view, just closes the account view page
				// not a problem but might be later?
				// -Brian
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets up the stage for the user to create an account.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 *
	 * 
	 * @param mainstage starting stage for create account
	 */
	public void start(Stage mainstage){
		mainstage.setTitle("Create Account");

	}
}

