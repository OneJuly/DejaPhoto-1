package team4.cse110.dejaphoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.Calendar;
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
    private int karma;
    private long time;
    private double weight;
    private Context context;

    /* Construct a Photo with a specified filepath */
    public Photo(Context context, String path) {
        this.path = path;
        this.id = UUID.randomUUID();
        this.context = context;
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
    public int getKarma() {
        return karma;
    }

    /**
     *
     * @param karma
     */
    public void setKarma(int karma) {
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
    public long getTime() {
        return time;
    }

    /**
     *
     * @param time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     *
     * @return double representing the weight of the photo
     */
    public double calcWeight(){
        double weight = 300;
        if(is_same_time()){
            weight += 100;
        }
        if(is_same_weekday()){
            weight += 100;
        }
        if(is_same_location()){
            weight += 100;
        }
        if(karma == 1){
            weight += 200;
        }
        if(is_recently_shown()){
            weight -= 200;
        }
        return weight += recentlyTakenWeight();
    }

    /***************** Helper Methods *********************/

    public Location getCurrentLocation() {
        // http://stackoverflow.com/questions/32491960/android-check-permission-for-locationmanager
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }
        catch (SecurityException e) {
            Log.v("ImageAlgorithms", "getCurrentLocation(): SecurityException caught.");
            System.out.println("getCurrentLocation(): SecurityException caught.");
            return null;
        }
    }

    public Calendar getCurrentDate() {
        try {
            Calendar calendar = Calendar.getInstance();
            return calendar;
        }
        catch (Exception e) {
            Log.v("ImageAlgorithms", "getCurrentDate(): Exception caught.");
            System.out.println("getCurrentDate(): Exception caught.");
            return null;
        }
    }

    public boolean is_same_time(){
        //7,200,000 = milliseconds in 2 hours
        Calendar calendar = getCurrentDate();
        if(calendar == null){
            return false;
        }
        return ((calendar.getTimeInMillis() - time) < 7200000 &&
                (calendar.getTimeInMillis() - time) > -7200000);
    }

    //TODO implement functionality
    public boolean is_same_weekday(){
        Calendar calendar = getCurrentDate();
        if(calendar == null){
            return false;
        }


        return false;
    }

    public boolean is_same_location(){

        Location photoLocation = new Location("");
        photoLocation.setLatitude(lat);
        photoLocation.setLongitude(lon);
        Location currLocation = getCurrentLocation();

        if(photoLocation == null || currLocation == null){
            return false;
        }

        float distanceInMeters =  photoLocation.distanceTo(currLocation);
        return (distanceInMeters < 150);
    }

    //TODO implement functionality
    public boolean is_recently_shown(){
        return false;
    }

    public double recentlyTakenWeight(){

        //creates a calendar object and error checks
        Calendar calendar = getCurrentDate();
        if(calendar == null){
            return 0;
        }
        //604800000 = milliseconds in a week
        if((calendar.getTimeInMillis() - time) < 604800000L){
            return 200;
        }
        //2600640000L = milliseconds in a month
        else if((calendar.getTimeInMillis() - time) < 2600640000L){
            return 100;
        }
        //31449600000L = milliseconds in a year
        else if((calendar.getTimeInMillis() - time) < 31449600000L){
            return 50;
        }
        //returns 0 if photo was not taken within a year
        else{
            return 0;
        }
    }

    /**
     * Get a Bitmap representation of this Photo Object
     *
     * @return
     */
    public Bitmap getBitmap() {
        return BitmapFactory.decodeFile(path);
    }

}
