import java.util.Scanner;
import java.io.File;
import java.util.NoSuchElementException;
import java.io.PrintWriter;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;

/**
 * Creates a set of mappings of an AAC that has two levels, one for categories and then within each
 * category, it has images that have associated text to be spoken. This class provides the methods
 * for interacting with the categories and updating the set of images that would be shown and
 * handling an interactions.
 * 
 * @author Catie Baker & Sheilla Muligande
 *
 */
public class AACMappings implements AACPage {

	//////////////////// FIELDS////////////////////////

	AssociativeArray<String, AACCategory> categories;
	AACCategory category;
	

	///////////////// CONSTRUCTOR///////////////////////

	/**
	 * Creates a set of mappings for the AAC based on the provided file. The file is read in to create
	 * categories and fill each of the categories with initial items. The file is formatted as the
	 * text location of the category followed by the text name of the category and then one line per
	 * item in the category that starts with > and then has the file name and text of that image
	 * 
	 * for instance: img/food/plate.png food >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing and food has french fries and
	 * watermelon and clothing has a collared shirt
	 * 
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {

		
		this.categories = new AssociativeArray<String, AACCategory>();
		this.category = null;
		

		Scanner scanner = null;

		try {
			 scanner = new Scanner(new File(filename));
			while (scanner.hasNextLine()) {

				String currLine = scanner.nextLine().trim();

				if (!currLine.isEmpty()) {
					if (currLine.charAt(0) != '>') {
						String[] pairs = currLine.split(" ", 2);
						AACCategory category1 = new AACCategory(pairs[1]);
						this.categories.set(pairs[0], category1);
						 //this.category = category1;
						// this.currcategory = category1;

					} else // if (currLine.charAt(0) == '>')
					 {
						String[] pairs = currLine.substring(1).split(" ", 2);
						if (this.category != null) {
							this.category.addItem(pairs[0], pairs[1]);
						}
					}
					// if (this.category != null) {
					// 	if (this.category.getImageLocs().length == 0) {
					// 		this.category = null;
					// 	}
					// }

				}
			}
		} catch (Exception e) {
		}

		if (scanner != null) {
			scanner.close();
		}

	}

	/////////////////////// Methods////////////////////////////////////////
	/**
	 * Given the image location selected, it determines the action to be taken. This can be updating
	 * the information that should be displayed or returning text to be spoken. If the image provided
	 * is a category, it updates the AAC's current category to be the category associated with that
	 * image and returns the empty string. If the AAC is currently in a category and the image
	 * provided is in that category, it returns the text to be spoken.
	 * 
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise it returns the
	 *         empty string
	 * @throws NoSuchElementException if the image provided is not in the current category
	 */
	public String select(String imageLoc) {
		try {
			if (this.category == null) {
				this.category = this.categories.get(imageLoc);
				return "";
			} else {
				if (this.category.hasImage(imageLoc)) {
					return this.category.select(imageLoc);
				} else {
					throw new NoSuchElementException();
				}
			}
		} catch (KeyNotFoundException e) {
			throw new NoSuchElementException();
		}

	}

	/**
	 * Provides an array of all the images in the current category
	 * 
	 * @return the array of images in the current category; if there are no images, it should return
	 *         an empty array
	 */
	public String[] getImageLocs() {
		// if (this.category == null){
		// 	return new String[0];
		// }
		// return this.categories.getKeyArray();
		// //return this.category.getImageLocs();

		if (this.category != null){
			return this.category.getImageLocs();
		}

		return new String[0];
	}

	/**
	 * Resets the current category of the AAC back to the default category
	 */
	public void reset() {
		this.category = null;
	}


	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as the text location of the
	 * category followed by the text name of the category and then one line per item in the category
	 * that starts with > and then has the file name and text of that image
	 * 
	 * for instance: img/food/plate.png food >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing and food has french fries and
	 * watermelon and clothing has a collared shirt
	 * 
	 * @param filename the name of the file to write the AAC mapping to
	 */
	public void writeToFile(String filename) {
		String[] paths;

		try {
			PrintWriter pen = new PrintWriter(filename);
			paths = this.categories.getKeyArray();
			for (int i = 0; i < paths.length; i++) {
				AACCategory categ = this.categories.get(paths[i]);
				pen.println(paths[i] + " " + this.category.getCategory());

				String[] locs = this.category.getImageLocs();
				for (int x = 0; x < locs.length; x++) {
					pen.println(" >" + locs[x] + this.category.select(locs[x]));
				} // for
			} // for
			pen.close();
		} catch (Exception e) {
		}

	} // WriteToFile


	/**
	 * Adds the mapping to the current category (or the default category if that is the current
	 * category)
	 * 
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		if (this.category != null) {
			this.category.addItem(imageLoc, text);
		}
	}


	/**
	 * Gets the name of the current category
	 * 
	 * @return returns the current category or the empty string if on the default category
	 */
	public String getCategory() {
		if (this.category != null) {
			return this.category.getCategory();
		} else {
			return "";
		}
	}


	/**
	 * Determines if the provided image is in the set of images that can be displayed and false
	 * otherwise
	 * 
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		if (this.category != null) {
			return this.category.hasImage(imageLoc);
		} else
			return false;
	}
}
