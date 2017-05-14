/**
 * Created by Alisa & Sam on 5/11/17.
 */

package team4.cse110.dejaphoto;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

//TODO notes:
//make the bitmap array an array of URI's?
//account for recently shown photos (weight) in the DJV/Random methods or in the photo class?
//create member variables having to do with current time/day/location?

public class ImageAlgorithms {

    //////////////////// MEMBER VARIABLES AND CONSTRUCTORS ////////////////////
    private Context context;
    private int imageIndex;
    Photo[] previousImages;
    Photo returnImage;
    PhotoUtils photoUtils;
    ArrayList<Photo> photoAlbum;

    public ImageAlgorithms(Context context){
        this.context = context;
        imageIndex = 0;
        previousImages = new Photo[11];
        Photo returnImage = null;
        photoUtils = new PhotoUtils(context);
        photoAlbum = photoUtils.getCameraPhotos();
    }

    //////////////////// HELPER METHODS ////////////////////

    //TODO get a DejaVuEnabled (or something) boolean out of sp for whether DejaVu Mode is enabled
    //http://stackoverflow.com/questions/6737283/weighted-randomness-in-java
    private boolean is_DJV_Enabled(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return false;
    }

    public void getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();


    }


    //////////////////// DJV() & RANDOM() ALGORITHM ////////////////////

    //randomly selects next photo based on weight
    public Photo DJV_algorithm(){

        //get the total weight
        double totalWeight = 0.0d;
        for(Photo photo : photoAlbum){
            totalWeight += photo.getWeight();
        }
        //choose a random photo
        int randomIndex = -1;
        double random = Math.random() * totalWeight;
        for(int i = 0; i < photoAlbum.size(); ++i){
            //TODO change this to photoAlbum.get(i).getWeight()
            //TODO and have the getWeight function set the photo's weight field
            random -= photoAlbum.get(i).getWeight();
            if(random < 0.0d){
                randomIndex = i;
                break;
            }
        }
        //returns a Photo object
        return photoAlbum.get(randomIndex);
    }

    //randomly selects next photo
    public Photo random_algorithm(){

        double random = Math.random() * (photoAlbum.size()+1);
        int randomIndex = (int) random;
        return photoAlbum.get(randomIndex);
    }

    //////////////////// BUTTON FUNCTIONALITY ////////////////////

    //gets called for the next image
    public Bitmap nextImage() {
        if (photoAlbum.isEmpty()) {
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.defaultimage);
            return icon;
        }
        if (photoAlbum.size() == 1){
            photoAlbum.get(0).mostRecent();
            return photoAlbum.get(0).getImage();
        }
        //if the user is on the most recent picture (more "next" than "previous" presses)
        if(imageIndex == 0){
            if(is_DJV_Enabled() == true){
                returnImage = DJV_algorithm();
                //for indexes with images, copies images in array starting with [9]->[10]
                for(int index = 9; index >-1; --index){
                    if(previousImages[index] != null) {
                        previousImages[index].lessRecent();
                        previousImages[index + 1] = previousImages[index];
                    }
                }
                //updates image array and returns a new image
                previousImages[0] = returnImage;
                returnImage.mostRecent();
                return returnImage.getImage();
            }
            else{
                returnImage = random_algorithm();
                //for indexes with images, copies images in array starting with [9]->[10]
                for(int index = 9; index > -1; --index){
                    if(previousImages[index] != null){
                        previousImages[index].lessRecent();
                        previousImages[index + 1] = previousImages[index];
                    }
                }
                //updates image array and rturns a new image
                previousImages[0] = returnImage;
                returnImage.mostRecent();
                return returnImage.getImage();
            }
        }
        //updates the imageIndex to point to the next image and returns that image
        else{
            imageIndex -= 1;
            return previousImages[imageIndex].getImage();
        }
    }

    //gets called for the previous image
    public Bitmap previousImage(){
        //goes back a maximum of 10 images
        if(imageIndex == 10) {
            return null;
        }
        //if there is no image to go back to
        else if(previousImages[imageIndex+1] == null){
            return null;
        }
        //returns the previous image (if there is one)
        else{
            imageIndex += 1;
            return previousImages[imageIndex].getImage();
        }
    }

    //TODO make sure previousImages[imageIndex].giveKarma() works as expected
    public void giveKarma(){
        previousImages[imageIndex].giveKarma();
    }

    //TODO make sure photoUtils.releasePhoto() works as expected
    public Bitmap releasePhoto(){
        photoUtils.releasePhoto();
        previousImages[imageIndex] = null;
        //adjusts the Photo array
        for(int index = imageIndex; index < 10; ++index){
            while(previousImages[imageIndex+1] != null){
                previousImages[index] = previousImages[index+1];
            }
        }
        //returns the bitmap of the next image
        return nextImage();
    }
}