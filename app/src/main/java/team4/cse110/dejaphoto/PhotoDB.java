package team4.cse110.dejaphoto;

import java.util.List;

public interface PhotoDB {

    /**
     * @return all the photos in the main database
     */
    List<Photo> getPhotos();

    /**
     * Removes an entry in the database
     * @param p photo to update
     * @return true if the update was succesful, false otherwise
     */
    void removePhoto(Photo p);

    /**
     *
     * @return all the photos in the previous cache
     */
    List<Photo> getCache();

    /**
     *
     * @return current position in cache
     */
    int getPosition();

    /**
     * Updates an entry in the database
     * @param p photo to update
     * @return true if the update was succesful, false otherwise
     */
    boolean updatePhoto(Photo p);


    /**
     * Updates the cache in the database
     * @param cache new cache
     */
    void setCache(List<Photo> cache);

    /**
     * Updates the position in the database
     * @param pos new position
     */
    void setPosition(int pos);
}
