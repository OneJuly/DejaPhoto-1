/**
 * Created by Alisa & Sam on 5/11/17.
 */

package team4.cse110.dejaphoto;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;

//TODO notes:
//make the bitmap array an array of URI's?
//account for recently shown photos (weight) in the DJV/Random methods or in the photo class?
//create member variables having to do with current time/day/location?

public class ImageAlgorithms {

    //////////////////// MEMBER VARIABLES AND CONSTRUCTORS ////////////////////

    private Context context;
    private int imageIndex;
    private Photo[] previousImages;
    private ArrayList<Photo> photoAlbum;

    private PhotoUtils photoUtils;
    private PrefUtils prefUtils;

    public ImageAlgorithms(Context context) {
        this.context = context;
        imageIndex = 0;
        previousImages = new Photo[11];
        photoUtils = new PhotoUtils(context);
        prefUtils = new PrefUtils();
        photoAlbum = photoUtils.getCameraPhotos();
    }

    //////////////////// HELPER METHODS ////////////////////

    private boolean is_DJV_Enabled() {
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return prefUtils.dejaVuEnabled(context);
    }

    public Calendar getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }


    public Location getCurrentLocation() {

        //create location manager instance
        LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return TODO;
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, locListener);
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double latitude=0;
        double longitude=0;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        return null;
    }

    //////////////////// DJV() & RANDOM() ALGORITHM ////////////////////

    //randomly selects next photo based on weight
    //http://stackoverflow.com/questions/6737283/weighted-randomness-in-java
    private Photo DJV_algorithm(){

        //get the total weight
        double totalWeight = 0.0d;
        for(Photo photo : photoAlbum){
            totalWeight += photo.calcWeight(getCurrentDate(), getCurrentLocation());
        }
        //choose a random photo
        int randomIndex = -1;
        double random = Math.random() * totalWeight;
        for(int i = 0; i < photoAlbum.size(); ++i){
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
    private Photo random_algorithm(){

        double random = Math.random() * (photoAlbum.size());
        int randomIndex = (int) random;
        return photoAlbum.get(randomIndex);
    }

    //////////////////// BUTTON FUNCTIONALITY ////////////////////

    //gets called for the next image
    private Bitmap nextImage() {
        if (photoAlbum.isEmpty()) {
            //Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
            //        R.drawable.defaultImage);
            return null;
        }
        if (photoAlbum.size() == 1){
            photoAlbum.get(0).mostRecent();
            return photoAlbum.get(0).getImage();
        }
        //if the user is on the most recent picture (more "next" than "previous" presses)
        if(imageIndex == 0){
            if(is_DJV_Enabled()){
                Photo returnImage = DJV_algorithm();
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
                Photo returnImage = random_algorithm();
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
    public Bitmap releasePhotoThroughWidget(){
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

    public void releasePhotoThroughGallery(){
        photoUtils.releasePhoto();
    }
}