/**
 * Created by Alisa on 5/11/17.
 */

package team4.cse110.dejaphoto;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ImageAlgorithms {

    ///////////////
    boolean DJV = true;
    Bitmap returnImage;
    public Bitmap DJV_algorithm(){
        return returnImage;}
    public Bitmap random_algorithm(){
        return returnImage;}
    //////////////

    private Context context;
    private int imageIndex;
    Bitmap[] previousImages = new Bitmap[11];

    public ImageAlgorithms(Context context){
        this.context = context;
        imageIndex = 0;
    }

    //TODO get a DejaVuEnabled (or something) boolean out of sp for whether DejaVu Mode is enabled
    SharedPreferences sp = context.getSharedPreferences("Settings",0);

    //gets called for the next image
    public Bitmap nextImage(){

        //TODO if there are no photos in the DejaPhoto album, display a default photo
        if(true){
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.defaultImage);
        }

        if(imageIndex == 0){
            //TODO update DJV boolean
            if(DJV == true){
                //TODO update DJV_algorithm
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
                //TODO update random_algorithm
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
