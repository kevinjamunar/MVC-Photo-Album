package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.User;
import model.Album;
import model.Photo;

/**
 * This controller handles the search results window, that is opened and loaded when a user searches for either a date range or some photo tags.
 * 
 * It controls viewing photos in the results, creating an album from the results, and slideshow of the results.
 * 
 * @author Brian Schillaci
 *
 */
public class SearchViewController {

	//goes back to the stage associated with the userViewController
	@FXML
	private Button homeButton;

	//sets the text to the search range or tags depending on which one the user searches for.
	@FXML
	private Text searchRangeOrTagText;

	//opens the slideshow view for the album selected
	@FXML
	private Button slideshowButton;

	//logs the user out and saves changes
	@FXML
	private Button logoutButton;

	//sets text for successfully created album
	@FXML
	private Text successCreateText;

	//creates and album out of the search results
	@FXML
	private Button createSearchResultsAlbumButton;

	//sets a message if the user clicks view photo but no photo was selected
	@FXML
	private Text errorMessage;

	//loads the stage for the PhotoViewController
	@FXML
	private Button viewPhotoButton;

	//TilePane for the search results
	@FXML
	private TilePane searchResultsTilePane;

	//reference to the current user
	private User currentUser;

	//image selected from the TilePane
	ImageView currentSelectedImage = null;

	//List of photos from the search results
	private ArrayList<Photo> searchResultsList;

	//list of photos for the tags that were searched for
	private ArrayList<Photo> searchedTags;

	//reference to photo object for the current photo clicked
	private Photo photoClicked;


	private String tags;

	/**
	 * returns a String representation of the tags searched for 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return String string representation of a tag
	 * 
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * sets the value of tags to the String representation of the tags searched for 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param tags - string representation of the tags searched for
	 * 
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	private String dateRange; //holds the date range for the search results

	/**
	 * Loads the TilePane to display all the images from the search results.
	 * Sets the text to the date range or tags based on which option the user searched by.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param stage - opens the stage associated with the SearchViewController
	 * 
	 */
	public void start(Stage stage) {
		//System.out.println(searchResultsList.size());
		stage.setTitle("Search Results");
		this.successCreateText.setOpacity(0);
		// If there are no search results, disable the slideshow and view photo buttons
		if(this.searchResultsList.size() == 0) {
			this.slideshowButton.setDisable(true);
			this.viewPhotoButton.setDisable(true);
		}

		for(int i = 0; i < searchResultsList.size(); i++) {
			ImageView imageView = new ImageView(new Image((new File(searchResultsList.get(i).getPhotoLocation()).toURI().toString())));
			imageView.setFitHeight(150);
			imageView.setFitWidth(150);
			imageView.setPreserveRatio(true);
			BorderPane bp = new BorderPane(imageView, new Label( searchResultsList.get(i).getPhotoCaption()), null, null, null);
			bp.setOnMouseClicked(e->setCurrentSelectedImage(bp, imageView));
			searchResultsTilePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			searchResultsTilePane.getChildren().add(bp);
		}

		if(dateRange != null) {
			this.searchRangeOrTagText.setText(dateRange);
		}
		else {
			this.searchRangeOrTagText.setText(tags);
		}

		//Event that happens when the user clicks the X to close album view
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				// Save the user's changes
				DataController.getInstance().saveUsers();
			}
		});
		
		
		logoutButton.setOnMouseClicked((e)->{
			DataController.getInstance().saveUsers();

			try { 
				Stage s;
				Parent root;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
				root = loader.load();
				s = (Stage)this.logoutButton.getScene().getWindow();
				Scene scene = new Scene(root);
				s.setScene(scene);
				LoginViewController lc = loader.getController();
				lc.start(stage);
				s.centerOnScreen();
				s.show();				
			} catch (IOException i) {
				i.printStackTrace();
			}
		});


	}

	/**
	 * Sets the images for the TilePane where each child of the TilePane is a BorderPane object
	 * consisting of Label and ImageView objects
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param bp the BorderPane associated with each child in the TilePane
	 * @param i the ImageView of the current selected image in the TilePane
	 * 
	 */
	private void setCurrentSelectedImage(BorderPane bp, ImageView i) {
		viewPhotoButton.setDisable(false);	
		//if no image was selected and an error message appears: the error message goes away once an image is selected
		errorMessage.setOpacity(0);

		//checks if a previous image is selected, and unselects it. The current image clicked on is then highlighted
		for(int a = 0; a < searchResultsList.size(); a++) {
			if(searchResultsTilePane.getChildren().get(a).getStyle() != null) {
				searchResultsTilePane.getChildren().get(a).setStyle(null);
			}
		}

		this.currentSelectedImage = i;
		bp.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(126,10,250,1), 10, 0, 0, 0);");

		for(int b = 0; b < searchResultsList.size(); b++) {
			if(searchResultsTilePane.getChildren().get(b).getStyle() != "") {
				this.photoClicked = searchResultsList.get(b);
				//System.out.println("Photo LOCATION IS: " + this.photoClicked.getPhotoLocation());
			}
		}
	}

	/**
	 * Creates an album out of the search results and loads the stage associated 
	 * with the createSearchResultsAlbumController asking the user to name the album
	 * or cancel the creation of the new album
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 */
	@FXML
	public void createAlbum() throws IOException {
		this.successCreateText.setOpacity(0);
		Album newAlbum = new Album("");

		for(Photo p: this.searchResultsList) {
			newAlbum.getPhotoList().add(p);
			updateAlbumDateRange(newAlbum, p);
		}

		try {
			Stage stage;
			Parent root;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/createSearchResultsAlbumView.fxml"));
			root = loader.load();
			stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			createSearchResultsAlbumController ac = loader.getController();
			ac.setAlbumToCreate(newAlbum);
			ac.setCurrentUser(currentUser);
			ac.start(stage);
			stage.centerOnScreen();
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(this.currentUser.getAlbumList().contains(newAlbum)) {
			this.successCreateText.setOpacity(1);
		}
	}

	/**
	 * This method updates the date range of photos associtated with the album argument passed in. 
	 * If a photo is added and the Album's start or end dates are null, they are set to the photo object's last modified
	 * date.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param a - the album the date is to be updated to
	 * 
	 * @param newPhoto - this is set to the photo object being copied to the new album created
	 * 					its date range is then modified
	 * 
	 * 
	 */
	private static void updateAlbumDateRange(Album a, Photo newPhoto) {
		if(a.getPhotoList().isEmpty()) {
			a.setEarliestDate(null);
			a.setLatestDate(null);
			return;
		}

		if(a.getEarliestDate() == null) {
			a.setEarliestDate(newPhoto.getLastModifiedDate());
		}

		if(a.getLatestDate() == null) {
			a.setLatestDate(newPhoto.getLastModifiedDate());
		}

		for(Photo p: a.getPhotoList()) {
			if(p.getLastModifiedDate().before(a.getEarliestDate())) {
				a.setEarliestDate(p.getLastModifiedDate());
			}
			if(p.getLastModifiedDate().after(a.getLatestDate())) {
				a.setLatestDate(p.getLastModifiedDate());
			}
		}	
	}

	/**
	 * Loads the stage associated with the UserViewController when the home button is clicked
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	@FXML
	public void onHomeButtonClicked() throws IOException {
		this.successCreateText.setOpacity(0);
		Stage stage = null;
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserView.fxml"));
		root = loader.load();
		stage = (Stage)this.homeButton.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		UserViewController uvc = loader.getController();
		uvc.setCurrentUser(currentUser);
		uvc.start(stage);
		stage.centerOnScreen();
		stage.show();
	}

	/**
	 * Loads the stage associated with the SlideShowViewController when the slideshow button is clicked
	 * and displays a slideshow of the images
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	@FXML
	public void onSlideshowButtonClicked() {
		this.successCreateText.setOpacity(0);
		try//to set up the stage 
		{
			Stage stage;
			Parent root;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SlideShowView.fxml"));
			root = loader.load();
			stage = (Stage)this.slideshowButton.getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			SlideShowViewController ssc = loader.getController();
			ssc.setCurrentAlbum(null);
			ssc.setCurrentUser(currentUser);
			ssc.setSearchResultPhotos(searchResultsList);
			ssc.start(stage);
			stage.centerOnScreen();
			stage.show();

		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the stage associated with the PhotoViewController when the view photo button is clicked
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	@FXML
	public void onViewPhotoButtonClicked() {
		this.successCreateText.setOpacity(0);
		//System.out.println("View Photo Button Clicked\n");
		if(this.photoClicked == null) {
			//System.out.println("No photo selected.");
			errorMessage.setText("No Photo Selected: Please select a photo to view it");
			errorMessage.setOpacity(1);
			errorMessage.setFill(Color.RED);
			return;
		}

		Album selectedImageAlbum = null;
		for(Album a: this.currentUser.getAlbumList()) {
			if(a.getAlbumName().compareTo(this.photoClicked.getAlbumName()) == 0) {
				selectedImageAlbum = a;
			}
		}
		if(selectedImageAlbum != null) {
			try//to set up the stage 
			{
				//No image selected
				if(currentSelectedImage == null) {
					errorMessage.setText("No Photo Selected: Please select a photo to view it");
					errorMessage.setFill(Color.RED);
					errorMessage.setOpacity(1);
					return;
				}
				Stage stage;
				Parent root;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));
				root = loader.load();
				Scene scene = new Scene(root);
				stage = new Stage();
				stage.setScene(scene);
				PhotoViewController pvc = loader.getController();
				pvc.setCurrentUser(currentUser);

				// Find the current album of the selected photo
				pvc.setPhotoClicked(photoClicked);
				pvc.setCurrentAlbum(selectedImageAlbum);
				pvc.setImageView(currentSelectedImage);
				pvc.setSearchResultPhotoView(true);
				pvc.setSearchResultsList(this.searchResultsList);
				pvc.start(stage);
				stage.centerOnScreen();
				stage.show();

			}catch (IOException e) {
				e.printStackTrace();
			}

		}
		else {
			//System.out.println("Can't view search result image, nothing selected.");
		}
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
	 * returns an arraylist of the photos associated with the search results
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return searchResultsList list of photos resulted from a search for tags or date range
	 * 
	 */
	public ArrayList<Photo> getSearchResultsList() {
		return searchResultsList;
	}

	/**
	 * sets the searhResultsList for the photos associated with the search results
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param searchResultsList - list of search results 
	 * 
	 */
	public void setSearchResultsList(ArrayList<Photo> searchResultsList) {
		this.searchResultsList = searchResultsList;
	}

	/**
	 * returns an arraylist of the photos associated with the searched tags
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return searchedTags list of tags searched for
	 * 
	 */
	public ArrayList<Photo> getSearchedTags() {
		return searchedTags;
	}

	/**
	 * sets the arraylist for the photos associated with the searched tags
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param searchedTags - list photos associated with the tags searched for by the user
	 * 
	 */
	public void setSearchedTags(ArrayList<Photo> searchedTags) {
		this.searchedTags = searchedTags;
	}

	/**
	 * returns a String object that represents the date range of the search results
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return String date range string representation
	 * 
	 */
	public String getDateRange() {
		return dateRange;
	}

	/**
	 * sets the String object that represents the date range of the search results
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param dateRange - String object that represents the date range of the search results
	 * 
	 */
	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}
}
