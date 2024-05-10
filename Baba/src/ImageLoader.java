

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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
	 */
	private final BufferedImage[] images;

	/**
	 * Creates a new ImageLoader that will retrieve images from files.
	 *
	 * @param blank File name where the image for the back of cards is located.
	 * @param pics  File names where the images for the faces of the cards are
	 *              located.
	 */
	public ImageLoader(String blank, String... pics)  {
		Objects.requireNonNull(pics);
		try {
		images = new BufferedImage[pics.length + 1];
		
		
		setImage(0,  blank);
		
		
		
		for (var i = 0; i < pics.length; i++) {
			setImage(i, pics[i]);
		}} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Retrieve a new image from a file, and stores it into a dedicated array.
	 * 
	 * @param position  Position (i.e., ID + 1) of the image.
	 * @param imagePath File name.
	 */
	private void setImage(int position,  String imagePath) throws IOException {
		Objects.requireNonNull(imagePath);
		try(InputStream input = ImageLoader.class.getResourceAsStream("/images/" + imagePath)) {
			images[position] = ImageIO.read(input);
		}
	}
	
	
	

	/**
	 * Gets the image showing the back of cards.
	 * 
	 * @return Image showing the back of cards.
	 */
	public BufferedImage blank() {
		return images[0];
	}

	
	
	
	
	/**
	 * Gets the image showing the face of a card.
	 * 
	 * @param i ID of the card.
	 * @return Image of the face of the card.
	 */
	public BufferedImage image(int i) {
		Objects.checkIndex(i, images.length - 1);
		return images[i + 1];
	}

	/**
	 * Gets the number of image faces stored.
	 * 
	 * @return Number of image faces of stored.
	 */
	public int size() {
		return images.length;
	}
}