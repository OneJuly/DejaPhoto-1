package team4.cse110.dejaphoto;

import android.graphics.Bitmap;

public interface Algorithm {

    /**
     * Get the next image in the display cycle. save() is called before return
     * @return bitmap representing the next image, or null if there are no images in the display
     * cycle
     */
    Bitmap next();

    /**
     * Get the previous image in the display cycle. save() is called before return
     * @return bitmap representing the previous image, or null if there are no previous images
     */
    Bitmap prev();

    /**
     * Set karma for the last image returned by prev() or next(). save() is called before return
     */
    void incKarma();

    /**
     * Check if the last image returned by prev() or next() has karma set
     * @return true if it has karma, false otherwise
     */
    boolean hasKarma();

    /**
     * Release the last image returned by prev() or next() from the display cycle
     * @return bitmap to display instead
     */
    Bitmap release();

    /**
     * Save object state into persistent store
     */
    void save();

    /**
     * Load object state from persistent store. Note that hsi may override the current state
     */
    void load();

}
