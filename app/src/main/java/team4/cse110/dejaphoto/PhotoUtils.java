package team4.cse110.dejaphoto;

import android.content.Context;
import android.media.ExifInterface;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import team4.cse110.dejaphoto.database.FirebasePhotoDatabase;
import team4.cse110.dejaphoto.photo.Photo;


/**
 * Singleton to store Photos and Photo-related utilities.
 */
public class PhotoUtils {
    private static final String TAG = "PhotoUtils";

    private static PhotoUtils sPhotoUtils;

    private Context mContext;
    FirebasePhotoDatabase mDatabase;

    private PhotoUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new FirebasePhotoDatabase();
    }

    /**
     * Returns a single instance of PhotoUtils. Creates and returns an
     * instance if one doesn't exist.
     *
     * @param context the application context
     * @return a PhotoUtils instance
     */
    public static PhotoUtils getInstance(Context context) {
        if (sPhotoUtils == null) {
            sPhotoUtils = new PhotoUtils(context);
        }

        return sPhotoUtils;
    }

    /**
     * Returns a List containing all the current Photo objects.
     *
     * @return the list of all photos
     */
    public List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList<>();


        return photos;
    }

    /**
     * Returns a list of Photos that were previously used as wallpaper.
     *
     * @return
     */
    public List<Photo> getCache() {
        List<Photo> cache = new ArrayList<>();

        return cache;
    }


    /**
     * Adds a given Photo to the photo database and DejaPhoto album.
     *
     * @param table
     * @param photo the photo to be added
     */
    public void addPhoto(String table, Photo photo) {

        File file = new File(photo.getLocalPath());
        ExifInterface exif = null;

        /* Date formatter to convert Exif date tags */
        SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

        Date date = null;
        float[] latlon = new float[2];

        /*  Make sure the photo exists */
        try {
            exif = new ExifInterface(photo.getLocalPath());

            try {
                /* Parse the Exif date according to SDF pattern */
                date = df.parse(exif.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED));

                /* Get latitude/longitude data */
                exif.getLatLong(latlon);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        /* Set the Photo time in millis */
        if (date != null) {
            photo.setTime(date.getTime());
        }

        /* Set latitude and longitude information */
        if (latlon != null) {
            photo.setLat((double) latlon[0]);
            photo.setLat((double) latlon[1]);
        }

        /* Add the Photo to the Database */
//        mDatabase.insert(table, null, values);
    }

    /**
     * Removes a given Photo from the database
     *
     * @param photo the photo to be added
     */
    public void removePhoto(Photo photo) {
//        String id = photo.getId().toString();

/*        mDatabase.delete(MAIN_NAME, PhotoTable.Cols.UUID + " =?",
                new String[] { id });*/
    }



}
