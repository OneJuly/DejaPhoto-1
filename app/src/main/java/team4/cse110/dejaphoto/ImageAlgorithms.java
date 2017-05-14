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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.snappydb.DB;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.util.Calendar;
import java.util.List;

//TODO notes:
//make the bitmap array an array of URI's?
//account for recently shown photos (weight) in the DJV/Random methods or in the photo class?
//create member variables having to do with current time/day/location?

public class ImageAlgorithms implements Algorithm {

    private static final String TAG = "ImageAlgorithms";

    private static final String DB_NAME = "AlgorithmDB";
    private static final String IMAGE_INDEX= "imageIndex";
    private static final String PREV_IMAGES = "previousImages";
    private static final String PHOTO_ALBUM = "photoAlbum";

    private static final int previousArrSize = 11;

    //////////////////// MEMBER VARIABLES AND CONSTRUCTORS ////////////////////

    private Context context;
    private int imageIndex;
    private Photo[] previousImages;
    private List<Photo> photoAlbum;

    private PhotoUtils photoUtils;
    private PrefUtils prefUtils;
    private DB snappydb;

    public ImageAlgorithms(Context context) {
        this.context = context;
        photoUtils = new PhotoUtils(context);
        prefUtils = new PrefUtils();
        photoAlbum = photoUtils.getCameraPhotos();

        /* Build snappyDB */
        try {

            snappydb = new SnappyDB.Builder(context)
                    .directory(Environment.getExternalStorageDirectory().getAbsolutePath())
                    .name(DB_NAME)
                    .build();

        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        load();
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

    public boolean hasKarma(){
        //edge case: no image
        if(previousImages[imageIndex] == null) {
            return false;
        }
        if(previousImages[imageIndex].hasKarma()){
            return true;
        }
        else{
            return false;
        }
    }


    public Location getCurrentLocation() {
        //TODO take out the following if statement and fix code
        if(true){ return null; }


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
        };

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED ) {
                //TODO fix first argument (don't cast to android.app.Activity)
                ActivityCompat.requestPermissions((android.app.Activity) context, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10 );

                return null;
            }
        }
        else{
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, locListener);
        }
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
    public Bitmap next() {
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
                save();
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
                save();
                return returnImage.getImage();
            }
        }
        //updates the imageIndex to point to the next image and returns that image
        else{
            imageIndex -= 1;
            save();
            return previousImages[imageIndex].getImage();
        }
    }

    //gets called for the previous image
    public Bitmap prev(){
        //goes back a maximum of 10 images
        if(imageIndex == 10) {
            save();
            return null;
        }
        //if there is no image to go back to
        else if(previousImages[imageIndex+1] == null){
            save();
            return null;
        }
        //returns the previous image (if there is one)
        else{
            imageIndex += 1;
            save();
            return previousImages[imageIndex].getImage();
        }
    }

    //TODO make sure previousImages[imageIndex].giveKarma() works as expected
    public void incKarma(){
        previousImages[imageIndex].giveKarma();
        save();
    }

    //TODO make sure photoUtils.releasePhoto() works as expected
    public Bitmap release(){
        photoUtils.releasePhoto();
        previousImages[imageIndex] = null;
        //adjusts the Photo array
        for(int index = imageIndex; index < 10; ++index){
            while(previousImages[imageIndex+1] != null){
                previousImages[index] = previousImages[index+1];
            }
        }
        //returns the bitmap of the next image
        save();
        return next();
    }

    public void releasePhotoThroughGallery(){
        photoUtils.releasePhoto();
        save();
    }

    //TODO implement algorithm
    public void save(){
        try {
            snappydb.put(IMAGE_INDEX, imageIndex);
            snappydb.put(PREV_IMAGES, previousImages);
            snappydb.put(PHOTO_ALBUM, photoAlbum);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    //TODO implement algorithm
    public void load() {
        try {
            if (snappydb.exists(IMAGE_INDEX)) {
                imageIndex = snappydb.getInt(IMAGE_INDEX);
            } else {
                imageIndex = 0;
            }

            if (snappydb.exists(PREV_IMAGES)) {
                previousImages = snappydb.getObjectArray(PREV_IMAGES, Photo.class);
            } else {
                previousImages = new Photo[previousArrSize];
            }

            if (snappydb.exists(PHOTO_ALBUM)) {
                photoAlbum = snappydb.getObject(PHOTO_ALBUM, List.class);
            } else {
                photoAlbum = photoUtils.getCameraPhotos();
            }

            snappydb.close();

        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}