package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.customImage;

/**
 * This class is the controller for the album view. This view is opened when a user opens an album.
 * It will display album thumbnails and allow the user to select photos in the album to display them, add caption, add tags,
 * move photos, copy photos, delete photos, and show a manual slide show of photos
 * @author Brian Schillaci
 * @author Kevin Jamunar
 *
 */
public class AlbumViewController {

	private User currentUser;

	// This is the album the was opened in the userViewController
	private Album currentAlbum;

	public Stage viewPhotoStage;

	// Goes back to the list of albums
	@FXML
	private Button albumsBackButton;

	//Goes to the slideshow view
	@FXML
	private Button slideshowButton;

	//Displays the photo in a new scene
	@FXML
	private Button viewPhotoButton;

	//uploads valid photo files from valid paths
	@FXML
	private Button uploadPhotoButton;

	//Displays the name of the album on the top of the scene
	@FXML
	private Text albumNameText;

	//TilePane holds images and captions in the form of BorderPane children
	@FXML
	private TilePane photosTilePane;

	//Holds the reference to the photo clicked in the TilePane 
	private Photo photoClicked;

	//Deletes the photo seleced by the user from the TilePane
	@FXML
	private Button deletePhotoButton;

	//Adds a tag to the photo selected by the user from the TilePane
	//Holds the format of tagName and tagValue
	@FXML
	private Button tagPhotoButton;

	//Removes a tag from the photo selected by the user from the TilePane
	@FXML
	private Button removeTagButton;

	//Captions the photo in the selected from the TilePane
	@FXML
	private Button captionPhotoButton;

	//Copies the photo object to the same album or another album
	@FXML
	private Button copyPhotoButton;

	//Moves the photo object from one album to another album
	@FXML
	private Button movePhotoButton;

	@FXML
	private Button leftButton;

	@FXML
	private Button rightButton;

	//Displays the image selected from the TilePane on the same scene
	@FXML
	private ImageView imageView;

	//Centers the image displayed from the TilePane
	@FXML
	private BorderPane photoPane;

	//Text representation of all confirmation messages,errors,advice
	@FXML
	private Text confirmationText;

	//TextField for the captionPhotoButton
	@FXML
	private TextField photoTextField;

	//Text above the display of the selected image in the TilePane
	//Displays the caption for the image
	@FXML
	private Text captionText;

	//TextField to hold the tagName entered
	@FXML		
	private TextField tagNameTextField;		

	//TextField to hold the TagValue entered
	@FXML		
	private TextField tagValueTextField;

	//ListView that holds all the albums associated with a user
	@FXML
	private ListView<String> albumListView;

	//ListView that holds all tags for a photo in a given album

	@FXML		
	private ListView<String> tagListView;

	//Holds the tags for a photo
	ObservableList<String> tags = FXCollections.observableArrayList();

	@FXML
	private Button logoutButton;


	// Iterator for list of slideshow images
	ListIterator<Image> itr;

	// List of images in the album that can be shown in the slideshow
	List<Image> imageSlideList;

	List<customImage> customImageSlideList;

	// Current index of the slideshow list
	int currentIndex;

	ImageView currentSelectedImage = null;

	Image currentImage = null;
	
	/**
	 * moves the slideshow left
	 */
	@FXML
	public void left() throws IOException {
		Image prev;
		//setCurrentSelectedImage(bp, imageView);

		//System.out.println(currentIndex);

		// Set current index, if at the end, move to 0, else just add 1 to it
		if(currentIndex <= 0) {
			currentIndex = imageSlideList.size()-1;
		}
		else {
			currentIndex--;
		}

		prev = imageSlideList.get(currentIndex);

		// Set the current image to the previous image
		currentImage = prev;

		// Need to select this image on the left side, in album list
		setCurrentSelectedImage(customImageSlideList.get(currentIndex).getBp(), customImageSlideList.get(currentIndex).getIv());


		// Setting the imageView to the next image
		imageView.setImage(currentImage);
	}

	/**
	 * moves the slideshow right
	 */
	@FXML
	public void right() throws IOException {
		Image next;

		//System.out.println("CI bef:" + currentIndex);
		// If the current index is at the end of the list, we need to move the iterator to the front
		if(currentIndex >= imageSlideList.size() - 1) {
			currentIndex = 0;
		}
		else {
			currentIndex++;
		}

		//System.out.println("CI aft ifelse:" + currentIndex);
		// Set the next image
		next = imageSlideList.get(currentIndex);

		// Set current image to the next image
		currentImage = next;

		// Need to select this image on the left side, in album list
		setCurrentSelectedImage(customImageSlideList.get(currentIndex).getBp(), customImageSlideList.get(currentIndex).getIv());

		//System.out.println("CI aft selecting:" + currentIndex);

		// Setting the imageView to the next image
		imageView.setImage(currentImage);
	}


	/**
	 * The selected image is matched to the photo object it represents and the color of the selected
	 * image(Held inside the BorderPane of the TilePane) is highlighted. All tags for the selected photo
	 * are then displayed to the Tags ListView.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param bp BorderPane parameter represents the child node of the TilePane
	 * @param i ImageView parameter represents the ImageView object stored inside the BorderPane.
	 * 
	 */
	private void setCurrentSelectedImage(BorderPane bp, ImageView i) {

		viewPhotoButton.setDisable(false);
		enableButtons();

		//checks if a previous image is selected, and unselects it. The current image clicked on is then highlighted
		for(int a=0; a<currentAlbum.getPhotoList().size(); a++) {
			if(photosTilePane.getChildren().get(a).getStyle() != null) {
				photosTilePane.getChildren().get(a).setStyle(null);
			}
		}

		this.currentSelectedImage = i;

		this.currentImage = i.getImage();


		//higlights the image selected from the TilePane in purple
		bp.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(126,10,250,1), 10, 0, 0, 0);");

		int b=0;
		//sets the photo clicked to the one that is selected by the user.
		for(b = 0; b < currentAlbum.getPhotoList().size(); b++) {
			if(photosTilePane.getChildren().get(b).getStyle() != "") {
				this.photoClicked = currentAlbum.getPhotoList().get(b);
				this.currentIndex = b;
				break;
			}
		}


		setImageView(i); //sets the image on the stage from the image selected in the list.


		captionText.setText(photoClicked.getPhotoCaption());
		tags.clear();

		confirmationText.setOpacity(0);

		for(int k=0; k < photoClicked.getPhotoTags().size(); k++) {		
			tags.add("(\"" + photoClicked.getPhotoTags().get(k).getName() + "\"" + ", " + "\"" + photoClicked.getPhotoTags().get(k).getValue() + "\")");		
		}		
		tagListView.setItems(tags);		
		tagListView.getSelectionModel().select(0);	
		this.enableButtons();

		if(this.currentAlbum.getPhotoList().size() <= 1) {
			this.leftButton.setDisable(true);
			this.rightButton.setDisable(true);
			//System.out.println("got it");
		}
		else {
			this.leftButton.setDisable(false);
			this.rightButton.setDisable(false);
		}
	}


	/**
	 * 
	 * This method executes when the stage is loaded. 
	 * All buttons are disabled and messages are set up
	 * to prompt the user on what actions to perform. 
	 * ImageView Objects are created for all photos inside of the album
	 * selected by the user on the UserView Stage. All the imageView objects
	 * are then combined with Labels that are used to represent the captions.
	 * The combination of Label and ImageView objects are then stored insided of a
	 * Border Pane an loaded onto the TilePane.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param stage Stage 
	 * 
	 * 
	 */
	public void start(Stage stage) {
		//If the TilePane list is not empty but no photo is selected, all appropriate buttons are disabled

		if(photoClicked == null && currentAlbum.getPhotoList().size() != 0) {
			disableButtons();
			viewPhotoButton.setDisable(true);
			confirmationText.setFill(Color.GREEN);
			confirmationText.setText("Select a photo from the list.");
			confirmationText.setOpacity(1);
		}

		//If the TilePane list is empty, all appropriate buttons are disabled
		else if(currentAlbum.getPhotoList().size() == 0) {

			disableButtons();
			viewPhotoButton.setDisable(true);
			slideshowButton.setDisable(true);
			confirmationText.setFill(Color.GREEN);
			confirmationText.setText("Upload photo(s) to your album.");
			confirmationText.setOpacity(1);
		}

		customImageSlideList = new ArrayList<customImage>();

		for(int i=0; i<currentAlbum.getPhotoList().size(); i++) {

			Image image = new Image((new File(currentAlbum.getPhotoList().get(i).getPhotoLocation()).toURI().toString()));
			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(150);
			imageView.setFitWidth(150);
			imageView.setPreserveRatio(true);
			BorderPane bp = new BorderPane(imageView, new Label( currentAlbum.getPhotoList().get(i).getPhotoCaption()), null, null, null);
			bp.setOnMouseClicked(e->setCurrentSelectedImage(bp, imageView));

			customImage a = new customImage(bp, imageView, image);
			customImageSlideList.add(a);

			//photosTilePane.setBorder(new Border(new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));		
			photosTilePane.getChildren().add(bp);			
		}

		albumNameText.setText(currentAlbum.getAlbumName());

		stage.setTitle("Album View");

		// Setup the manual slideshow
		// ------------------------------

		imageSlideList = new ArrayList<Image>();

		// Set current index to beginning of the list
		currentIndex = 0;
		// Copy all photos of this album into a doubly linked list
		for(Photo p: currentAlbum.getPhotoList()) {
			Image temp = new Image((new File(p.getPhotoLocation()).toURI().toString()));
			imageSlideList.add(temp);
		}


		// ------------------------------

		//Event that happens when the user clicks the X to close album view
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				// Save the user's changes
				DataController.getInstance().saveUsers();
			}
		});
	}

	/**
	 * Logs out the current user and loads the AlbumViewController stage
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	@FXML
	public void onLogoutButtonClicked() throws IOException {

		if(this.viewPhotoStage != null) {
			if(this.viewPhotoStage.isShowing()) {
				viewPhotoStage.close();
			}
		}

		//System.out.println("logout button clicked");
		this.confirmationText.setOpacity(0);
		DataController.getInstance().saveUsers();
		Stage stage = null;
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
		root = loader.load();
		stage = (Stage)this.logoutButton.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		LoginViewController lvc = loader.getController();
		lvc.start(stage);
		stage.centerOnScreen();
		stage.show();

	}



	/**
	 * 
	 * This method is executed when the delete button is clicked.
	 * An alert is sent out to user confirming that they want to delete the 
	 * selected photo. If the user clicks "OK" on the confirmation box, the photo
	 * is removed from the album and all appropriate buttons are disabled.
	 * The TilePane is then refreshed with the remaining photos in the Album
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 */
	@FXML
	public void onDeletePhotoButtonClicked() {
		/*
		 * sends out an alert and removes the photo from the list.
		 */
		confirmationText.setOpacity(0);
		//alert sent out to confirm if the user wants to delete the photo
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Photo Deletion Confirmation");
		alert.setHeaderText("Photo Deletion");
		alert.setContentText("Are you sure you want to delete this photo?");
		Optional<ButtonType> reply = alert.showAndWait();

		/*
		 *if the user clicks "OK" on the alertbox, the current album is searched 
		 *for the photo object and deletes that object.
		 */
		if((reply.isPresent()) && (reply.get() == ButtonType.OK)) {
			for(int i=0; i<currentAlbum.getPhotoList().size(); i++) {

				if(currentAlbum.getPhotoList().get(i).equals(photoClicked)){
					Photo toRemove = currentAlbum.getPhotoList().get(i);
					currentAlbum.getPhotoList().remove(i);
					imageSlideList.remove(i);

					if(currentAlbum.getPhotoList().size() == 0) {
						disableButtons();
						viewPhotoButton.setDisable(true);
						slideshowButton.setDisable(true);
					}

					//Updating album date range after deleting the photo
					AlbumViewController.updateAlbumDateRange(currentAlbum, null, toRemove);

					confirmationText.setFill(Color.BLUE);
					confirmationText.setText("Photo Successfully Deleted");
					confirmationText.setOpacity(1);

					//disables buttons when the photo is deleted until the user selects an image from the TilePane
					disableButtons(); 
					imageView.setImage(null);
					this.captionText.setText(null);
					this.tags.clear();
					viewPhotoButton.setDisable(true);

					photosTilePane.getChildren().clear();
					customImageSlideList.clear();

					for(int j=0; j<currentAlbum.getPhotoList().size(); j++) {
						Image image = new Image((new File(currentAlbum.getPhotoList().get(j).getPhotoLocation()).toURI().toString()));
						ImageView imageView = new ImageView(image);
						imageView.setFitHeight(150);
						imageView.setFitWidth(150);
						imageView.setPreserveRatio(true);
						BorderPane bp = new BorderPane(imageView, new Label( currentAlbum.getPhotoList().get(j).getPhotoCaption()), null, null, null);
						bp.setOnMouseClicked(e->setCurrentSelectedImage(bp, imageView));

						customImage a = new customImage(bp, imageView, image);
						customImageSlideList.add(a);

						//photosTilePane.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
						photosTilePane.getChildren().add(bp);			
					}
				}
			}

			this.photoClicked = null;

		}
		DataController.getInstance().saveUsers();
	}

	/**
	 * 
	 * This method is executed when the add tag button is clicked.
	 * The tag text fields are checked to make sure there are values
	 * in both fields before the tag is added to the list of tags for the
	 * selected photo. If the tags entered by the user match a tag already 
	 * asscociated with the photo, that tag is rejected.
	 * All text fields are cleared when the user clicks "Add Tag"
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	@FXML
	public void onTagPhotoButtonClicked() {
		/*
		 * checks if the text fields for tagName and TagValue are empty.
		 * If the textfields are empty, display an error message and return from the function.
		 */
		if(tagNameTextField.getText().trim().isEmpty() || tagValueTextField.getText().trim().isEmpty()) {
			confirmationText.setText("Fill out both text fields on the right to add a tag");
			confirmationText.setOpacity(1);
			confirmationText.setFill(Color.RED);
			return;
		}
		/*
		 * Outer loop matches the photo selected to a matching photo object in the 
		 * photo list of the current album
		 */
		for(int i=0; i<currentAlbum.getPhotoList().size(); i++) {

			Photo currentPhoto = currentAlbum.getPhotoList().get(i);
			if(currentPhoto.equals(photoClicked)) {
				/*
				 * If the tag the user is trying to add matches the name,value pair of a 
				 * another tag on the photo, the tag is rejected.
				 * Otherwise the tag is added to the photo and the listview is updated
				 */

				if(currentPhoto.getPhotoTags().size() != 0) {
					for(int j=0; j<currentPhoto.getPhotoTags().size(); j++) {

						String nameTag = currentPhoto.getPhotoTags().get(j).getName();
						String valueTag = currentPhoto.getPhotoTags().get(j).getValue();

						if(nameTag.equals(tagNameTextField.getText()) && valueTag.equals(tagValueTextField.getText())) {
							confirmationText.setFill(Color.RED);
							confirmationText.setText("Photo Tag already exists");
							confirmationText.setOpacity(1);

							//clears text fields if the user enters a tag that already exists
							photoTextField.clear(); 
							tagValueTextField.clear();
							tagNameTextField.clear();
							return;
						}
					}
				}
				/*
				 * If the tag the user added is unique, the tagName, tagValue and tagListRepresentation
				 * are then added to the list of tags for that photo
				 * and the listview is updated.
				 */
				Tag tempTag = new Tag(tagNameTextField.getText() ,tagValueTextField.getText());
				tempTag.setListRepresentation("(\"" + tempTag.getName() + "\"" + ", " + "\"" + tempTag.getValue() + "\")");
				currentPhoto.getPhotoTags().add(tempTag);
				confirmationText.setFill(Color.BLUE);
				confirmationText.setText("Photo Successfully Tagged");
				confirmationText.setOpacity(1);

				tags.add("(\"" + tempTag.getName() + "\"" + ", " + "\"" + tempTag.getValue() + "\")");
				tagListView.setItems(tags);

				tagListView.getSelectionModel().select(tags.size()-1);
			}
		}
		//System.out.println("TagPhoto button clicked");
		//clears all textfields
		photoTextField.clear(); 
		tagValueTextField.clear();
		tagNameTextField.clear();

		DataController.getInstance().saveUsers();
	}


	/**
	 * 
	 * This method is executed when the remove tag button is clicked.
	 * The list of tags on the selected photo is searched for the tag
	 * selected on the tag list ListView. When the tag is found, it is removed
	 * from the list of tags for that photo and the ListView is refreshed 
	 * with the updated tag list. All text fields are also cleared of any input.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	@FXML
	public void onRemoveTagButtonClicked() {
		if(this.tags.isEmpty()) {
			confirmationText.setFill(Color.RED);
			confirmationText.setText("Tag list is empty");
			confirmationText.setOpacity(1);
		}

		for(int i=0; i<currentAlbum.getPhotoList().size(); i++) {

			if(currentAlbum.getPhotoList().get(i).equals(photoClicked)) {
				/*
				 * Searches the current photo for the tag the user is trying to remove.
				 * When the tag the user is trying to remove is found, that tag is then deleted
				 * from the photo's tags.
				 */
				if(currentAlbum.getPhotoList().get(i).getPhotoTags().size() != 0) {

					for(int j=0; j<currentAlbum.getPhotoList().get(i).getPhotoTags().size(); j++) {

						if(currentAlbum.getPhotoList().get(i).getPhotoTags().get(j).getListRepresentation().equals(tagListView.getSelectionModel().getSelectedItem())) {

							tags.remove(j);
							currentAlbum.getPhotoList().get(i).getPhotoTags().remove(j);
							tagListView.setItems(tags);

							//selects where to position the selector after removing the tag
							if(tags.size()-1 == j-1) {
								tagListView.getSelectionModel().select(j-1);
							}
							else {
								tagListView.getSelectionModel().select(j);
							}
							confirmationText.setFill(Color.BLUE);
							confirmationText.setText("Photo Tag successfully deleted");
							confirmationText.setOpacity(1);

							break;
						}
					}
				}
			}
		}

		//clears all textfields
		photoTextField.clear(); 
		tagValueTextField.clear();
		tagNameTextField.clear();
		DataController.getInstance().saveUsers();
	}


	/**
	 * 
	 * This method is executed when the caption photo button is clicked.
	 * The text field associated with the CaptionPhoto button is checked
	 * for input. If there is no input, an error message is generated.
	 * If there is input, the photo object's caption is set to the 
	 * information in the text field and a success message is generated.
	 * The TilePane is then refreshed to display the new caption for that photo.
	 * All text fields are cleared.
	 * 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 */
	@FXML
	public void onCaptionPhotoButtonClicked() {
		/*
		 * checks if the text field is empty.
		 * If the textfield is empty, display an error message and return from the function.
		 */
		if(photoTextField.getText().trim().isEmpty()) {
			confirmationText.setText("Cannot add an empty caption");
			confirmationText.setOpacity(1);
			confirmationText.setFill(Color.RED);
			return;
		}

		/*
		 * If the user clicks "OK" in the alertbox, the current album is searched
		 * for the photo that the user wants to caption. When the photo is found,
		 * the caption is added to that photo and a success message in blue is displayed.
		 */
		for(int i=0; i<currentAlbum.getPhotoList().size(); i++) {
			if(currentAlbum.getPhotoList().get(i).equals(photoClicked)){

				currentAlbum.getPhotoList().get(i).setPhotoCaption(photoTextField.getText());
				captionText.setText(currentAlbum.getPhotoList().get(i).getPhotoCaption());

				confirmationText.setFill(Color.BLUE);
				confirmationText.setText("Photo Successfully Captioned");
				confirmationText.setOpacity(1);

			}
		}

		photosTilePane.getChildren().clear();

		for(int j=0; j<currentAlbum.getPhotoList().size(); j++) {

			ImageView imageView = new ImageView(new Image((new File(currentAlbum.getPhotoList().get(j).getPhotoLocation()).toURI().toString())));
			imageView.setFitHeight(150);
			imageView.setFitWidth(150);
			imageView.setPreserveRatio(true);
			BorderPane bp = new BorderPane(imageView, new Label( currentAlbum.getPhotoList().get(j).getPhotoCaption()), null, null, null);
			bp.setOnMouseClicked(e->setCurrentSelectedImage(bp, imageView));

			//photosTilePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			photosTilePane.getChildren().add(bp);			
		}

		//clears all textfields
		photoTextField.clear(); 
		tagValueTextField.clear();
		tagNameTextField.clear();

		DataController.getInstance().saveUsers();
	}


	/**
	 * 
	 * This method is executed when the copy photo button is clicked.
	 * The ListView of albums are loaded onto a new Stage for the user
	 * to select which album they want to copy the selected photo to.
	 * The entire object is copied with all the values from the original photo object.
	 * The TilePane is then refreshed to accomodate if the photo was copied
	 * to the same album. The stage is then closed
	 *
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 *
	 */
	@FXML
	public void onCopyPhotoButtonClicked() {

		String listViewAlbumName = null; //holds the selected album from the listview
		SelectAlbumViewController slvc = null;

		// open up a selectalbum view window to select which album name to copy this 
		// current selected photo into
		try {
			Stage stage;
			Parent root;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectAlbumView.fxml"));
			root = loader.load();
			stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			slvc = loader.getController();
			slvc.setCurrentUser(currentUser);
			slvc.setAlbumList(this.currentUser.getAlbumList());
			slvc.setAlbumSelected(null);
			slvc.setTitle("Select an album to copy this photo to:");
			slvc.start(stage);
			stage.centerOnScreen();
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// If the user doesn't select anything, just return
		if(slvc.getAlbumSelected() == null) {
			//System.out.println("No album selected");
			return;
		}

		// Set listViewAlbumName to the selection of the album selector window
		listViewAlbumName = slvc.getAlbumSelected();

		/*
		 * Searches the album list to find the album that matches the one the user selects in the list.
		 * When the album is found, a tempPhoto reference is created that holds a clone
		 * of the photo the user is trying to copy. The copy is then copied to the album the user selected
		 * in the listview.
		 */
		for(int i=0; i<currentUser.getAlbumList().size(); i++) {
			Album albumToCopyTo = currentUser.getAlbumList().get(i);
			if(albumToCopyTo.getAlbumName().equals(listViewAlbumName)){
				Photo tempPhoto = new Photo(photoClicked,listViewAlbumName);
				albumToCopyTo.getPhotoList().add(tempPhoto); 
				Image ii = new Image((new File(tempPhoto.getPhotoLocation()).toURI().toString()));
				// Add this new image to the imageSlideList, if the photo is being copied to the same album
				if(albumToCopyTo.getAlbumName().compareTo(this.currentAlbum.getAlbumName()) == 0) {
					imageSlideList.add(ii);
				}
				AlbumViewController.updateAlbumDateRange(albumToCopyTo, tempPhoto, null);

				confirmationText.setText("Photo Successfully Copied");
				confirmationText.setOpacity(1);
				confirmationText.setFill(Color.BLUE);

				photosTilePane.getChildren().clear();
				customImageSlideList.clear();

				for(int j=0; j<currentAlbum.getPhotoList().size(); j++) {

					Image image = new Image((new File(currentAlbum.getPhotoList().get(j).getPhotoLocation()).toURI().toString()));
					ImageView imageView = new ImageView(image);
					imageView.setFitHeight(150);
					imageView.setFitWidth(150);
					imageView.setPreserveRatio(true);
					BorderPane bp = new BorderPane(imageView, new Label( currentAlbum.getPhotoList().get(j).getPhotoCaption()), null, null, null);
					bp.setOnMouseClicked(e->setCurrentSelectedImage(bp, imageView));

					customImage a = new customImage(bp, imageView, image);
					customImageSlideList.add(a);

					//photosTilePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
					photosTilePane.getChildren().add(bp);			
				}
			}

		}
		DataController.getInstance().saveUsers();
		
		if(this.currentAlbum.getPhotoList().size() > 1 && this.photoClicked != null) {
			this.leftButton.setDisable(false);
			this.rightButton.setDisable(false);
		}
		
	}


	/**
	 * 
	 * This method is executed when the move photo button is clicked.
	 * The ListView of albums except the current one are loaded onto a new Stage for the user
	 * to select which album they want to move the selected photo to.
	 * The entire object is copied with all the values from the original photo object.
	 * The original object is deleted and the new copy is moved to the selected album.
	 * The TilePane is then refreshed to accomodate the updated photo list.
	 *
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 */
	@FXML
	public void onMovePhotoButtonClicked() {

		if(currentUser.getAlbumList().size() == 1) {
			confirmationText.setText("No other albums to move the selected photo to");
			confirmationText.setOpacity(1);
			confirmationText.setFill(Color.RED);
			return;
		}

		String listViewAlbumName = null; //holds the selected album from the listview
		// Opening a new window to select the album to move to
		SelectAlbumViewController slvc = null;
		try {
			Stage stage;
			Parent root;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectAlbumView.fxml"));
			root = loader.load();
			stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			slvc = loader.getController();
			slvc.setCurrentUser(currentUser);
			// Remove the current album first to prevent user from trying to move a photo to the same album
			this.currentUser.getAlbumList().remove(this.currentAlbum);
			slvc.setAlbumList(this.currentUser.getAlbumList());
			slvc.setAlbumSelected(null);
			slvc.setTitle("Select an album to move this photo to:");
			slvc.start(stage);
			stage.centerOnScreen();
			stage.showAndWait();
			// Add the current album back
			this.currentUser.getAlbumList().add(this.currentAlbum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Handling the case where the user doesn't select an album in the new window
		if(slvc.getAlbumSelected() == null) {
			//System.out.println("No album selected");
			return;
		}

		// Set listViewAlbumName to the selection of the album selector window
		listViewAlbumName = slvc.getAlbumSelected();

		/*
		 * Checks if the user is trying to move a photo to the same album and disallows move.
		 */
		if(currentAlbum.getAlbumName().equals(listViewAlbumName)) {
			//System.out.println("Disallow moving to the same album");
			confirmationText.setText("Cannot move to the same album");
			confirmationText.setOpacity(1);
			confirmationText.setFill(Color.RED);
			return;
		}
		/*
		 * Searches the album list to find the album that matches the one the user selects in the list.
		 * When the album is found, a tempPhoto reference is created that holds a clone
		 * of the photo the user is trying to move. The object is then deleted from its current album 
		 * and moved to the album the user selected in the listview.
		 */
		for(int i=0; i<currentUser.getAlbumList().size(); i++) {
			if(currentUser.getAlbumList().get(i).getAlbumName().equals(listViewAlbumName)){
				Photo clicked = (Photo)photoClicked;
				Photo tempPhoto = new Photo(clicked,listViewAlbumName);

				for(int j=0; j<currentAlbum.getPhotoList().size(); j++) {
					if(currentAlbum.getPhotoList().get(j).equals(photoClicked)){

						Photo movePhoto = currentAlbum.getPhotoList().get(j);

						currentAlbum.getPhotoList().remove(j); //removes the photo object that is being moved from its current album
						imageSlideList.remove(j);
						//photosTilePane.getChildren().remove(j);
						// Update the date range of the original album before the move
						AlbumViewController.updateAlbumDateRange(currentAlbum, null, movePhoto);

						disableButtons();
						viewPhotoButton.setDisable(true);
					}
				}

				// Update the date range of the album the photo is moving to
				AlbumViewController.updateAlbumDateRange(currentUser.getAlbumList().get(i), tempPhoto, null);

				currentUser.getAlbumList().get(i).getPhotoList().add(tempPhoto); 
				confirmationText.setText("Photo Successfully Moved");
				confirmationText.setOpacity(1);
				confirmationText.setFill(Color.BLUE);

				if(currentAlbum.getPhotoList().size() == 0) {
					slideshowButton.setDisable(true);
				}
			}

		}		
		// Update the tile pane with new stuff
		photosTilePane.getChildren().clear();
		customImageSlideList.clear();

		for(int j=0; j<currentAlbum.getPhotoList().size(); j++) {

			Image image = new Image((new File(currentAlbum.getPhotoList().get(j).getPhotoLocation()).toURI().toString()));
			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(150);
			imageView.setFitWidth(150);
			imageView.setPreserveRatio(true);
			BorderPane bp = new BorderPane(imageView, new Label( currentAlbum.getPhotoList().get(j).getPhotoCaption()), null, null, null);
			//imageView.setOnMouseClicked(e->setCurrentSelectedImage(imageView));
			bp.setOnMouseClicked(e->setCurrentSelectedImage(bp, imageView));

			customImage a = new customImage(bp, imageView, image);
			customImageSlideList.add(a);

			//photosTilePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

			photosTilePane.getChildren().add(bp);			
		}

		DataController.getInstance().saveUsers();

		imageView.setImage(null);
		this.captionText.setText(null);
		this.tags.clear();
		this.photoClicked = null;

		if(this.currentAlbum.getPhotoList().size() <= 1) {
			this.leftButton.setDisable(true);
			this.rightButton.setDisable(true);
		}
		
	}

	/**
	 * This method is executed when the Upload Photo button is clicked.
	 * A FileChooser object is created that opens up the list of files on
	 * the system. The user is then allowed to select multiple files via
	 * the showOpenMultiDialog() method. These files are stored into a List.
	 * The list is then checked for files that do do not match an Image format
	 * (BMP, GIF, JPEG, PNG) via the ImageIO class' read function. An error
	 * message is returned if the format violates all of the valid image formats
	 * and the function is returned. If all selected files are valid, a confirmation
	 * box is sent out to confirm the upload. If the user clicks "OK", photo objects are
	 * created from the image information, the photo list is updated, and the TilePane is 
	 * refreshed with the updated information.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 *
	 */
	@FXML
	public void onUploadPhotoButtonClicked() {

		String imageInformation = null;
		confirmationText.setOpacity(0.0);

		FileChooser fc = new FileChooser();
		List<File> files = fc.showOpenMultipleDialog(null);
		if(files == null) {
			//System.err.println("No files selected.");
			return;
		}

		//loops through the files to make sure they are all legitimate images.
		for(int p=0; p<files.size(); p++) {
			imageInformation = files.get(p).getAbsolutePath();
			try {
				if(ImageIO.read(new File(imageInformation)) == null) {
					confirmationText.setText("Invalid file(s) were selected: Upload cancelled");
					confirmationText.setFill(Color.RED);
					confirmationText.setOpacity(1.0);
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}

		try {
			//alert sent out if the format is valid
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Photo Upload Confirmation");
			alert.setHeaderText("Photo Upload");
			alert.setContentText("Are you sure you want to upload the selected photo(s)?");
			//alert's return type is "Optional"
			Optional<ButtonType> reply = alert.showAndWait();

			if((reply.isPresent()) && (reply.get() == ButtonType.OK)) {

				if(photoClicked == null) {
					disableButtons();
					viewPhotoButton.setDisable(true);
					confirmationText.setFill(Color.GREEN);
					confirmationText.setText("Select a photo from the list.");
					confirmationText.setOpacity(1);
				}

				for(int q=0; q<files.size(); q++) {

					//adds and shows the uploaded photos on the tilepane 

					imageInformation = files.get(q).getAbsolutePath();

					//adds the image if the user clicks "OK" in the confirmation box
					for(int i=0; i<currentUser.getAlbumList().size(); i++) {
						if(currentUser.getAlbumList().get(i).getAlbumName() == currentAlbum.getAlbumName()) {
							Photo newPhoto = new Photo(currentAlbum.getAlbumName(), imageInformation);
							currentUser.getAlbumList().get(i).getPhotoList().add(newPhoto);

							// Update the date range of this album after adding this photo
							AlbumViewController.updateAlbumDateRange(currentAlbum, newPhoto, null);
						}
					}
					Image image = new Image((new File(imageInformation).toURI().toString()));
					ImageView currentImageUpload = new ImageView(image);
					imageSlideList.add(image);
					currentImageUpload.setFitHeight(150);
					currentImageUpload.setFitWidth(150);
					currentImageUpload.setPreserveRatio(true);

					BorderPane bp = new BorderPane(currentImageUpload, new Label(null), null, null, null);
					bp.setOnMouseClicked(e->setCurrentSelectedImage(bp, currentImageUpload));

					customImage a = new customImage(bp, currentImageUpload, image);
					customImageSlideList.add(a);

					//photosTilePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

					photosTilePane.getChildren().add(bp);

					if(deletePhotoButton.isDisable()) {
						confirmationText.setFill(Color.GREEN);
						confirmationText.setText("Select a photo from the list.");
						confirmationText.setOpacity(1);
					}
				}
			}
		}catch(NullPointerException e){
			//System.err.println("URL is null / currentUser is null\n");
		}catch(IllegalArgumentException e) {
			//System.err.println("Invalid/Unsupported URL\n");
		}
		
		if(this.currentAlbum.getPhotoList().size() > 1 && this.photoClicked != null) {
			this.leftButton.setDisable(false);
			this.rightButton.setDisable(false);
		}

		DataController.getInstance().saveUsers();
	}

	/**
	 * This method updates the date range of photos associtated with the album argument passed in.
	 * If there are no photos in the album, the start and end dates are set to null. If a photo is 
	 * added and the Album's start or end dates are null, they are set to the photo object's last modified
	 * date. If the photo being removed represents the start or end date associate with the album, that date
	 * is set to null and the photos are looped through to update the null dates.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param a - the album the date is to be updated to
	 * 
	 * @param photoAdded - this is set to the photo object being moved, copied, or uploaded. 
	 * 					   If these actions are not being performed on the photo, the vlaue
	 * 					   is set to null.
	 * 
	 * @param removed -  this is set to the photo object being deleted, or moved.
	 * 					 If these actions are not being performed on the photo, the value 
	 * 					 is set to null.
	 * 
	 */
	public static void updateAlbumDateRange(Album a, Photo photoAdded, Photo removed) {
		// If there are no more photos in the album, set beginning and ending date both to null
		if(a.getPhotoList().isEmpty()) {
			a.setEarliestDate(null);
			a.setLatestDate(null);
			return;
		}

		// Checks for if the album has any null beggining or ending dates.
		if(photoAdded != null) {
			if(a.getEarliestDate() == null) {
				a.setEarliestDate(photoAdded.getLastModifiedDate());
			}

			if(a.getLatestDate() == null) {
				a.setLatestDate(photoAdded.getLastModifiedDate());
			}
		}

		// Check to see if the photo being removed is the beginning or end date or this album - if so remove it
		if(removed != null) {
			if(a.getEarliestDate().compareTo(removed.getLastModifiedDate()) == 0) {
				a.setEarliestDate(null);
			}

			if(a.getLatestDate().compareTo(removed.getLastModifiedDate()) == 0) {
				a.setLatestDate(null);
			}
		}

		// Loop through remaining photos to update beginning and ending date of album
		for(Photo p: a.getPhotoList()) {
			if(a.getEarliestDate() == null) {
				a.setEarliestDate(p.getLastModifiedDate());
			}

			if(a.getLatestDate() == null) {
				a.setLatestDate(p.getLastModifiedDate());
			}

			if(p.getLastModifiedDate().before(a.getEarliestDate())) {
				a.setEarliestDate(p.getLastModifiedDate());
			}
			if(p.getLastModifiedDate().after(a.getLatestDate())) {
				a.setLatestDate(p.getLastModifiedDate());
			}
		}	
	}


	/**
	 * This method is executed when the slideshow button is clicked.
	 * It loads a new stage and makes a call to the SlideShowView Controller.
	 * The photos can then be looped through continuously (If the last photo is reached,
	 * the first photo is displayed again)
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */

	@FXML
	public void onSlideshowButtonClicked() {
		//System.out.println("Slideshow Button Clicked\n");

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
			ssc.setCurrentAlbum(currentAlbum);
			ssc.setCurrentUser(currentUser);
			ssc.start(stage);
			stage.centerOnScreen();
			stage.show();

		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is executed when the view photo button is clicked.
	 * A new stage is loaded and the photo selected by the user is displayed
	 * along with the date the photo was last modified, its caption and the tags 
	 * associated with it.
	 * 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	@FXML
	public void onViewPhotoButtonClicked() {
		try//to set up the stage 
		{
			//No image selected
			if(currentSelectedImage == null) {
				confirmationText.setText("No Photo Selected: Please select a photo to view it");
				confirmationText.setFill(Color.RED);
				confirmationText.setOpacity(1);
				return;
			}

			Parent root;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			viewPhotoStage = new Stage();
			viewPhotoStage.setScene(scene);
			PhotoViewController pvc = loader.getController();
			pvc.setCurrentUser(currentUser);
			pvc.setCurrentAlbum(currentAlbum);
			pvc.setImageView(currentSelectedImage);
			pvc.setSearchResultsList(null);
			pvc.setPhotoClicked(this.photoClicked);
			pvc.setSearchResultPhotoView(false);
			pvc.start(viewPhotoStage);
			viewPhotoStage.centerOnScreen();
			viewPhotoStage.show();

		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is executed when the Album back button is clicked.
	 * The user's view stage is loaded.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	@FXML
	public void onAlbumsBackButtonClicked() {
		//System.out.println("Albums Button Clicked\n");
		try//to set up the stage 
		{
			Stage stage;
			Parent root;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserView.fxml"));
			root = loader.load();
			stage = (Stage)this.albumsBackButton.getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			UserViewController uc = loader.getController();
			uc.setCurrentUser(currentUser);
			uc.start(stage);
			stage.centerOnScreen();
			stage.show();

		}catch (IOException e) {
			e.printStackTrace();
		}	
	}


	/**
	 * 
	 * The disableButtons method disables all buttons
	 * except for the slideshow, update photo, back to home, 
	 * and view photo buttons.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	private void disableButtons() {
		deletePhotoButton.setDisable(true);
		tagPhotoButton.setDisable(true);
		removeTagButton.setDisable(true);
		captionPhotoButton.setDisable(true);
		movePhotoButton.setDisable(true);
		copyPhotoButton.setDisable(true);
		leftButton.setDisable(true);
		rightButton.setDisable(true);
	}

	/**
	 * 
	 * The enableButtons method enables all buttons
	 * except for the slideshow, update photo, back to home, 
	 * and view photo buttons.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	private void enableButtons() {
		deletePhotoButton.setDisable(false);
		tagPhotoButton.setDisable(false);
		removeTagButton.setDisable(false);
		captionPhotoButton.setDisable(false);
		movePhotoButton.setDisable(false);
		copyPhotoButton.setDisable(false);
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
		this.imageView.setImage(imageView.getImage());
	}

}
