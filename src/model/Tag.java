package model;

import java.io.Serializable;
/**
 * Class representing a photo tag.
 * 
 * Has a string value and string name.
 * 
 * Users can put many tags on a photo.
 * @author Brian Schillaci
 *
 */
public class Tag implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4311497896654961624L;
	private String name; //tag name
	private String value; // tag value
	private String listRepresentation; //representation of a tag in the ListView
	

	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 *
	 * Sets the tag name
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param name - tag name
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * Sets the tag value
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param value - tag value
	 * 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 *
	 * returns the tag name
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return tag name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 *
	 * returns the tag value
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return tag value
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 *
	 * returns the list representation of a tag in the ListView
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return list representation of a tag in the ListView
	 */
	public String getListRepresentation(){
		return this.listRepresentation;
	}
	

	/**
	 *
	 * Sets the list representation to the list representation of a tag in the ListView
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @param listRepresentation - list representation of a tag in the ListView
	 * 
	 */
	public void setListRepresentation(String listRepresentation) {
		this.listRepresentation = listRepresentation;
	}
	
	/**
	 *
	 * returns the String representation of a tag name and tag value
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return String representation of a tag name and tag value
	 */
	public String toString() {
		return this.name + "=" + this.value;
	}
	
	/**
	 *
	 * returns true if the tag name and tag value of the argument matches that of the Tag object calling the equals method
	 * returns false otherwise
	 * 
	 * @author Kevin Jamunar
	 * @author Brian Schillaci
	 * 
	 * @return true if the tag name and tag value of the argument matches that of the Tag object calling the equals method
	 * returns false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Tag)){
			return false;
		}
		Tag tag = (Tag)o;
		
		return (tag.name.compareTo(this.name) == 0 && (tag.value.compareTo(this.value)) == 0);
	}
	
}
