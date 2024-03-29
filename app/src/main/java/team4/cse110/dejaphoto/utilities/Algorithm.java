package team4.cse110.dejaphoto.utilities;

import team4.cse110.dejaphoto.database.DatabaseInterface;
import team4.cse110.dejaphoto.photo.Photo;

/**
 * This is an interface for the DejaAlgorithm class.
 */
public interface Algorithm {
    /**
     * Get the next image in the display cycle. save() is called before return
     * @return the bitmap representing the next image, or null if there are no
     * images in the display cycle
     */
    Photo next();

    /**
     * Get the previous image in the display cycle. save() is called before
     * return.
     * @return the bitmap representing the previous image, or null if there
     * are no previous images.
     */
    Photo prev();

    /**
     * Set karma for the last image returned by prev(), next() or release().
     * save() is called before return.
     */
    void incKarma();

    /**
     * Check if the last image returned by prev(), next() or release() has
     * karma set.
     * @return true if it has karma, false otherwise.
     */
    boolean hasKarma();

    /**
     * Release the last image returned by prev() or next() from the display
     * cycle. save() is called before return.
     * @return the bitmap to display instead, or null if there are no
     * replacement bitmaps.
     */
    Photo release();

    /**
     * Save state to data base.
     */
    void save();

    /**
     * Load state from data base. Note that this my overwrite current state.
     */
    void load();

    /**
     * @return database of this algorithm
     */
    DatabaseInterface getDatabase();
}
