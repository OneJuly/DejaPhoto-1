package team4.cse110.dejaphoto;

import java.util.UUID;

/**
 * Created by Sean on 5/14/2017.
 */

public class Photo {

    /******************** Photo Attributes (i.e. PhotoTable Columns) ************/
    private UUID id;
    private String path;
    private String lat;
    private String lon;
    private int karma;
    private double weight;
    private int active;


    /******************** Attribute Accessors/Mutators ********************/
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
