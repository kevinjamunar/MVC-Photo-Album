package controller;

import java.util.ArrayList;
import model.Album;
import model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This controller is for the window opened when a user is selecting an album to move or copy a photo to.
 * 
 * If the user is trying to move a photo, the current album is removed from the list and later added back to prevent the
 * user from moving a photo to the same album.
 * 
 * @author Brian Schillaci
 *
 */
public class SelectAlbumViewController {

	//submits the album to move/copy to when clicked 
	@FXML
	private Button submitAlbumButton;

	//cancels submission of the album to move/copy to when cicked
	@FXML
	private Button cancelButton;

	//reference to the current user
	@FXML
	private User currentUser;

	//String representation of album selected by the user to move/copy to - NOT USED
	@FXML
	private String albumSelected;

	//holds title for the current controller
	@FXML
	private Text title;

	//list view representation of the albums to move/copy to
	@FXML
	private ListView<String> albumListView;

	//arraylist of the albums to move/copy to 
	private ArrayList<Album> albumList;

	/**
	 * 
	 * Stores the list of albums inside of an observable list then gets the album selected by the 
	 * user from the listview and sets the album selected to be used by the AlbumViewController class
	 * via the setAlbumSelected() method
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param stage - opens the the stage for the SelectAlbumViewController
	 * 
	 */
	public void start(Stage stage) {
		stage.setTitle("Select an Album");

		//adds albums to an observable list to add to the list view
		ObservableList<String> albums = FXCollections.observableArrayList();
		for(int j=0; j < currentUser.getAlbumList().size(); j++) {
			albums.add(currentUser.getAlbumList().get(j).getAlbumName());
		}
		albumListView.setItems(albums);
		albumListView.getSelectionModel().select(0);

		// Handles case where the user just clicks on X to close the window, saves the user's changes
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				//System.out.println("Stage is closing");
				stage.close();
			}
		});

		// User cancels selecting an album to move or copy into
		cancelButton.setOnMouseClicked((e)->{
			stage.close();
		});

		// User has clicked submit button to submit an album to be selected for moving or copying
		submitAlbumButton.setOnMouseClicked((e)->{
			String selected = albumListView.getSelectionModel().getSelectedItem();
			if(selected == null) {
				//System.out.println("No album selected");
				return;
			}

			// Take this selected album and return it to the albumviewcontroller 
			// to either move the photo to this album or copy to this album
			this.setAlbumSelected(selected);

			stage.close();
		});
	}

	/**
	 * 
	 * returns the album selected to use in the move/copy operation
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return albumSelected selected album in the list view
	 * 
	 */
	public String getAlbumSelected() {
		return albumSelected;
	}

	/**
	 * 
	 * sets the value of the album selected to  be used in the move/copy operation
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param albumSelected - the album selected to use in the move/copy operation
	 * 
	 */
	public void setAlbumSelected(String albumSelected) {
		this.albumSelected = albumSelected;
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

	/**
	 * returns the arraylist of albums associated with the current user logged in
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return albumList list of albums of the current user
	 * 
	 */
	public ArrayList<Album> getAlbumList() {
		return albumList;
	}

	/**
	 * This method sets the arraylist of albums associated with the current user logged in
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param albumList - arraylist of albums associated with the current user logged in
	 * 
	 */
	public void setAlbumList(ArrayList<Album> albumList) {
		this.albumList = albumList;
	}

	/**
	 * This method returns the title for the current controller
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return title.getText()
	 * 
	 */
	public String getTitle() {
		return title.getText();
	}
	
	/**
	 * This method sets the title for the current controller
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param title title for the controller
	 * 
	 */
	public void setTitle(String title) {
		this.title.setText(title);
	}
}
