package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import model.Album;
import model.Photo;
import model.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * This is the controller for the slideshow view.
 * 
 * The user can go into slideshow view from either album view or the search results view.
 * 
 * This view just allows the user to cycle through their photos manually to get a better view of them.
 * 
 * @author Brian Schillaci
 *
 */
public class SlideShowViewController {

	// Button that will show the next image in slideshow
	@FXML
	private Button nextButton;

	@FXML
	private Button logoutButton;
	
	@FXML
	private HBox hbox;

	// Button that will show the next image in slideshow
	@FXML
	private Button prevButton;

	// Image view for the current image being shown in the slideshow
	@FXML
	private ImageView slideShowImageView;

	@FXML
	private Button backButton;

	// Current album of the slideshow
	private Album currentAlbum;

	// Current list of search result photos for slideshow
	private ArrayList<Photo> searchResultPhotos;

	// Current user logged in
	private User currentUser;

	// Iterator for list of slideshow images
	ListIterator<Image> itr;

	// Current image shown in the slideshow
	Image currentImage;

	// List of images in the album that can be shown in the slideshow
	LinkedList<Image> imageSlideList;

	// Current index of the slideshow list
	int currentIndex;

	/**
	 * 
	 * 
	 * Function run at the start of the slideshow view life cycle.
	 * Sets up the image view and list of images also has options for when the next and prev buttons are pressed.
	 * 
	 * @author Brian Schillaci
	 * 
	 * @param stage Slideshow View
	 */
	public void start(Stage stage) {
		stage.setTitle("Slideshow View");
		imageSlideList = new LinkedList<Image>();

		//Slideshow for an album
		if(currentAlbum != null) {
			if(this.currentAlbum.getPhotoList().size() <= 1) {
				this.prevButton.setDisable(true);
				this.nextButton.setDisable(true);
			}

			// Set current index to beginning of the list
			currentIndex = 0;
			// Copy all photos of this album into a doubly linked list
			for(Photo p: currentAlbum.getPhotoList()) {
				Image temp = new Image((new File(p.getPhotoLocation()).toURI().toString()));
				imageSlideList.add(temp);
			}
		}
		// Slideshow for search results
		else {
			if(this.searchResultPhotos.size() <= 1) {
				this.prevButton.setDisable(true);
				this.nextButton.setDisable(true);
			}

			// Set current index to beginning of the list
			currentIndex = 0;
			// Copy all photos of this album into a doubly linked list
			for(Photo p: searchResultPhotos) {
				Image temp = new Image((new File(p.getPhotoLocation()).toURI().toString()));
				imageSlideList.add(temp);
			}
		}

		// Create an iterator for the list
		itr = imageSlideList.listIterator();

		currentImage = itr.next();

		// Set the image view to the first image in the album
		slideShowImageView.setImage(currentImage);

		centerImage(slideShowImageView);


		// Handles case where the user just clicks on X to close the window
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				//System.out.println("Slideshow view closed");
				DataController.getInstance().saveUsers();
			}
		});

		// User clicked on the previous button
		prevButton.setOnAction(event -> prevPhoto(prevButton));

		// User clicked on the next button
		nextButton.setOnAction(event -> nextPhoto(nextButton));

		backButton.setOnAction(event -> backButton(backButton));
		
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
	 * Takes in an imageview and centers the image that that contains.
	 * 
	 * @param imageV image view that needs its image to be centered
	 */
	public static void centerImage(ImageView imageV) {
		Image img = imageV.getImage();

		if (img != null) {
			double width = 0;
			double height = 0;

			double x = imageV.getFitWidth() / img.getWidth();
			double y = imageV.getFitHeight() / img.getHeight();

			double rc = 0;
			if(x >= y) {
				rc = y;
			} else {
				rc = x;
			}

			height = img.getHeight() * rc;
			width = img.getWidth() * rc;

			imageV.setX((imageV.getFitWidth() - width) / 2);
			imageV.setY((imageV.getFitHeight() - height) / 2);

		}
	}

	/**
	 * 
	 * Goes back to the stage for the SearchViewController or the AlbumViewController based
	 * on which view the slideshow button was accessed from
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param button button to move back to previous page
	 * 
	 */
	void backButton(final Button button) {
		if(this.currentAlbum != null) {
			try//to set up the stage 
			{
				Stage stage;
				Parent root;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumView.fxml"));
				root = loader.load();
				stage = (Stage)this.backButton.getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				AlbumViewController ac = loader.getController();
				ac.setCurrentUser(currentUser);
				ac.setCurrentAlbum(currentAlbum);
				ac.start(stage);
				stage.centerOnScreen();
				stage.show();

			}catch (IOException e) {
				e.printStackTrace();
			}	
		}
		else {
			// Open up a search view here and fill in the results
			try {
				Stage stage = null;
				Parent root;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchView.fxml"));
				root = loader.load();
				stage = (Stage)this.backButton.getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				SearchViewController svc = loader.getController();
				svc.setCurrentUser(currentUser);
				svc.setSearchResultsList(this.searchResultPhotos);
				svc.start(stage);
				stage.centerOnScreen();
				stage.show();
			}
			catch(IOException e) {
				e.printStackTrace();
			}


		}
	}

	/**
	 * Function that handles next button being pressed. Shows the next image in slideshow.
	 * 
	 * @author Brian Schillaci
	 * @param button Next image button
	 */
	void nextPhoto(final Button button) {
		Image next;
		Image currentTemp = currentImage;

		// If the current index is at the end of the list, we need to move the iterator to the front
		if(currentIndex == imageSlideList.size() - 1) {
			itr = imageSlideList.listIterator(0);
		}

		// Set the next image
		next = itr.next();

		// To avoid having the same photo appear when there are alternating .next() and .previous() calls
		if(next.equals(currentTemp)) {
			next = itr.next();
		}

		// Set current image to the next image
		currentImage = next;

		// Set current index, if at the end, move to 0, else just add 1 to it
		if(currentIndex == imageSlideList.size()-1) {
			currentIndex = 0;
		}
		else {
			currentIndex++;
		}

		// Setting the imageView to the next image
		slideShowImageView.setImage(currentImage);

		centerImage(slideShowImageView);
	}

	/**
	 * Function that handles prev button being pressed. Shows the previous image in slideshow.
	 * 
	 * @author Brian Schillaci
	 * @param button Prev image button
	 */
	void prevPhoto(final Button button) {
		Image prev;
		Image currentTemp = currentImage;

		// If the current index is at the beginning of the list, we need to move the iterator to the back
		if(currentIndex == 0) {
			prev = imageSlideList.getLast();
			itr = imageSlideList.listIterator(imageSlideList.size()-1);
		}
		else {
			prev = itr.previous();
		}

		// To avoid having the same photo appear when there are alternating .next() and .previous() calls
		if(prev.equals(currentTemp)) {
			prev = itr.previous();
		}

		// Set the current image to the previous image
		currentImage = prev;

		// Set current index, if at the end, move to 0, else just add 1 to it
		if(currentIndex == 0) {
			currentIndex = imageSlideList.size()-1;
		}
		else {
			currentIndex--;
		}

		// Setting the imageView to the next image
		slideShowImageView.setImage(currentImage);

		centerImage(slideShowImageView);
	}

	// ------------------ Getters and setters ------------------

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
	 * returns an arraylist of photos based on the search results
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return searchResultPhotos list of search result photos
	 * 
	 */
	public ArrayList<Photo> getSearchResultPhotos() {
		return searchResultPhotos;
	}

	/**
	 * sets an arraylist of photos based on the search results
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param searchResultPhotos - arraylist of photos based on the search results
	 * 
	 */
	public void setSearchResultPhotos(ArrayList<Photo> searchResultPhotos) {
		this.searchResultPhotos = searchResultPhotos;
	}
}