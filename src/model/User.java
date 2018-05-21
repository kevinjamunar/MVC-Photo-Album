package model;

import java.io.Serializable;
import java.util.ArrayList;

import controller.AlbumViewController;
/**
 * This class represents a User that creates an account.
 * 
 * The user is serializable and has a username and a password.
 * 
 * The user also has a list of albums associated with it.
 * 
 * There is also a constructor in this class for a stock user that has stock for its username and password.
 * @author Brian Schillaci
 *
 */
public class User implements Serializable{
private static final long serialVersionUID = 908259028350982608L;

private String username, password; //the username and password for a user

public static User mainUser; 


private ArrayList<Album> albumList; //list of albums associated with a user

public static void setMainUser(User in){
	mainUser = in;
}

public User(String username, String password){
	this.username = username;
	this.password = password;
	this.albumList = new ArrayList<Album>();
}

public User(){
	this.username="default";
	this.password="default";
	this.albumList = new ArrayList<Album>();
}

public User(String stock) {
	this.username = stock;
	this.password = stock;
	this.albumList = new ArrayList<Album>();
	Album a = new Album("stock");
	this.albumList.add(a);
	Photo newPhoto = new Photo(a.getAlbumName(), "stockcar.jpg");
	this.albumList.get(0).getPhotoList().add(newPhoto);
	AlbumViewController.updateAlbumDateRange(this.getAlbumList().get(0), newPhoto, null);
	newPhoto = new Photo(a.getAlbumName(), "stockcar2.jpg");
	this.albumList.get(0).getPhotoList().add(newPhoto);
	AlbumViewController.updateAlbumDateRange(this.getAlbumList().get(0), newPhoto, null);
	newPhoto = new Photo(a.getAlbumName(), "stockcat.jpg");
	this.albumList.get(0).getPhotoList().add(newPhoto);
	AlbumViewController.updateAlbumDateRange(this.getAlbumList().get(0), newPhoto, null);
	newPhoto = new Photo(a.getAlbumName(), "stockdog.jpg");
	this.albumList.get(0).getPhotoList().add(newPhoto);
	AlbumViewController.updateAlbumDateRange(this.getAlbumList().get(0), newPhoto, null);
	newPhoto = new Photo(a.getAlbumName(), "stockhouse.jpg");
	this.albumList.get(0).getPhotoList().add(newPhoto);
	AlbumViewController.updateAlbumDateRange(this.getAlbumList().get(0), newPhoto, null);
	newPhoto = new Photo(a.getAlbumName(), "stockhouse2.jpg");
	this.albumList.get(0).getPhotoList().add(newPhoto);
	AlbumViewController.updateAlbumDateRange(this.getAlbumList().get(0), newPhoto, null);
	newPhoto = new Photo(a.getAlbumName(), "stockhouse3.jpg");
	this.albumList.get(0).getPhotoList().add(newPhoto);
	AlbumViewController.updateAlbumDateRange(this.getAlbumList().get(0), newPhoto, null);
	newPhoto = new Photo(a.getAlbumName(), "stockhouse5.jpg");
	this.albumList.get(0).getPhotoList().add(newPhoto);
	AlbumViewController.updateAlbumDateRange(this.getAlbumList().get(0), newPhoto, null);
}


/**
 * returns true if the user object's username is equal to the user argument object's username, false otherwise
 * 
 * @author Kevin Jamunar
 * @author Brian Schillaci
 * 
 * @param o - object passed in
 * 
 * @return true if the user object's username is equal to the user argument object's username, false otherwise
 */
public boolean equals(Object o) {
	if(o == null || !(o instanceof User)) {
		return false;
	}
	
	User u = (User)o;
	
	return (this.getUsername().compareTo(u.getUsername()) == 0);
}
/**
 * returns the username for a user
 * 
 * @author Kevin Jamunar
 * @author Brian Schillaci
 * 
 * 
 * @return username
 */
public String getUsername() {
	return username;
}


/**
 * sets the username for a user
 * 
 * @author Kevin Jamunar
 * @author Brian Schillaci
 * 
 * @param username - sets the username for the user
 * 
 */
public void setUsername(String username) {
	this.username = username;
}


/**
 * returns the password for a user
 * 
 * @author Kevin Jamunar
 * @author Brian Schillaci
 * 
 * 
 * @return password
 */
public String getPassword() {
	return password;
}


/**
 * sets the password for a user
 * 
 * @author Kevin Jamunar
 * @author Brian Schillaci
 * 
 * @param password - sets the password for the user
 * 
 */
public void setPassword(String password) {
	this.password = password;
}


/**
 * returns an arraylist of Albums associated with this user 
 * 
 * @author Kevin Jamunar
 * @author Brian Schillaci
 * 
 * @return arraylist of albums associated with this user
 */
public ArrayList<Album> getAlbumList(){
	return this.albumList;
}

}


