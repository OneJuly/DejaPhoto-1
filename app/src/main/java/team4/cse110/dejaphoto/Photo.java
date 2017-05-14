/**
 * Created by Alisa & Sam on 5/11/2017.
 */

//https://github.com/drewnoakes/metadata-extractor
//http://stackoverflow.com/questions/28502206/format-time-and-date-which-is-get-from-exifinterface-tag-datetime
//http://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java
//http://stackoverflow.com/questions/17983865/making-a-location-object-in-android-with-latitude-and-longitude-valuesgit

package team4.cse110.dejaphoto;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.provider.MediaStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//TODO notes:
//convert from path string into bitmap
//check to see if weight member variable is needed

public class Photo {

    //////////////////// MEMBER VARIABLES AND CONSTRUCTORS ////////////////////

    //Class member variables
    private Context context;
    private String path;
    private Bitmap returnImage;
    private long time;
    private int dayTime;
    private int dayOfWeek;
    private Location location;
    private boolean karma;
    private double recentlyShown;
    private double weight;

    //Default constructor for the photo class
    public Photo(Context context){
        this.context = context;
        returnImage = null;
        path = null;
        time = 0;
        dayTime = 0;
        dayOfWeek = 0;
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
        time = getTime();
        dayTime = getHour();
        dayOfWeek = getWeekday();
        location = getLocation();
        karma = false;
        recentlyShown = 1;
        weight = 300;
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

    private long getTime(){
        //give the time that the photo was taken in milliseconds since jan 1st, 1970
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
        //String dateTaken = MediaStore.Images.Media.DATE_TAKEN;
        time = Long.parseLong(dateTaken);
        return time;
    }

    //TODO FINISH METHOD
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

    private boolean within_a_week(Calendar calendar) {
        //604,800,000 is the milliseconds in a week
        long day = 604800000L;
        Date date = calendar.getTime();
        long currentTime = date.getTime();
        return ((currentTime - time) < day);
    }

    private boolean within_a_month(Calendar calendar) {
        //2,600,640,000 is the milliseconds in about 30 days
        long month = 2600640000L;
        Date date = calendar.getTime();
        long currentTime = date.getTime();
        return ((currentTime - time) < month);
    }

    private boolean within_a_year(Calendar calendar) {
        //31,449,600,000 is the milliseconds in a year
        long year = 31449600000L;
        Date date = calendar.getTime();
        long currentTime = date.getTime();
        return ((currentTime - time) < year);
    }

    //method to return a weight for the image based on how recently the photo was taken
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

    public boolean hasKarma(){
        return karma;
    }

    //method to calculate the overall weight of the photo
    public double calcWeight(Calendar calendar, Location location){
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
        if(karma) {
            weight += 200;
        }
        weight += recentlyTakenWeight(calendar);

        //sets the most recent photo's weight to zero if is the most recent photo
        if(recentlyShown == 11){
            this.weight = 0;
            return 0;
        }
        //adjusts for the weight of the photo based on whether it was recently shown
        else{
            this.weight = (weight/recentlyShown);
            return (weight/recentlyShown);
        }
    }
}