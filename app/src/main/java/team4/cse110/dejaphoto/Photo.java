/**
 * Created by Sam Wang on 5/11/2017.
 */

//https://github.com/drewnoakes/metadata-extractor
//http://stackoverflow.com/questions/28502206/format-time-and-date-which-is-get-from-exifinterface-tag-datetime
//http://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java

package team4.cse110.dejaphoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.media.ExifInterface;

import java.util.Date;
import java.text.ParseException;

public class Photo {

    //Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
    //Metadata metadata = ImageMetadataReader.readMetadata(stream);

    //TODO replace member variables with their appropriate types
    Bitmap returnImage;
    int dayTime;
    String weekday;
    int location;
    boolean karma;
    //////////

    private Context context;
    int weight;
    ExifInterface exifInterface;
    String weekdayFormat;
    String timeFormat;
    Date d;
    String time;

    //constructor for the photo class
    public Photo(Context context){
        this.context = context;
        dayTime = 0;
        weekday = "";
        location = 0;
        karma = false;
        weight = 0;
    }

    public String getWeekday() {
        weekday = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
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

    public int getTime(){
        time = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
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




    //TODO update these with the appropriate calculations
    //methods to determine what weights to add onto the photos
    private boolean is_dayTime() {
        return false;
    }

    private boolean is_weekday() {
        return false;
    }

    private boolean is_location() {
        return false;
    }

    private boolean within_a_week() {
        return false;
    }

    private boolean within_a_month() {
        return false;
    }

    private boolean within_a_year() {
        return false;
    }

    //TODO replace instances of "false" with calculations
    //method to return a weight for the image based on how recently the photo was taken
    public int recentlyTakenWeight() {
        if(within_a_week()) {
            return 200;
        }
        if(within_a_month()) {
            return 100;
        }
        if(within_a_year()) {
            return 50;
        }
        else return 0;
    }

    //method to calcualte the overall weight of the photo
    public void calcWeight(){
        //TODO check whether the time of day is within an hour of the time the pic was taken
        weight = 300;

        if(is_dayTime()) {
            weight += 100;
        }
        if(is_weekday()) {
            weight += 100;
        }
        if(is_location()) {
            weight += 100;
        }
        if(karma) {
            weight += 200;
        }
        weight += recentlyTakenWeight();
        }
    }
}