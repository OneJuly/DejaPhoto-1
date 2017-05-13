package team4.cse110.dejaphoto;

import android.content.Context;
import android.net.Uri;

/**
 * Created by Sam Wang on 5/11/2017.
 */

public class Photo {

    private Context context;
    private Uri uri;

    //constructor for the photo class
    public Photo(Context context, Uri uri){
        this.context = context;
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public String getWeekday() {
        return "";
    }

    public int getTime() {
        return 0;
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
        return 0;
    }

    //method to calcualte the overall weight of the photo
    public void calcWeight(){

    }
}
