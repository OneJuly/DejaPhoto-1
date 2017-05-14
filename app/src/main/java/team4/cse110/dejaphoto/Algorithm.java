package team4.cse110.dejaphoto;

import android.graphics.Bitmap;

import com.snappydb.SnappydbException;

public interface Algorithm {

    /**
     * Get the next image in the display cycle. save() is called before return
     *
     * @return bitmap representing the next image, or null if there are no images in the display
     * cycle
     */
    Bitmap next();

    /**
     * Get the previous image in the display cycle. save() is called before return
     *
     * @return bitmap representing the previous image, or null if there are no previous images
     */
    Bitmap prev();

    /**
     * Set karma for the last image returned by prev(), next() or release(). save() is called
     * before return
     */
    void incKarma();

    /**
     * Check if the last image returned by prev(), next() or release() has karma set
     *
     * @return true if it has karma, false otherwise
     */
    boolean hasKarma();

    /**
     * Release the last image returned by prev() or next() from the display cycle. save() is
     * called before return
     *
     * @return bitmap to display instead, or null if there are no replacement bitmaps
     */
    Bitmap release();

    /**
     * Save object state into persistent store
     */
    void save();

    /**
     * Load object state from persistent store. Note that this may override the current state
     */
    void load() throws SnappydbException;

}
