/**
 * Created by Alisa & Sam on 5/11/17.
 */

package team4.cse110.dejaphoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//

//TODO notes:
//make the bitmap array an array of URI's?
//account for recently shown photos (weight) in the DJV/Random methods or in the photo class?
//create member variables having to do with current time/day/location?

public class ImageAlgorithms implements Algorithm {

    private static final String DB_NAME = "AlgorithmDB";
    private static final String IMAGE_INDEX= "imageIndex";
    private static final String PREV_IMAGES = "previousImages";
    private static final String PHOTO_ALBUM = "photoAlbum";

    // Store up to 10 photos in history.
    private static final int previousArrSize = 11;

    //////////////////// MEMBER VARIABLES AND CONSTRUCTORS ////////////////////

    private Context context;
    private int imageIndex;
    private Photo[] previousImages;
    private List<Photo> photoAlbum;

    private PhotoUtils photoUtils;
    private PrefUtils prefUtils;
    private DB snappydb;

    /**
     * Constructor.
     * @param context - environment data.
     */
    public ImageAlgorithms(Context context) {
        this.context = context;
        photoUtils = new PhotoUtils(context);
        prefUtils = new PrefUtils();

        try {
            load();
        } catch (SnappydbException e) {
            e.printStackTrace();
            previousImages = new Photo[previousArrSize];
            photoAlbum = photoUtils.getCameraPhotos();
            imageIndex = 0;
        }

        // Get SnappyDB reference.
        try {
            snappydb = DBFactory.open(context, DB_NAME);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    //////////////////// HELPER METHODS ////////////////////

    /**
     * This method checks if DejaVu mode is enabled.
     * @return true if DejaVu mode if enable; false otherwise.
     */
    private boolean is_DJV_Enabled() {
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return prefUtils.dejaVuEnabled(context);
    }

    /**
     * This method checks the current date.
     * @return the current date.
     */
    public Calendar getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }

    /**
     * This method checks if a photo has been given karma.
     * @return true if the photo has karma; false otherwise.
     */
    public boolean hasKarma(){
        // Edge case - no image
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

    /**
     * This method checks the current location of the user.
     * @return the current location of the user.
     */
    public Location getCurrentLocation() {
        //TODO take out the following if statement and fix code

        // http://stackoverflow.com/questions/32491960/android-check-permission-for-locationmanager
        LocationManager locationManager;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }
        catch (SecurityException e) {
            return null;
        }

        /*
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
        */
    }

    //////////////////// DJV() & RANDOM() ALGORITHM ////////////////////

    //http://stackoverflow.com/questions/6737283/weighted-randomness-in-java
    /**
     * This method randomly selects the next photo to be displayed, based on
     * weight.
     * @return the next photo to be displayed.
     */
    private Photo DJV_algorithm(){

        // Get the total weight.
        double totalWeight = 0.0d;
        for(Photo photo : photoAlbum){
            totalWeight += photo.calcWeight(getCurrentDate(), getCurrentLocation());
        }
        // Choose a random photo.
        int randomIndex = -1;
        double random = Math.random() * totalWeight;
        for(int i = 0; i < photoAlbum.size(); ++i){
            random -= photoAlbum.get(i).getWeight();
            if(random < 0.0d){
                randomIndex = i;
                break;
            }
        }
        // Return a Photo object.
        return photoAlbum.get(randomIndex);
    }

    /**
     * This method randomly selected the next photo to be displayed.
     * @return the next photo to be displayed.
     */
    private Photo random_algorithm(){
        // Generate a random number.
        double random = Math.random() * (photoAlbum.size());
        // Convert that number into an int.
        int randomIndex = (int) random;
        // Retrieve the photo at that index of the collection.
        return photoAlbum.get(randomIndex);
    }

    //////////////////// BUTTON FUNCTIONALITY ////////////////////

    //gets called for the next image

    /**
     * This method TODO
     * @return
     */
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

    /**
     * This method TODO
     * @return
     */
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

    /**
     * This method gives karma to a photo.
     */
    public void incKarma(){
        previousImages[imageIndex].giveKarma();
        save();
    }

    //TODO make sure photoUtils.releasePhoto() works as expected

    /**
     * This method deletes a photo from the display cycle.
     * @return TODO
     */
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

    /**
     * This method releases a photo from the display gallery TODO
     */
    public void releasePhotoThroughGallery(){
        photoUtils.releasePhoto();
        save();
    }

    /**
     * This method retrieves the location where a photo was taken.
     * @return the location where the photo was taken.
     */
    public String getAddress(){
        String cityName = "address:______";
        Photo currPhoto = previousImages[imageIndex];
        Location location = currPhoto.getLocation(); //TODO get the location
        if(location == null){
            return cityName;
        }
        else{
            Geocoder geo = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> address = geo.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                if (address.size() > 0) {
                    //System.out.println(address.get(0).getLocality());
                    cityName = address.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return cityName;
        }
    }

    //TODO implement algorithm

    /**
     * This method TODO
     */
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

    /**
     * This method TODO
     * @throws SnappydbException if TODO
     */
    public void load() throws SnappydbException {
        try {
            imageIndex = snappydb.getInt(IMAGE_INDEX);
            previousImages = snappydb.getObjectArray(PREV_IMAGES, Photo.class);
            photoAlbum = snappydb.getObject(PHOTO_ALBUM, List.class);
        } catch (SnappydbException e) {
            throw e;
        }
    }
}