package application;
	

import controller.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Photos extends Application {
	
	/**
	 * Loads the controller associated with the Login View when the application starts up
	 * 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 */
	@Override
	public void start(Stage stage) {
		try {
			/**
			 * On the start of the program, the login screen will appear
			 */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/LoginView.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			LoginViewController lc = loader.getController();
			lc.start(stage);
			stage.show();	
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
