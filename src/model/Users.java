package model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * This class represents the list of users for the whole program.
 * 
 * @author Brian Schillaci
 *
 */
public class Users implements Serializable {
	private static final long serialVersionUID = 8579283759287358297L;
	/**
	 * 
	 * The Users contained in PhotoAlbum.
	 * 
	 * @author Brian Schillaci
	 */
	public ArrayList<User> users;
	
	/**
	 * 
	 * Instantiates a new Users object.
	 * 
	 *  @author Brian Schillaci
	 */
	public Users() {
		users = new ArrayList<User>();
	}
	
	/**
	 * Adds a User to this Users object.
	 * 
	 * @author Brian Schillaci
	 * @param user the User to add.
	 * @return true if the User could be added, false if another User by that username exists.
	 */
	public boolean addUser(User user) {
		for (User u : users) {
			if (u.equals(user)) {
				return false;
			}
		}
		users.add(user);
		return true;
		
	}
	
	/**
	 * Looks up a User by name.
	 * 
	 * @author Brian Schillaci
	 * @param name the username to look up.
	 * @return A User if a matching one could be found, null otherwise.
	 */
	public User getUser(String name) {
		for (User u : users) {
			if (u.getUsername().equals(name)) return u;
		}
		return null;
	}
	
	/**
	 * Gets the names of all the Users contained in this instance.
	 * 
	 * @author Brian Schillaci
	 * @return a List of usernames.
	 */
	public ArrayList<String> getUserNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (User u : users) names.add(u.getUsername());
		return names;
	}
	

}
