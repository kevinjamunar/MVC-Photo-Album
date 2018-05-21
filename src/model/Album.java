package model;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * This class represents the model of an Album created by a user
 * 
 * albumName: holds the name of the album
 * 
 * photoList: holds a list of photos in a given album
 * 
 * @author Kevin Jamunar
 * 
 * @author Brian Schillaci
 *
 */

public class Album implements Serializable{

	private static final long serialVersionUID = -265627496053330954L;
	private String albumName; //name of the album
	private ArrayList<Photo> photoList; //list of photo objects
	private Date earliestDate; //earliest date associated with a photo
	private Date latestDate; //latest date associated with a photo


	public Album(String albumName) {
		this.albumName = albumName;
		this.photoList = new ArrayList<Photo>();
		this.earliestDate = null;
		this.latestDate = null;
	}
	
	/**
	 * returns a String representation of the album name, number of photos in that album,
	 * and the date range of the photos in the album.
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * 
	 * @return String representation of the album name, number of photos in that album,
	 * and the date range of the photos in the album.
	 */
	public String toString() {
		if(this.earliestDate != null && this.latestDate != null) {
			return "Name: " + this.albumName + ", Number of photos: " + photoList.size() + ", Date Range: " + new SimpleDateFormat("MM/dd/yyyy, h:mm:ss aaa").format(this.earliestDate).toString() +  "  -  " + new SimpleDateFormat("MM/dd/yyyy, h:mm:ss aaa").format(this.latestDate).toString();
		}
		else if(this.earliestDate == null && this.latestDate == null) {
			return "Name: " + this.albumName + ", Number of photos: " + photoList.size() + ", Date Range: "  +  "  -  ";
		}
		else if(this.earliestDate == null && this.latestDate != null) {
			return "Name: " + this.albumName + ", Number of photos: " + photoList.size() + ", Date Range: " + new SimpleDateFormat("MM/dd/yyyy, h:mm:ss aaa").format(this.latestDate).toString() +  "  -  " + new SimpleDateFormat("MM/dd/yyyy, h:mm:ss aaa").format(this.latestDate).toString();
		}
		else if(this.earliestDate != null && this.latestDate == null){
			return "Name: " + this.albumName + ", Number of photos: " + photoList.size() + ", Date Range: " + new SimpleDateFormat("MM/dd/yyyy, h:mm:ss aaa").format(this.earliestDate).toString() +  "  -  " + new SimpleDateFormat("MM/dd/yyyy, h:mm:ss aaa").format(this.earliestDate).toString();
		}
		return "none";
	}

	/**
	 * returns the album name 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return albumName album name
	 */
	public String getAlbumName(){
		return this.albumName;
	}

	/**
	 * returns an arraylist of photo objects 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return arraylist of photo objects
	 */
	public ArrayList<Photo> getPhotoList() {
		return this.photoList;
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
	 * returns the earliestDate of a photo in the album
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return earliestDate date object
	 */
	public Date getEarliestDate() {
		return earliestDate;
	}

	/**
	 * sets the earliestDate of a photo in the album
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param  earliestDate - earliestDate of a photo in the album
	 * 
	 */
	public void setEarliestDate(Date earliestDate) {
		this.earliestDate = earliestDate;
	}

	/**
	 * returns the latestDate of a photo in the album
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return latestDate latest Date
	 */
	public Date getLatestDate() {
		return latestDate;
	}

	/**
	 * sets the latestDate of a photo in the album
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param  latestDate - latestDate of a photo in the album
	 * 
	 */
	public void setLatestDate(Date latestDate) {
		this.latestDate = latestDate;
	}

}
