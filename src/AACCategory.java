import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.NullKeyException;
import edu.grinnell.csc207.util.KeyNotFoundException;


/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Sheilla Muligande
 *
 */
public class AACCategory implements AACPage {

	//////////FIELDS/////////////////

	String name;
	AssociativeArray<String,String> pairs;

	//////////CONSTRUCTOR////////////
	
	/**
	 * Creates a new empty category with the given name
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.name = name;
		this.pairs = new AssociativeArray<>();

	}
	
	///////////////METHODS///////////////////

	/**
	 * Adds the image location, text pairing to the category
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
		try {
			this.pairs.set(imageLoc,text);
		} catch (NullKeyException e) {
		} //try/catch
	} //addItems

	/**
	 * Returns an array of all the images in the category
	 * @return the array of image locations; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		if (this.pairs.size() == 0){
			return new String[0];
		}
		return this.pairs.getKeyArray();
	} //getImageLocs

	/**
	 * Returns the name of the category
	 * @return the name of the category
	 */
	public String getCategory() {
		return this.name;
	} //getCategory

	/**
	 * Returns the text associated with the given image in this category
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current
	 * 		   category
	 */
	public String select(String imageLoc) {
			try {
				return pairs.get(imageLoc);
			} catch (KeyNotFoundException e) {
				throw new NoSuchElementException();
			}
	}

	/**
	 * Determines if the provided images is stored in the category
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return this.pairs.hasKey(imageLoc);
	}
	
}
