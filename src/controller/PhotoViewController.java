package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

/**
 * This is the controller for the photo view.
 * 
 * Photo view displays a photo's image along with its date modified and its caption, also a list of its tags.
 * 
 * @author Brian Schillaci
 *
 */
public class PhotoViewController {

	/********************************* VARIABLE LIST **********************************/
	
	private User currentUser;

	// This is the album the was opened in the userViewController
	private Album currentAlbum;

	// This holds the image being displayed on the Stage
	@FXML
	private ImageView imageView;

	//This holds the results of the search on the photo
	private ArrayList<Photo> searchResultsList;

	//This holds the caption associated with the photo being viewed. 
	@FXML
	private Text photoCaption;

	//This holds the last modified date of the photo
	@FXML
	private Text photoDateText;

	//This holds the list of tags for photo being viewed.
	@FXML		
	private ListView<String> tagListView;

	//set to true if the searchResultsList has data, false otherwise
	private Boolean searchResultPhotoView;

	//Holds the list of tags associated with a given photo
	ObservableList<String> tags = FXCollections.observableArrayList();

	//holds the photo object clicked in the AlbumView scene
	private Photo photoClicked; 

	/********************************* METHOD LIST **********************************/

	/**
	 * Sets the date associated with the photo, its caption, and its the list of tags.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param stage - sets the stage to the PhotoViewController 
	 * 
	 */
	public void start(Stage stage) {

		stage.setTitle("Photo View");

		for(int k=0; k < photoClicked.getPhotoTags().size(); k++) {		
			tags.add("(\"" + photoClicked.getPhotoTags().get(k).getName() + "\"" + ", " + "\"" + photoClicked.getPhotoTags().get(k).getValue() + "\")");		
		}		
		tagListView.setItems(tags);		
		tagListView.getSelectionModel().select(0);	

		StringBuilder caption = new StringBuilder();

		if(this.searchResultsList == null) {	
			//Sets the photo's caption when the stage is loaded
			for(int i=0; i<currentAlbum.getPhotoList().size(); i++) {
				Photo photo = currentAlbum.getPhotoList().get(i);
				if(photo.equals(photoClicked)){
					caption.append("Caption: ");
					if(photo.getPhotoCaption() != null) {
						caption.append(photo.getPhotoCaption());
					}
					caption.append("\nDate: ");
					if(photo.getLastModifiedDate() != null) {
						caption.append(new SimpleDateFormat("MM/dd/yyyy, h:mm:ss aaa").format(photo.getLastModifiedDate()).toString());
					}
				}
			}
		}
		else {
			for(Photo p: this.searchResultsList) {
				if(p.equals(photoClicked)){
					caption.append("Caption: ");
					if(p.getPhotoCaption() != null) {
						caption.append(p.getPhotoCaption());
					}
					caption.append("\nDate: ");
					if(p.getLastModifiedDate() != null) {
						caption.append(new SimpleDateFormat("MM/dd/yyyy, h:mm:ss aaa").format(p.getLastModifiedDate()).toString());
					}
				}
			}
		}
		photoCaption.setText(caption.toString());
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
	 * @param u the current user 
	 * 
	 */
	public void setCurrentUser(User u){
		this.currentUser=u;
	}

	/**
	 * This method returns the currentAlbum reference associated with the current album being viewed.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return User returns the current Album being viewed 
	 * 
	 */
	public Album getCurrentAlbum() {
		return currentAlbum;
	}
	
	/**
	 * This method sets the currentAlbum reference to the current album being viewed.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param currentAlbum The current album being viewed
	 * 
	 */
	public void setCurrentAlbum(Album currentAlbum) {
		this.currentAlbum = currentAlbum;
	}
	/**
	 * This method sets the photoClicked object to the current photo clicked on the TilePane
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param photoClicked the current photo that is clicked
	 * 
	 */
	public void setPhotoClicked(Photo photoClicked) {
		this.photoClicked = photoClicked;
	}

	/**
	 * This method sets the image displayed on the current stage to the imageView
	 * associated with the photo clicked by the user.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param imageView the current photo that is clicked
	 * 
	 */
	public void setImageView(ImageView imageView) {
		//System.out.println(imageView);
		this.imageView.setImage(imageView.getImage());
	}

	/**
	 * This method gets the object associated with the current photo clicked on the TilePane
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return Photo returns the photoClicked on the TilePane
	 * 
	 */
	public Photo getPhotoClicked() {
		return this.photoClicked;
	}


	/**
	 * This method returns true if the searchResultPhotoView list isn't empty, false otherwise
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return returns true if the searchResult list isn't empty, false otherwise
	 * 
	 */
	public Boolean getSearchResultPhotoView() {
		return searchResultPhotoView;
	}

	/**
	 * This method sets true or false to searchResultPhotoView if list associated with the search results is not empty or empty respectively
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param searchResultPhotoView  boolean value of whether this photo view is from a search result
	 * 
	 */
	public void setSearchResultPhotoView(Boolean searchResultPhotoView) {
		this.searchResultPhotoView = searchResultPhotoView;
	}

	/**
	 * This method returns the list associated with the search results.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return searchResultsList arraylist of the search results
	 * 
	 */
	public ArrayList<Photo> getSearchResultsList() {
		return searchResultsList;
	}

	/**
	 * This method sets the list associated with the search results.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param searchResultsList arraylist of the search results
	 * 
	 */
	public void setSearchResultsList(ArrayList<Photo> searchResultsList) {
		this.searchResultsList = searchResultsList;
	}
}
