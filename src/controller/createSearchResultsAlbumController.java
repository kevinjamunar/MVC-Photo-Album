package controller;

import model.Album;
import model.Photo;
import model.User;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * This controller is for creating an album from search results.
 * 
 * It gets the album name the user wants and creates the search result album.
 * @author Brian Schillaci
 *
 */
public class createSearchResultsAlbumController {

	//renames the Album when clicked
	@FXML
	private Button submitRenameButton;

	//cancels the renaming of the album when clicked
	@FXML
	private Button cancelRenameButton;

	//holds the information for the new album name
	@FXML
	private TextField renameTextField;

	//holds a reference to the new album being created -NOT USED
	private Album albumToCreate;

	//holds a reference to the current user logged in
	private User currentUser;

	//NOT USED
	public static ObservableList<Album> obsList;

	/**
	 * Sets up the stage List of albums.
	 * removes the stage for the renaming of an album if the the "cancel" button on that stage is clicked
	 * renames the album if the "ok" button is clicked
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 *
	 * 
	 * @param stage - stage for the list of albums
	 */
	public void start(Stage stage) {
		stage.setTitle("Create Search Results Album");

		// Handles case where the user just clicks on X to close the window, saves the user's changes
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				//System.out.println("Stage is closing");
				stage.close();
			}
		});

		cancelRenameButton.setOnMouseClicked((e)->{
			stage.close();
		});

		submitRenameButton.setOnMouseClicked((e)->{
			Album newNameAlbum = this.getAlbumToCreate();

			newNameAlbum.setAlbumName(renameTextField.getText());

			this.getCurrentUser().getAlbumList().add(newNameAlbum);	
			
			for(Photo p: newNameAlbum.getPhotoList()) {
				p.setAlbumName(renameTextField.getText());
			}

			stage.close();
			
			DataController.getInstance().saveUsers();
		});
	}

	/**
	 * returns a reference to the album that is being created
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 *
	 * 
	 * @return albumToCreate album to create
	 */
	public Album getAlbumToCreate() {
		return albumToCreate;
	}

	/**
	 * Sets the reference to the album being created
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 *
	 * 
	 * @param albumToCreate album to create
	 */
	public void setAlbumToCreate(Album albumToCreate) {
		this.albumToCreate = albumToCreate;
	}

	/**
	 * This method returns the user object associated with the current user 
	 * logged in.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return User returns the current user logged in
	 * 
	 */
	public User getCurrentUser() {
		return currentUser;
	}
	
	/**
	 * This method sets the user reference associated with the current user 
	 * logged in.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param currentUser the current user 
	 * 
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}