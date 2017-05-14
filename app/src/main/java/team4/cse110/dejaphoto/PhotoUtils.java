package team4.cse110.dejaphoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import team4.cse110.dejaphoto.database.PhotoDBCursorWrapper;
import team4.cse110.dejaphoto.database.PhotoDBHelper;

import static team4.cse110.dejaphoto.database.PhotoDBSchema.PhotoTable;

/**
 * Singleton to store photos.
 */
public class PhotoUtils {
    private static PhotoUtils sPhotoUtils;

    private Context mContext;
    private File mAlbum;
    SQLiteDatabase mDatabase;

    private PhotoUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PhotoDBHelper(mContext).getWritableDatabase();
//        mAlbum = album;
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
     //    private static final String DB_NAME = "";
     * @return the list of all photos
     */
    public List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList<>();

        PhotoDBCursorWrapper cursor = queryPhotos(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                photos.add(cursor.getPhoto());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return photos;
    }

    /** * Returns a single Photo object according to the supplied UUID.
     *
     * @param id a photo's unique identifier
     * @return the photo with matching id
     */
    public Photo getPhoto(UUID id) {
        PhotoDBCursorWrapper cursor = queryPhotos(
                PhotoTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPhoto();
        } finally {
            cursor.close();
        }
    }

    /**
     * Adds a given Photo to the photo database and DejaPhoto album.
     *
     * @param photo the photo to be added
     */
    public void addPhoto(Photo photo) {
        ContentValues values = getContentValues(photo);
        mDatabase.insert(PhotoTable.NAME, null, values);

//        File original = new File(photo.getPath());
//        File added = new File(mAlbum.getAbsolutePath() + photo.getFileName());
//        original.renameTo(added);
    }

    /**
     * Update an existing database entry
     *
     * @param photo
     */
    public void updatePhoto(Photo photo) {
        String uuidString = photo.getId().toString();
        ContentValues values = getContentValues(photo);

        mDatabase.update(PhotoTable.NAME, values, PhotoTable.Cols.UUID + " = ?",
                // use '?': good habit to prevent sql injection
                new String[] { uuidString });
    }

    /**
     *
     * @param photo
     * @return
     */
    private static ContentValues getContentValues(Photo photo) {
        ContentValues values = new ContentValues();

        values.put(PhotoTable.Cols.UUID, photo.getId().toString());
        values.put(PhotoTable.Cols.PATH, photo.getPath());
        values.put(PhotoTable.Cols.LAT, photo.getLat());
        values.put(PhotoTable.Cols.LON, photo.getLon());
        values.put(PhotoTable.Cols.KARMA, photo.hasKarma() ? 1 : 0);
        values.put(PhotoTable.Cols.WEIGHT, photo.getWeight());
        values.put(PhotoTable.Cols.ACTIVE, photo.isActive() ? 1 : 0);

        return values;
    }

    private PhotoDBCursorWrapper queryPhotos(String whereClause, String[] whereArgs) {
        Cursor cursor =
                mDatabase.query(PhotoTable.NAME, null, // columns parameter - null selects all columns
                        whereClause, whereArgs, null, // groupBy
                        null, // having
                        null  // orderBy
                );

        return new PhotoDBCursorWrapper(cursor);
    }


    /* Populate the database with all pictures in camera roll */
    public void initFromCameraRoll() {

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };

        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";

        String[] selectionArgs = new String[] {
                "Camera"
        };

        final String orderDate = MediaStore.Images.Media.DATE_ADDED;

        // get cursor over query
        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, orderDate);

        // number of camera images
        int numPhotos = cursor.getCount();

        // image paths
        String[] arrPath = new String[numPhotos];
        String[] arrLat = new String[numPhotos];
        String[] arrLon = new String[numPhotos];

        for (int i = 0; i < numPhotos; i++) {
            cursor.moveToPosition(i);
            int pathColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//            int latColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
//            int lonColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);

            // init photo to add to the database
            arrPath[i]= cursor.getString(pathColumnIndex);

            Photo photo = new Photo(mContext, arrPath[i]);
            photo.setActive(1);
            photo.setKarma(0);
            photo.setWeight(1);

            /* insert photo into db */
            addPhoto(photo);

        }
    }
}