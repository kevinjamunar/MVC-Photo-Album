package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * This controller controls the page that opens when the user first logs in.
 * 
 * This page lists all the users albums and metadata about them, and allows the user to:
 * open an album, rename an album, create an album, delete an album, search for tags,
 * search for a date range.
 * 
 * @author Brian Schillaci
 *
 */
public class UserViewController {

	private User currentUser;

	// This is the current selection in the scroll pane
	private Album selectedAlbum;

	// Holds a list view of all albums associated with the current user.
	@FXML
	private ListView<Album> albumListView;

	//Opens the stage associated with the AlbumViewController
	@FXML
	private Button openAlbumButton;

	//Gets the data entered by the user in the newAlbumField text field and creates a new album
	@FXML
	private Button createAlbumButton;

	//opens the SearchViewController displaying photos based on the tag values entered
	@FXML
	private Button tagSearchButton;

	//opens the SearchViewController displaying photos based on the date range specified
	@FXML
	private Button dateRangeSearchButton;

	//sets the start date for the date range 
	@FXML
	private DatePicker startDate;

	//sets the end date for the date range
	@FXML
	private DatePicker endDate;

	//opens the RenameAlbumController
	@FXML
	private Button renameAlbumButton;

	//deletes the album selected in the albumListView
	@FXML
	private Button deleteAlbumButton;

	//logs out the current user
	@FXML
	private Button logoutButton;

	//The TextField used to set the name of a new album the user wants to create
	@FXML
	private TextField newAlbumField;

	//Sets the title for the stage associated with the UserViewController
	@FXML
	private Text userViewTitle;

	//displays text if an album already exists
	@FXML
	private Text errorMessageText;

	//represents the TextField used to search by tags
	@FXML
	private TextField searchField;

	//holds and observable list of all albums associated with a current user
	public static ObservableList<Album> obsList;

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
	 * Disables appropriate buttons*
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	public void disableButtons() {
		openAlbumButton.setDisable(true);
		renameAlbumButton.setDisable(true);
		deleteAlbumButton.setDisable(true);
		dateRangeSearchButton.setDisable(true);
		tagSearchButton.setDisable(true);
	}

	/**
	 * enables appropriate buttons*
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 */
	public void enableButtons() {
		openAlbumButton.setDisable(false);
		renameAlbumButton.setDisable(false);
		deleteAlbumButton.setDisable(false);
		dateRangeSearchButton.setDisable(false);
		tagSearchButton.setDisable(false);
	}

	/**
	 * loads all of the albums associated with the current user logged in.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param stage - opens the stage associated with the current controller 
	 * 
	 */
	public void start(Stage stage) {
		//System.out.println("This is the first window that appears when a user logs in.");

		this.errorMessageText.setOpacity(0);

		if(currentUser.getAlbumList().size() == 0) {
			disableButtons();
			this.errorMessageText.setOpacity(1);
			this.errorMessageText.setText("Create an album to start adding photos");
			this.errorMessageText.setFill(Color.GREEN);
		}

		stage.setTitle("User View");


		this.startDate.getEditor().setDisable(true);

		this.endDate.getEditor().setDisable(true);

		obsList = FXCollections.observableArrayList();

		albumListView.setItems(obsList);

		this.userViewTitle.setText(this.currentUser.getUsername() + "'s Photos");

		if(this.currentUser.getAlbumList() != null) {
			// load all of the albums of current user into listview
			for(Album a: this.currentUser.getAlbumList()) {
				obsList.add(a);
			}
		}
		if(this.currentUser.getAlbumList().size() >= 1) {
			this.albumListView.getSelectionModel().select(0);
		}

		//Event that happens when the user clicks the X to close view 
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				// Save the user's changes
				DataController.getInstance().saveUsers();
			}
		});

	}

	/**
	 * opens the stage associated with the AlbumViewController when the open album button is clicked
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param event - when the user clicks the open album button
	 * 
	 */
	@FXML
	void openAlbum(ActionEvent event) throws IOException {
		this.errorMessageText.setOpacity(0);
		Album selected = albumListView.getSelectionModel().getSelectedItem();
		if(selected == null) {
			this.errorMessageText.setOpacity(1);
			this.errorMessageText.setText("Please select an album to open");
			this.errorMessageText.setFill(Color.RED);
			return;
		}

		Stage stage = null;
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumView.fxml"));
		root = loader.load();
		stage = (Stage)this.openAlbumButton.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		AlbumViewController avc = loader.getController();

		avc.setCurrentAlbum(selected);
		avc.setCurrentUser(currentUser);
		avc.start(stage);
		stage.centerOnScreen();
		stage.show();
	}

	/**
	 * deletes the album selected from the albumListView
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param event - when the user clicks the delete album button
	 * 
	 */
	@FXML
	void deleteAlbum(ActionEvent event) throws IOException {

		this.errorMessageText.setOpacity(0);


		Album selected = albumListView.getSelectionModel().getSelectedItem();
		if(selected == null) {
			//System.out.println("No album selected");
			this.errorMessageText.setOpacity(1);
			this.errorMessageText.setText("Please select an album to delete");
			this.errorMessageText.setFill(Color.RED);
			return;
		}

		//alert sent out if the format is valid
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Album Confirmation");
		alert.setHeaderText("Album Deletion");
		alert.setContentText("Are you sure you want to delete the selected album?");

		Optional<ButtonType> reply = alert.showAndWait();

		if((reply.isPresent()) && (reply.get() == ButtonType.OK)) {

			for(Album a: currentUser.getAlbumList()) {
				if(a.getAlbumName().compareTo(selected.getAlbumName()) == 0) {
					currentUser.getAlbumList().remove(a);
					obsList.remove(selected);
					if(currentUser.getAlbumList().size() == 0) {
						disableButtons();
						this.errorMessageText.setText("Create an album to start adding photos");
						this.errorMessageText.setOpacity(1);
						this.errorMessageText.setFill(Color.GREEN);
					}
					break;
				}
			}

		}
		DataController.getInstance().saveUsers();
		if(this.currentUser.getAlbumList().size() > 0) {
			this.albumListView.getSelectionModel().select(0);
		}
	}

	/**
	 * creates an album selected from the information entered in the newAlbumField TextField
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param event - when the user clicks the create album button
	 * 
	 */
	@FXML
	void createAlbum(ActionEvent event) throws IOException {



		this.errorMessageText.setOpacity(0.0);

		if(newAlbumField.getText().trim().isEmpty()) {
			this.errorMessageText.setOpacity(1);
			this.errorMessageText.setFill(Color.RED);
			this.errorMessageText.setText("An album cannot have an empty name");
			return;
		}

		enableButtons();

		for(Album a: this.currentUser.getAlbumList()) {
			if(a.getAlbumName().compareTo(newAlbumField.getText()) == 0) {
				this.errorMessageText.setOpacity(1);
				this.errorMessageText.setFill(Color.RED);
				this.errorMessageText.setText("Album Name already exists");
				return;
			}
		}

		// Create new album object
		Album newAlbum = new Album(newAlbumField.getText());

		// Add this album to the current user's list of albums
		this.currentUser.getAlbumList().add(newAlbum);

		// Add this album to the list view of albums
		obsList.add(newAlbum);

		// Clear the text field for creating an album
		this.newAlbumField.clear();

		if(this.currentUser.getAlbumList().size() > 0) {
			this.albumListView.getSelectionModel().select(newAlbum);
		}

		DataController.getInstance().saveUsers();
	}

	/**
	 * renames the album selected from the albumListView via the RenameAlbumeViewController
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param event - when the user clicks the rename album button
	 * 
	 */
	@FXML
	void renameAlbum(ActionEvent event) throws IOException {
		this.errorMessageText.setOpacity(0);
		Album selected = albumListView.getSelectionModel().getSelectedItem();


		if(selected == null) {
			//System.out.println("No album selected");
			this.errorMessageText.setOpacity(1);
			this.errorMessageText.setText("Please select an album to rename");
			this.errorMessageText.setFill(Color.RED);
			return;
		}


		try {
			Stage stage;
			Parent root;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/renameAlbumView.fxml"));
			root = loader.load();
			stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			renameAlbumController rc = loader.getController();
			rc.setAlbumToRename(selected);
			rc.setCurrentUser(currentUser);
			rc.start(stage);
			stage.centerOnScreen();
			stage.showAndWait();

			if(rc.getIsAlbumRenamed()) {
				this.errorMessageText.setOpacity(1);
				this.errorMessageText.setText("The selected album has been successfully renamed");
				this.errorMessageText.setFill(Color.BLUE);
			}


		} catch (IOException e) {
			e.printStackTrace();
		}

		albumListView.refresh();
		if(this.currentUser.getAlbumList().size() > 0) {
			this.albumListView.getSelectionModel().select(selected);
		}
		DataController.getInstance().saveUsers();
	}

	/**
	 * searches for photos last modified between the start and end dates specified from the DatePicker
	 * and loads the stage for the SearchViewController where the images modified between those specified dates
	 * are displayed
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param event - when the user clicks the search button associated with the DatePicker
	 * 
	 * 
	 */
	@FXML
	void dateRangeSearch(ActionEvent event) throws IOException {

		ArrayList<Photo> searchResults = new ArrayList<Photo>();
		this.errorMessageText.setOpacity(0);
		if(this.startDate.getValue() == null || this.endDate.getValue() == null) {
			this.errorMessageText.setOpacity(1);
			this.errorMessageText.setText("Please select both a start date and end date");
			this.errorMessageText.setFill(Color.RED);
			return;
		}

		LocalDate localDate = startDate.getValue();
		Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
		Date startDate = Date.from(instant);

		LocalDate localDate2 = endDate.getValue();
		Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
		Date endDate = Date.from(instant2);

		if(endDate.before(startDate)) {
			//System.out.println("Start date must be before end date");
			this.errorMessageText.setText("Start date must be before end date");
			this.errorMessageText.setOpacity(1);
			return;
		}

		for(Album a: this.currentUser.getAlbumList()) {
			for(Photo p: a.getPhotoList()) {
				if(UserViewController.isWithinRange(startDate, endDate, p.getLastModifiedDate())) {
					Photo copy = new Photo(p,p.getAlbumName());
					searchResults.add(copy);
				}
			}
		}

		// Open up a search view here and fill in the results
		Stage stage = null;
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchView.fxml"));
		root = loader.load();
		stage = (Stage)this.tagSearchButton.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		SearchViewController svc = loader.getController();
		svc.setCurrentUser(currentUser);
		svc.setSearchResultsList(searchResults);
		svc.setDateRange("Date Range: " + new SimpleDateFormat("MM/dd/yyyy").format(startDate).toString() + " to " + new SimpleDateFormat("MM/dd/yyyy").format(endDate).toString());
		svc.start(stage);
		stage.centerOnScreen();
		stage.show();	
	}


	/**
	 * returns true if the startDate and endDate are within range of the last modified date, false otherwise
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param startDate - the startDate specified on DatePicker
	 * @param endDate  - the endDate specified on the DatePicker
	 * @return true if the startDate and endDate are within range of the last modified date, false otherwise
	 * 
	 */
	private static boolean isWithinRange(Date startDate, Date endDate, Date testDate) {
		return !(testDate.before(startDate) || testDate.after(endDate));
	}

	/**
	 * Searches for photos that have the specified tag formats:
	 * 
	 * A single tagName=tagValue pair
	 * 
	 * Mulitple tagName=tagValue pairs separated by "or" example Location=Canada or Fruit=Apple or ...
	 * 
	 * Mulitple tagName=tagValue pairs separated by "and" example Location=Canada and Fruit=Apple or ...
	 * 
	 * After the textfields are filled out and the search button is clicked, the SearchViewController is loaded
	 * with the resulting images that satisfy the tag entries.
	 * 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param event searchButton clicked for the tag entries.
	 * 
	 */
	@FXML
	void searchTags(ActionEvent event) throws IOException {
		//System.out.println("Search button clicked");
		this.errorMessageText.setOpacity(0);
		if(searchField.getText().trim().isEmpty()) {
			//System.out.println("No search entered");
			this.errorMessageText.setText("Please enter a Tag(s) to search for");
			this.errorMessageText.setFill(Color.RED);
			this.errorMessageText.setOpacity(1);
			return;
		}

		ArrayList<Photo> searchResults = new ArrayList<Photo>();

		// Searching for tags
		if(searchField.getText().contains("=")) {
			// Single tag no 'or' or 'and'
			if(!searchField.getText().contains("or") && !searchField.getText().contains("and")) {
				String[] split = searchField.getText().split("=");
				if(split.length != 2) {
					this.errorMessageText.setText("Invalid tag search.\nEx: name=brian");
					this.errorMessageText.setOpacity(1);
					return;
				}
				String name = split[0];
				String value = split[1];
				Tag searchTag = new Tag(name, value);
				for(Album a: this.currentUser.getAlbumList()) {
					for(Photo p: a.getPhotoList()) {
						if(p.getPhotoTags().contains(searchTag)) {
							Photo copy = new Photo(p,p.getAlbumName());
							searchResults.add(copy);
						}
					}
				}
			}
			// There are multiple tags separated by 'or'
			else if(searchField.getText().contains("or")){
				String[] splitOr = searchField.getText().split(" or ");

				if(splitOr.length != 2) {
					this.errorMessageText.setText("Invalid tag search.\nEx: place=store or name=brian");
					this.errorMessageText.setOpacity(1);
					return;
				}

				String[] tag1Split = splitOr[0].split("=");

				if(tag1Split.length != 2) {
					this.errorMessageText.setText("Invalid tag search.\nEx: place=store or name=brian");
					this.errorMessageText.setOpacity(1);
					return;
				}

				Tag tag1 = new Tag(tag1Split[0], tag1Split[1]);

				String[] tag2Split = splitOr[1].split("=");

				if(tag2Split.length != 2) {
					this.errorMessageText.setText("Invalid tag search.\nEx: place=store or name=brian");
					this.errorMessageText.setOpacity(1);
					return;
				}

				Tag tag2 = new Tag(tag2Split[0], tag2Split[1]);

				for(Album a: this.currentUser.getAlbumList()) {
					for(Photo p: a.getPhotoList()) {
						if(p.getPhotoTags().contains(tag1) || p.getPhotoTags().contains(tag2)) {
							Photo copy = new Photo(p,p.getAlbumName());
							searchResults.add(copy);
						}
					}
				}

			}
			// There are multiple tags separated by 'and'
			else {


				String[] splitAnd = searchField.getText().split(" and ");

				if(splitAnd.length != 2) {
					this.errorMessageText.setText("Invalid tag search.\nEx: place=store and name=brian");
					this.errorMessageText.setOpacity(1);
					return;
				}

				String[] tag1Split = splitAnd[0].split("=");

				if(tag1Split.length != 2) {
					this.errorMessageText.setText("Invalid tag search.\nEx: place=store and name=brian");
					this.errorMessageText.setOpacity(1);
					return;
				}

				Tag tag1 = new Tag(tag1Split[0], tag1Split[1]);

				String[] tag2Split = splitAnd[1].split("=");

				if(tag2Split.length != 2) {
					this.errorMessageText.setText("Invalid tag search.\nEx: place=store and name=brian");
					this.errorMessageText.setOpacity(1);
					return;
				}

				Tag tag2 = new Tag(tag2Split[0], tag2Split[1]);

				for(Album a: this.currentUser.getAlbumList()) {
					for(Photo p: a.getPhotoList()) {
						if(p.getPhotoTags().contains(tag1) && p.getPhotoTags().contains(tag2)) {
							Photo copy = new Photo(p,p.getAlbumName());
							searchResults.add(copy);
						}
					}
				}
			}
		}
		else {
			this.errorMessageText.setText("Invalid tag search.\nEx1: place=store\nEx2: location=NJ and season=fall");
			this.errorMessageText.setOpacity(1);
			return;
		}

		// Open up a search view here and fill in the results
		Stage stage = null;
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchView.fxml"));
		root = loader.load();
		stage = (Stage)this.tagSearchButton.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		SearchViewController svc = loader.getController();
		svc.setCurrentUser(currentUser);
		svc.setSearchResultsList(searchResults);
		svc.setDateRange(null);
		svc.setTags("Tag(s): " + searchField.getText());
		svc.start(stage);
		stage.centerOnScreen();
		stage.show();


		searchField.clear();
	}

	/**
	 * Logs out the current user and loads the LoginViewController stage
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param event - when the user clicks the logout button.
	 * 
	 * 
	 */
	@FXML
	void logout(ActionEvent event) throws IOException {
		//System.out.println("logout button clicked");
		this.errorMessageText.setOpacity(0);
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
	 * returns the album selected via the albumListView.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 * @return selectedAlbum album in the list view that is selected
	 * 
	 */
	public Album getSelectedAlbum() {
		return selectedAlbum;
	}

	/**
	 * sets the selectedAlbum to the album selected via the albumListView.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param selectedAlbum - the album selected via the albumListView
	 * 
	 * 
	 */
	public void setSelectedAlbum(Album selectedAlbum) {
		this.selectedAlbum = selectedAlbum;
	}
}