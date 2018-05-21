package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * This is a class that contains the borderpane, imageview, and image of a certain photo.
 * 
 * @author Brian Schillaci
 */
public class customImage {
	private BorderPane bp;
	private ImageView iv;
	private Image image;
	
	public customImage(BorderPane bp, ImageView iv, Image image) {
		this.bp = bp;
		this.iv = iv;
		this.image = image;
	}

	public BorderPane getBp() {
		return bp;
	}

	public void setBp(BorderPane bp) {
		this.bp = bp;
	}

	public ImageView getIv() {
		return iv;
	}

	public void setIv(ImageView iv) {
		this.iv = iv;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
}
