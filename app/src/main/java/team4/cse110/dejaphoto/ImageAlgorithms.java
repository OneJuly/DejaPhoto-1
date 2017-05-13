/**
 * Created by Alisa & Sam on 5/11/17.
 */

package team4.cse110.dejaphoto;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//on return, convert return value to a bitmap



public class ImageAlgorithms {


    //////////////////// MEMBER VARIABLES AND CONSTRUCTORS ////////////////////

    //TODO replace variables with appropriate types and initialize values
    Bitmap returnImage;
    //////////

    private Context context;
    private int imageIndex;
    Bitmap[] previousImages = new Bitmap[11];

    public ImageAlgorithms(Context context){
        this.context = context;
        imageIndex = 0;
    }

    //TODO get a DejaVuEnabled (or something) boolean out of sp for whether DejaVu Mode is enabled
    private boolean is_DJV_Enabled(){
        SharedPreferences sp = context.getSharedPreferences("Settings",0);
        return false;
    }

    //////////////////// DJV & RANDOM ALGORITHM ////////////////////

    //TODO implement algorithm
    public Bitmap DJV_algorithm(){

        //for each photo in the DejaVu photo album, update all of their weights.
        //for each photo in the DejaVu


        int weightSum = 0;
        for(int index = 0; index < 1; ++index/*each photo in the album*/) {
            for (int i = 0; i < weightSum; ++i) {
                weightSum += 0;
            }
        }

        return returnImage;
    }

    //TODO implement algorithm
    public Bitmap random_algorithm(){

        return returnImage;
    }

    //////////////////// BUTTON FUNCTIONALITY ////////////////////

    //gets called for the next image
    public Bitmap nextImage(){
        //TODO if there are no photos in the DejaPhoto album
        if(true){
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.defaultimage);
            return icon;
        }
        //if the user is on the most recent picture (more "next" than "previous" presses)
        if(imageIndex == 0){
            if(is_DJV_Enabled() == true){
                returnImage = DJV_algorithm();
                //for indexes with images, copies images in array starting with [9]->[10]
                for(int index = 9; index >-1; --index){
                    if(previousImages[index] != null) {
                        previousImages[index + 1] = previousImages[index];
                    }
                }
                //updates image array and returns a new image
                previousImages[0] = returnImage;
                return returnImage;
            }
            else{
                returnImage = random_algorithm();
                //for indexes with images, copies images in array starting with [9]->[10]
                for(int index = 9; index > -1; --index){
                    if(previousImages[index] != null){
                        previousImages[index + 1] = previousImages[index];
                    }
                }
                //updates image array and rturns a new image
                previousImages[0] = returnImage;
                return returnImage;
            }
        }
        //updates the imageIndex to point to the next image and returns that image
        else{
            imageIndex -= 1;
            return previousImages[imageIndex];
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
            return previousImages[imageIndex];
        }
    }
}