package src.fr.uge.memory;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

/**
 * The ImageLoader class deals with retrieving and storing images from files.
 * 
 * @author vincent
 *
 */
public class ImageLoader {
	/**
	 * Array in which retrieved images are stored.
	 * 
	 */
	private Map<Integer,BufferedImage> images;
	private Map<Character,Integer> charToIdMap ;
	private int currentId ;
	/**
	 * Creates a new ImageLoader that will retrieve images from files.
	 *
	 * @param blank File name where the image for the back of cards is located.
	 * @param pics  File names where the images for the faces of the cards are
	 *              located.
	 */
	public ImageLoader()  {
		images = new HashMap<>();
		charToIdMap = new HashMap<>();
		currentId = 1;
	}

	/**
	 * Gets the image.
	 * 
	 * @param i ID of the card.
	 * @return Image of the card.
	 */
	public BufferedImage image(int i) {
		return images.getOrDefault(i, null);
	}

	/**
	 * Gets the number of image faces stored.
	 * 
	 * @return Number of image stored
	 */
	public int size() {
		return images.size();
	}
	/**
	 * load Image by using the character
	 * @param Character represent the name of the image
	 * @throws IOException if the we cannot read the file
	 */
	 public void loadImageForCharacter(char ch) throws IOException {
	        if (!charToIdMap.containsKey(ch)) {
	            try(InputStream input = ImageLoader.class.getResourceAsStream("/images/" +ch+".png")) {
	    			var image = ImageIO.read(input);
	    			images.put(currentId, image);
	    			charToIdMap.put(ch, currentId++);
	    		}
	        }
	    }

	    public BufferedImage getImage(char ch) {
	        Integer id = charToIdMap.get(ch);
	        return id == null ? null : images.get(id);
	    }

	    public int getImageId(char ch) {
	        return charToIdMap.getOrDefault(ch, -1);
	    }
}