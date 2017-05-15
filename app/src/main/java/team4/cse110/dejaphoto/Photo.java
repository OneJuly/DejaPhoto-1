package team4.cse110.dejaphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.UUID;

/**
 * Created by Sean on 5/14/2017.
 */

public class Photo {

    /******************** Photo Attributes (i.e. PhotoTable Columns) ************/
    private UUID id;
    private String path;
    private double lat;
    private double lon;
    private boolean karma;
    private double weight;
    private int time;  // see MediaStore.Images.Media.DATE...says it's an int?


    /* Construct a Photo with a specified filepath */
    public Photo(String path) {
        this.path = path;
        this.id = UUID.randomUUID();
    }


    /******************** Attribute Accessors/Mutators ********************/

    /**
     * Get this photo's unique identifier
     *
     * @return
     */
    public UUID getId() {
        return id;
    }

    /**
     * Set this photo's unique identifier
     *
     * @param id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Get the absolute file path of this Photo
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the absolute file path of this Photo
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Get the latitude info attached to this Photo
     *
     * @return
     */
    public double getLat() {
        return lat;
    }

    /**
     * Set the latitude info attached to this Photo
     *
     * @return
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * Get the longitude info attached to this Photo
     *
     * @return
     */
    public double getLon() {
        return lon;
    }

    /**
     * Set the longitude info attached to this Photo
     *
     * @return
     */
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /**
     *
     * @return
     */
    public boolean getKarma() {
        return karma;
    }

    /**
     *
     * @param karma
     */
    public void setKarma(boolean karma) {
        this.karma = karma;
    }

    /**
     *
     * @return
     */
    public double getWeight() {
        return weight;
    }

    /**
     *
     * @param weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     *
     * @return
     */
    public int getTime() {
        return time;
    }

    /**
     *
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /***************** Helper Methods *********************/


    /**
     * Get a Bitmap representation of this Photo Object
     *
     * @return
     */
    public Bitmap getBitmap() {
        return BitmapFactory.decodeFile(path);
    }

}
