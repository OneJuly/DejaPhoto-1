package team4.cse110.dejaphoto.database;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import team4.cse110.dejaphoto.photo.Photo;

/**
 * Describes the functionality needed for a persistent storage backend
 */
public interface DatabaseInterface {

    /**
     * Add a new photo to the user's photo list node
     * @param photo photo to add
     */
    void addPhoto(Photo photo);

    /**
     * Remove a photo from the user's photo list node
     * @param photo photo to remove
     */
    void deletePhoto(Photo photo);

    /**
     * Set the value of the user's share photos flag.
     * This flag tells other users we are willing to share out photos
     * @param value value to set the flag to. <code>true</code> will enable sharing
     */
    void setSharePhotos(boolean value);

    /**
     * Get the list of all available photos for the display cycle.
     * This takes into account whether user wants to see its own photos and whether the user's
     * friends are willing to share their photos
     * @return list of all available photos for the display cycle
     */
    List<Photo> getPhotoList();

    /**
     * Get a displayable bitmap for the photo
     * @param photo photo to get a bitmap for
     * @return the bitmap
     */
    Bitmap fetchBitmap(Photo photo);

    /**
     * Store the previous list
     * @param list previous list
     */
    void storePreviousList(ArrayList<Photo> list);

    /**
     * Retrieve the previous list from database
     * @return the previous list
     */
    ArrayList<Photo> getPreviousList();

    /**
     * Store the current position in the previous list
     * @param index current position in the previous list
     */
    void storePreviousIndex(int index);

    /**
     * Retrieve the current position in the previous list from storage
     * @return current position in the previous list
     */
    int getPreviousIndex();
}