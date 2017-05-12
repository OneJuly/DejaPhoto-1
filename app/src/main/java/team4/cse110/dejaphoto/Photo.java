/**
 * Created by Sam Wang on 5/11/2017.
 */

//https://github.com/drewnoakes/metadata-extractor

package team4.cse110.dejaphoto;
import android.content.Context;
import android.graphics.Bitmap;

public class Photo {

    //Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
    //Metadata metadata = ImageMetadataReader.readMetadata(stream);

    //TODO replace member variables with their appropriate types
    Bitmap returnImage;
    int dayTime;
    int weekday;
    int location;
    int karma;
    //////////

    private Context context;
    int weight;

    //constructor for the photo class
    public Photo(Context context){
        this.context = context;
        dayTime = 0;
        weekday = 0;
        location = 0;
        karma = 0;
        weight = 0;
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

    private boolean has_karma() {
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
        if(has_karma()) {
            weight += 100;
        }

        weight += recentlyTakenWeight();
        }
    }
}