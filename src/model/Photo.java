package model;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 
 * 
 * This class represents the model of photo added by a user: 
 *
 * albumName: holds the album the added photo is in.
 * 
 * tagsList: holds a list of tags associated with a given photo
 *
 *
 * @author Kevin Jamunar
 * 
 * @author Brian Schillaci
 * 
 *
 */

public class Photo implements Serializable{
	
	private static final long serialVersionUID = -3218158205842797606L;
	private String photoCaption = null; //caption for a given photo object
	private String albumName; //name of the album the photo is located in
	private ArrayList<Tag> tagsList; //list of tags for a given photo object
	private String photoLocation; //location of the photo on the system
	private Date lastModifiedDate; //the date the photo was last modified
	
	public Photo(String albumName, String photoLocation) {
		this.albumName = albumName;
		this.lastModifiedDate = new Date(new File(photoLocation).lastModified());
		this.tagsList = new ArrayList<Tag>();
		this.photoLocation = photoLocation;
	}
	
	/**
	 * returns a String representation of the date the photo was last modified
	 * 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return SimpleDateFormat string form
	 */
	public String photoModifiedDate() {
		return new SimpleDateFormat("dd-MM-yyyy, h:mm:ss aaa").format(this.lastModifiedDate).toString();
	}
	
	/**
	 * returns the album name 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return albumName album name
	 */
	public String getAlbumName() {
		return this.albumName;
	}
	
	/**
	 * returns an arraylist of photo tags 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return arraylist of photo tags
	 */
	public ArrayList<Tag> getPhotoTags() {
		return this.tagsList;
	}
	
	/**
	 * sets the album name
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param  albumName - album name
	 * 
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	/**
	 * returns the location of the photo on the System
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return location of the photo on the System
	 */
	public String getPhotoLocation() {
		return photoLocation;
	}


	/**
	 * sets the location of a photo on the system
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param  photoLocation - location of a photo on the system
	 * 
	 */
	public void setPhotoLocation(String photoLocation) {
		this.photoLocation = photoLocation;
	}
	
	/**
	 * sets the caption for a photo
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param  photoCaption - caption for a photo
	 * 
	 */
	public void setPhotoCaption(String photoCaption) {
		this.photoCaption = photoCaption;
	}
	
	/**
	 * returns the the caption for a photo
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return photoCaption string caption
	 */
	public String getPhotoCaption() {
		return this.photoCaption;
	}
	

	/**
	 * returns the latest modified Date of a photo in the album
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return latestModifiedDate last modified date
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}


	/**
	 * sets the latest modified date of a photo in the album
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param  lastModifiedDate - lastModifiedDate of a photo in the album
	 * 
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}  
	
	/**
	 * 
	 * Constructor to clone a photo.
	 * This is used in photo move and photo copy.
	 * 
	 * 
	 * @author Brian Schillaci
	 * @param photo photo to copy
	 * @param newAlbumName album to copy to
	 */
	public Photo (Photo photo, String newAlbumName) {
		this.albumName = photo.albumName;
		this.lastModifiedDate = photo.lastModifiedDate;
		this.photoCaption = photo.photoCaption;
		this.photoLocation = photo.photoLocation;
		this.tagsList = new ArrayList<Tag>();
		int numOfTags = photo.getPhotoTags().size();
		for(int i=0; i<numOfTags; i++) {
			this.tagsList.add(photo.getPhotoTags().get(i));
		}
	}
}
