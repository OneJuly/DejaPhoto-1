package team4.cse110.dejaphoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import team4.cse110.dejaphoto.database.PhotoDBCursorWrapper;
import team4.cse110.dejaphoto.database.PhotoDBHelper;

import static team4.cse110.dejaphoto.database.PhotoDBSchema.PhotoTable;

/**
 * Singleton to store Photos and Photo-related utilities.
 */
public class PhotoUtils {
    private static final String TAG = "PhotoUtils";
    private static int imageIndex; // last image

    private static PhotoUtils sPhotoUtils;

    private Context mContext;
    SQLiteDatabase mDatabase;

    private PhotoUtils(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PhotoDBHelper(mContext).getWritableDatabase();
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

        //TODO copy image file to app ext dir
    }

    /**
     * Removes a given Photo from the database
     *
     * @param photo the photo to be added
     */
    public void removePhoto(Photo photo) {
        String id = photo.getId().toString();

        mDatabase.delete(PhotoTable.NAME, PhotoTable.Cols.UUID + " =?",
                new String[] { id });
    }

    /**
     * Update an existing database entry
     *
     * @param photo
     */
    public void updatePhoto(Photo photo) {
        String id = photo.getId().toString();
        ContentValues values = getContentValues(photo);

        mDatabase.update(PhotoTable.NAME, values, PhotoTable.Cols.UUID + " = ?",

                new String[] { id });
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


    /**
     * Build a bitmap from a given Photo object
     *
     * @param photo
     * @return
     */
    Bitmap getBitmap(Photo photo) {
        return BitmapFactory.decodeFile(photo.getPath());
    }

    /**
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    private PhotoDBCursorWrapper queryPhotos(String whereClause, String[] whereArgs) {
        Cursor cursor =
                mDatabase.query(PhotoTable.NAME, null, // columns parameter - null selects all columns
                        whereClause, whereArgs, null, // groupBy
                        null, // having
                        null  // orderBy
                );

        return new PhotoDBCursorWrapper(cursor);
    }

    /**
     *
     * @return
     */
    public Bitmap next() {

        List<Photo> album = getPhotos();

        /* Check if the photos db is empty */
        if (album.isEmpty()) {
            return null;
        }

        /* Only one Photo in the db */
        if (album.size() == 1) {
            return getBitmap(album.get(0));
        }

        return null;

    }

}