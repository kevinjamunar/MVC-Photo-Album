package controller;

import model.Album;
import model.Photo;
import model.User;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class is a controller for renaming an album.
 * 
 * Just opens a window to get user input for the new album name by a text field.
 * 
 * Also checks for duplicate album name.
 * 
 * @author Brian Schillaci
 *
 */
public class renameAlbumController {

	//renames the album to the information specified in the renameTextField
	@FXML
	private Button submitRenameButton;

	//cancels the renaming of the selected album
	@FXML
	private  Button cancelRenameButton;

	//holds the information for the new album name.
	@FXML
	private TextField renameTextField;

	//holds a reference to the album object to be renamed
	private Album albumToRename;

	//holds a reference to the current user obejct
	private User currentUser;
	
	//returns true if the album name already exists, false otherwise
	private Boolean duplicate;

	//holds a list of albums associated with the current user
	public static ObservableList<Album> obsList;
	
	//holds true if the textfield is empty, false otherwise
	private Boolean isEmpty;
	@FXML
	private Text errorMessageText;
	
	//holds true is the album being renamed was successful
	private boolean isAlbumRenamed;


	/**
	 * Opens the stage associated with the renameAlbumViewController
	 * 
	 * If the user clicks the submit button
	 * the album is rename to the information specified in the renameTextField if the information is not associated with another album.
	 * The stage is then closed
	 * 
	 * If the user clicks the canelRenameButton, the renaming of the selected album is cancelled
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param stage - opens the stage for the renameAlbumController
	 * 
	 */
	public void start(Stage stage) {
		stage.setTitle("Rename Album");
		this.setDuplicate(false);
		// Handles case where the user just clicks on X to close the window, saves the user's changes
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				//System.out.println("Stage is closing");
				stage.close();
			}
		});

		isAlbumRenamed = false;
		cancelRenameButton.setOnMouseClicked((e)->{
			stage.close();
		});

		submitRenameButton.setOnMouseClicked((e)->{
			
			this.setDuplicate(false);
			this.isEmpty = false;
			
			Album newNameAlbum = this.getAlbumToRename();

			for(Album a: this.currentUser.getAlbumList()) {
				
				if(a.getAlbumName().compareTo(renameTextField.getText()) == 0) {
					errorMessageText.setText("Album name already exists");
					errorMessageText.setOpacity(1);
					errorMessageText.setFill(Color.RED);
					this.setDuplicate(true);
				}
				if(renameTextField.getText().trim().isEmpty()) {
					errorMessageText.setText("An album cannot have an empty name");
					errorMessageText.setOpacity(1);
					errorMessageText.setFill(Color.RED);
					this.isEmpty = true;
				}
				
			}
			
			if(!this.getDuplicate() && !isEmpty) {
				
				isAlbumRenamed = true;
				
				newNameAlbum.setAlbumName(renameTextField.getText());
				
				for(Photo p: newNameAlbum.getPhotoList()) {
					p.setAlbumName(renameTextField.getText());
				}
	
				this.getCurrentUser().getAlbumList().remove(this.getAlbumToRename());
	
				this.getCurrentUser().getAlbumList().add(newNameAlbum);
	
				stage.close();
				
				DataController.getInstance().saveUsers();
			}
			
		});
	}

	/**
	 * 
	 * returns a referenct to the album to be renamed
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return albumToRename album to rename
	 * 
	 */
	public Album getAlbumToRename() {
		return albumToRename;
	}

	/**
	 * 
	 * sets the reference to the album to be renamed
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param albumToRename - the album to be renamed
	 * 
	 */
	public void setAlbumToRename(Album albumToRename) {
		this.albumToRename = albumToRename;
	}

	/**
	 * This method returns the user object associated with the current user 
	 * logged in.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return User the current user logged in
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

	/**
	 * returns true if the information in the textfield is the same as an existing album, false otherwise
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return duplicate if a album entry name is duplicate
	 * 
	 */
	public Boolean getDuplicate() {
		return duplicate;
	}
	
	/**
	 * sets the vlalue of this.duplicate to the value specified by the argument duplicate
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param duplicate - holds true if album name exists, false otherwise
	 * 
	 */
	public void setDuplicate(Boolean duplicate) {
		this.duplicate = duplicate;
	}
	
	public boolean getIsAlbumRenamed(){
		return this.isAlbumRenamed;
	}
	
}