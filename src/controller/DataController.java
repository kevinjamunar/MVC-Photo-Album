package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import model.Users;

/**
 * This class is a controller for saving and loading the user's results.
 * 
 * It saves by serializing to the users.ser file and loads by deserializing the users.ser file.
 * 
 * @author Brian Schillaci
 *
 */
public class DataController {
	
	private static final String USERS_FILE = "users.ser";
	
	private static DataController data;
	
	//users object holds a list of users
	private Users users;
		
	private DataController() {
		loadUsers();
	}
	
	/**
	 * 
	 * Gets the instance of Data that exists in this application.
	 * 
	 * @author Brian Schillaci
	 * @return the Data singleton.
	 */
	public static DataController getInstance() {
		if (data == null) {
			data = new DataController();
		}
		return data;
	}
	
	/**
	 * Gets the current state of the Users object for Photo Album.
	 * @author Brian Schillaci
	 * @return Pointer to the Users object.
	 */
	public Users getUsers() {
		return users;
	}
	
	/**
	 * 
	 * Saves the Users object to a package file "users.ser"
	 * Users.ser has all of the users serialized
	 * 
	 * @author Brian Schillaci
	 */
	public void saveUsers() {
		try {
			FileOutputStream outTest = new FileOutputStream(USERS_FILE);
			PrintWriter pw = new PrintWriter(outTest);
			pw.close(); // Empty out existing file
			FileOutputStream outFile = new FileOutputStream(USERS_FILE);
			ObjectOutputStream out = new ObjectOutputStream(outFile);
			out.writeObject(users);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * reads in the serialized users file and deserializes it to obtain the saved information
	 * If the file does not exist, a file is created and a users object is created.
	 * 
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 */
	private void loadUsers() {
    	File f = new File(USERS_FILE);
    	if (!f.exists()) {
    		users = new Users();
    		//System.out.println("DNE");
    		saveUsers();
    		return;
    	}
    	try {
    		FileInputStream inFile = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(inFile);
			Users users = (Users) in.readObject();
			in.close();
			inFile.close();
			this.users = users;
			return;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}

}

