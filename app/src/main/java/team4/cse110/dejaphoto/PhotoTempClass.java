/**
 * Created by Alisa & Sam on 5/11/2017.
 */

//https://github.com/drewnoakes/metadata-extractor
//http://stackoverflow.com/questions/28502206/format-time-and-date-which-is-get-from-exifinterface-tag-datetime
//http://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java
//http://stackoverflow.com/questions/17983865/making-a-location-object-in-android-with-latitude-and-longitude-valuesgit

package team4.cse110.dejaphoto;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.ExifInterface;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

//TODO notes:
//convert from path string into bitmap
//check to see if weight member variable is needed

public class Photo {

    //////////////////// MEMBER VARIABLES AND CONSTRUCTORS ////////////////////

    //TODO replace member variables with their appropriate types
    //Class member variables
    private Context context;
    private String path;
    private Bitmap returnImage;
    private double dayTime;
    private String weekday;
    private Location location;
    private boolean karma;
    private double recentlyShown;
    private double weight;

    //TODO clean extra member variables for methods
    private ExifInterface exifInterface;
    private String timeFormat;
    private String weekdayFormat;
    private Date d;
    private String time;
    private String lat;
    private String lon;
    //private String locationStr;

    //Default constructor for the photo class
    public Photo(Context context){
        this.context = context;
        returnImage = null;
        path = null;
        dayTime = 0;
        weekday = "";
        location = null;
        karma = false;
        recentlyShown = 1;
        weight = 0;
    }

    //Constructor for the photo class. Used by PhotoUtils class.
    public Photo (Context context, String path){
        this.context = context;
        this.path = path;
        returnImage = getImage();
        dayTime = getTime();
        weekday = getWeekday();
        location = getLocation();
        karma = false;
        recentlyShown = 1;
        weight = calcWeight();
    }

    //////////////////// HELPER METHODS TO SET CLASS MEMBER VARIABLE VALUES ////////////////////

    //path should be set by the photo constructor (DONE)
    public String getPath(){
        return path;
    }

    //TODO implement functionality
    //returns a Bitmap from the "path" member variable
    public Bitmap getImage(){
        return returnImage;
    }

    //TODO FINISH METHOD
    public double getTime(){

        //TODO use this.returnImage???
        try {
            exifInterface = new ExifInterface(path);
            time = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
        }
        catch (IOException f){
            f.printStackTrace();
        }
        SimpleDateFormat timeF = new SimpleDateFormat("HH");
        try {
            d = timeF.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return 0;
        }
        timeFormat = timeF.format(d);
        return dayTime;
    }

    //TODO FINISH METHOD
    public String getWeekday() {

        //TODO use this.returnImage???
        try {
            exifInterface = new ExifInterface(path);
            weekday = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
        }
        catch(IOException f){
            f.printStackTrace();
        }
        SimpleDateFormat weekdayF = new SimpleDateFormat("EEE");
        try {
            d = weekdayF.parse(weekday);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return "";
        }
        weekdayFormat = weekdayF.format(d);
        return weekdayFormat;
    }

    //TODO FINISH METHOD
    public Location getLocation(){

        //TODO use this.returnImage???
        try {
            exifInterface = new ExifInterface(path);
            lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            lon = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        }
        catch(IOException f){
            f.printStackTrace();
        }
        Location location = new Location("");//provider name is unnecessary
        location.setLatitude(Double.parseDouble(lat));//your coords of course
        location.setLongitude(Double.parseDouble(lon));
        return location;
    }

    //Give karma to a photo
    public void giveKarma(){
        this.karma = true;
    }

    //assigns member variable to keep track of how recently the photo has been shown
    public void mostRecent(){
        recentlyShown = 11;
    }

    public void lessRecent(){
        recentlyShown -= 1;
    }

    public double getWeight(){
        return this.weight;
    }

    //////////////////// METHODS TO DETERMINE PHOTO WEIGHT ////////////////////

    //TODO replace instances of "false" with calculations
    private boolean same_dayTime(Date date) {
        long currentTime = date.getTime();
        long photoTime =


        return false;

    }

    private boolean same_weekday(Date date) {
        return false;
    }

    private boolean same_location() {
        return false;
    }

    private boolean within_a_week(Date date) {
        return false;
    }

    private boolean within_a_month(Date date) {
        return false;
    }

    private boolean within_a_year(Date date) {
        return false;
    }

    //method to return a weight for the image based on how recently the photo was taken
    public double recentlyTakenWeight(Date date) {
        if(within_a_week(date)) {
            return 200;
        }
        if(within_a_month(date)) {
            return 100;
        }
        if(within_a_year(date)) {
            return 50;
        }
        else return 0;
    }

    public boolean hasKarma(){
        if(karma){ return true; }
        else{ return false; }
    }

    //method to calculate the overall weight of the photo
    public double calcWeight(Date date){
        double weight = 300;

        if(same_dayTime(date)) {
            weight += 100;
        }
        if(same_weekday(date)) {
            weight += 100;
        }
        if(same_location()) {
            weight += 100;
        }
        if(karma) {
            weight += 200;
        }
        weight += recentlyTakenWeight(date);

        //TODO factor in whether the picture was taken recently or not

        //sets the most recent photo's weight to zero
        if(recentlyShown == 11){
            this.weight = 0;
            return 0;
        }
        //adjusts for the weight of the photo based on whether it was recently shown
        else{
            this.weight = weight/recentlyShown;
            return (weight/recentlyShown);
        }
    }
}