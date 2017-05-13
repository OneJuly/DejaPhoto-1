/**
 * Created by Sam Wang on 5/11/2017.
 */

//https://github.com/drewnoakes/metadata-extractor
//http://stackoverflow.com/questions/28502206/format-time-and-date-which-is-get-from-exifinterface-tag-datetime
//http://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java

package team4.cse110.dejaphoto;

import android.content.Context;

public class Photo {

    //TODO replace member variables with their appropriate types
    private Context context;
    private String path;
/*    private Bitmap returnImage;
    private int dayTime;
    private String weekday;
    private int location;
    private boolean karma;*/
    //private int weight;
    //////////

//    ExifInterface exifInterface;
/*    private String timeFormat;
    private String weekdayFormat;
    private Date d;
    private String time;*/

    //constructor for the photo class
    public Photo(Context context){
        this.context = context;
/*        returnImage = null;
        path = null;
        dayTime = 0;
        weekday = "";
        location = 0;
        karma = false;*/
        //weight = 0;
    }

    public Photo (Context context, String path){
        this.context = context;
        this.path = path;
/*        returnImage = null;
        dayTime = getTime();
        weekday = getWeekday();
        location = getLocation();
        karma = false;*/
        //weight = calcWeight();
    }

    public String getPath(){
        return path;
    }

/*    //TODO FINISH METHOD
    public int getTime(){

        //TODO use this.returnImage???

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

    //TODO FINISH METHOD
    public String getWeekday() {

        //TODO use this.returnImage???

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

    //TODO FINISH METHOD
    public int getLocation(){

        //TODO use this.returnImage???

        return 0;
    }

    //Give karma to a photo
    public void giveKarma(){
        this.karma = true;
    }

    //TODO replace instances of "false" with calculations
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
    public int calcWeight(){
        //TODO check whether the time of day is within an hour of the time the pic was taken
        int weight = 300;

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

        return weight;
    }*/
}