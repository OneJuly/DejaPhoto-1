package team4.cse110.dejaphoto;

import java.util.List;

/**
 * Created by Sean on 5/14/2017.
 */

public interface PhotoDB {

    /**
     *
     * @return
     */
    List<Photo> getPhotos();

    /**
     *
     * @return
     */
    List<Photo> getCache();

    /**
     *
     * @return
     */
    int getPosition();

    /**
     *
     * @param p
     * @return
     */
    boolean updatePhoto(Photo p);

    /**
     *
     * @param cache
     */
    void setCache(List<Photo> cache);

    /**
     *
     * @param pos
     */
    void setPosition(int pos);
}
