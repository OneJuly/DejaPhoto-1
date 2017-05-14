/**
 * Created by Alisa & Sam on 5/11/2017.
 */

//https://github.com/drewnoakes/metadata-extractor
//http://stackoverflow.com/questions/28502206/format-time-and-date-which-is-get-from-exifinterface-tag-datetime
//http://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java
//http://stackoverflow.com/questions/17983865/making-a-location-object-in-android-with-latitude-and-longitude-valuesgit

package team4.cse110.dejaphoto;

import android.content.Context;
import android.location.Location;
import android.provider.MediaStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

//TODO notes:
//convert from path string into bitmap
//check to see if weight member variable is needed

/**
 * This class keeps track of the metadata of a photo, and calculates weights
 * for the photo, which is used in deciding the probability of the photo being
 * shown.
 */
public class Photo {

    //////////////////// MEMBER VARIABLES AND CONSTRUCTORS ////////////////////

    // Class member variables for calculations
    private Context context;
    private long time;
    private int dayTime;
    private int dayOfWeek;
    private Location location;
    private double recentlyShown;

    // Photo database columns
    private String path;
    private int karma;
    private double weight;
    private int active;
    private String lat;
    private String lon;
    private UUID id;

    /**
     * Default constructor with random UUID
     */
    public Photo() {
        this(UUID.randomUUID());
    }

    /**
     * Constructor with given UUID
     *
     * @param uuid
     */
    public Photo(UUID uuid){
//        time = getTime();
        id = uuid;
        dayTime = getHour();
        dayOfWeek = getWeekday();
//        location = getLocation();
        karma = 0;
        recentlyShown = 1;
        weight = 300;
    }

    //////////////////// HELPER METHODS TO SET CLASS MEMBER VARIABLE VALUES ////////////////////


    /**
     *
     * @param active
     */
    public void setActive(int active) {
        this.active = active;
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
     * @param karma
     */
    public void setKarma(int karma) {
        this.karma = karma;
    }

    /**
     *
     * @param lon
     */
    public void setLon(String lon) {
        this.lon = lon;
    }

    /**
     *
     * @param lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @return
     */
    public String getLat(){
        return lat;
    }

    /**
     *
     * @return
     */
    public String getLon() {
        return lon;
    }

    /**
     *
     * @return
     */
    public UUID getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public boolean isActive() {
        return active == 1;
    }

    /**
     *
     * @return
     */
    public String getFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    /**
     *
     * @return
     */
    public String getPath(){
        return path;
    }

    /**
     * This method calculates how long ago a picture was taken.
     * @return how long ago a picture was taken, in milliseconds.
     */
/*    private long getTime(){
        // Give the time that the photo was taken in milliseconds since
        // 1.1.1970.
        String[] columns = { MediaStore.Audio.Media._ID };
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                MediaStore.MediaColumns.DATA + "='" + path + "'", null, null);
        if (cursor.getCount() == 0) {
            return 0;
        }
        cursor.moveToFirst();
        String dateTaken = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
        cursor.close();
        // String dateTaken = MediaStore.Images.Media.DATE_TAKEN;
        time = Long.parseLong(dateTaken);
        return time;
    }*/

    //TODO FINISH METHOD

    /**
     * This method calculates
     * @return
     */
    private int getHour(){
        Date d;
        String dateTaken = MediaStore.Images.Media.DATE_TAKEN;

//        //TODO use this.returnImage???
//        try {
//            exifInterface = new ExifInterface(path);
//            time = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
//        }
//        catch (IOException f){
//            f.printStackTrace();
//        }
        SimpleDateFormat time = new SimpleDateFormat("HH");
        try {
            d = time.parse(dateTaken);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return 0;
        }
        String timeFormat = time.format(d);
        dayTime = Integer.parseInt(timeFormat);
        return dayTime;
    }

    //TODO FINISH METHOD
    private int getWeekday() {

        Date d;
        String dateTaken = MediaStore.Images.Media.DATE_TAKEN;
        //TODO use this.returnImage???
//        try {
//            exifInterface = new ExifInterface(path);
//            weekday = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
//        }
//        catch(IOException f){
//            f.printStackTrace();
//        }
        SimpleDateFormat weekday = new SimpleDateFormat("EEE");
        try {
            d = weekday.parse(dateTaken);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return 0;
        }
        String weekdayFormat = weekday.format(d);
        switch (weekdayFormat) {
            case "SUN":
                dayOfWeek = 0;
                break;
            case "MON":
                dayOfWeek = 1;
                break;
            case "TUE":
                dayOfWeek = 2;
                break;
            case "WED":
                dayOfWeek = 3;
                break;
            case "THU":
                dayOfWeek = 4;
                break;
            case "FRI":
                dayOfWeek = 5;
                break;
            case "SAT":
                dayOfWeek = 6;
                break;
        }
        return dayOfWeek;
    }

    //TODO FINISH METHOD
    public Location getLocation(){

        String latitude = MediaStore.Images.Media.LATITUDE;
        String longitude = MediaStore.Images.Media.LONGITUDE;

        //TODO use this.returnImage???
//        try {
//            exifInterface = new ExifInterface(path);
//            lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
//            lon = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//        }
//        catch(IOException f){
//            f.printStackTrace();
//        }
        location = new Location("");//provider name is unnecessary
        location.setLatitude(Double.parseDouble(latitude));//your coords of course
        location.setLongitude(Double.parseDouble(longitude));
        return location;
    }

    //////////////////// METHODS TO DETERMINE PHOTO WEIGHT ////////////////////

    //TODO replace instances of "false" with calculations
    private boolean same_dayTime(Calendar calendar) {
        return ((calendar.get(Calendar.HOUR_OF_DAY) - dayTime) < 2 &&
                (calendar.get(Calendar.HOUR_OF_DAY) - dayTime) > -2);
    }

    private boolean same_weekday(Calendar calendar) {
        return ((calendar.get(Calendar.DAY_OF_WEEK) - 1) == dayOfWeek);
    }

    private boolean same_location(Location location) {
        if(this.location == null || location == null){
            return false;
        }
        float distanceInMeters =  this.location.distanceTo(location);
        return (distanceInMeters < 150);
    }

    /**
     * This method calculates whether a photo was taken in the past week.
     * @param calendar - the current date.
     * @return true if the photo was taken within the last week; false
     * otherwise.
     */
    private boolean within_a_week(Calendar calendar) {
        //604,800,000 is the milliseconds in a week
        long day = 604800000L;
        Date date = calendar.getTime();
        long currentTime = date.getTime();
        return ((currentTime - time) < day);
    }

    /**
     * This method calculates whether a photo was taken in the past month.
     * @param calendar - the current date.
     * @return true if the photo was taken within the last month; false
     * otherwise.
     */
    private boolean within_a_month(Calendar calendar) {
        // 2,600,640,000 milliseconds in 30 days.
        long month = 2600640000L;
        Date date = calendar.getTime();
        long currentTime = date.getTime();
        return ((currentTime - time) < month);
    }

    /**
     * This method checks if a photo was taken within the last year.
     * @param calendar - the current date.
     * @return true if the photo was taken within the last year; false
     * otherwise.
     */
    private boolean within_a_year(Calendar calendar) {
        // 31,449,600,000 milliseconds in a year
        long year = 31449600000L;
        Date date = calendar.getTime();
        long currentTime = date.getTime();
        return ((currentTime - time) < year);
    }

    /**
     * This method returns a weight for the image based on how recently the
     * photo was taken.
     * @param calendar - the date the photo was taken.
     * @return the weight given to the photo.
     */
    private double recentlyTakenWeight(Calendar calendar) {
        if(within_a_week(calendar)) {
            return 200;
        }
        if(within_a_month(calendar)) {
            return 100;
        }
        if(within_a_year(calendar)) {
            return 50;
        }
        else return 0;
    }

    /**
     *
     * @return
     */
    public boolean hasKarma(){
        return karma == 1;
    }

    /**
     * This method calculates the overall weight of a photo.
     * @param calendar - the date the photo was taken.
     * @param location - the location where the photo was taken.
     * @return the weight given to the photo.
     */
    public double getWeight(Calendar calendar, Location location){
        double weight = 300;

        if(same_dayTime(calendar)) {
            weight += 100;
        }
        if(same_weekday(calendar)) {
            weight += 100;
        }
        if(same_location(location)) {
            weight += 100;
        }
        if(hasKarma()) {
            weight += 200;
        }

        weight += recentlyTakenWeight(calendar);

        // Sets the most recent photo's weight to zero if is the most recent photo.
        if(recentlyShown == 11){
            this.weight = 0;
            return 0;
        }
        // Adjusts for the weight of the photo based on whether it was recently shown.
        else{
            this.weight = (weight/recentlyShown);
            return (weight/recentlyShown);
        }
    }

    public double getWeight() {
        return weight;
    }

    // TODO wtf is this?
    public void mostRecent() {
        recentlyShown = 11;
    }

    // TODO wtf is this?
    public void lessRecent() {
        recentlyShown -= 1;
    }
}