package team4.cse110.dejaphoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import team4.cse110.dejaphoto.database.PhotoDBCursorWrapper;
import team4.cse110.dejaphoto.database.PhotoDBHelper;
import team4.cse110.dejaphoto.database.PhotoDBSchema.CacheTable;
import team4.cse110.dejaphoto.database.PhotoDBSchema.PrevIndexTable;

import static team4.cse110.dejaphoto.database.PhotoDBSchema.PhotoTable;

/**
 * Singleton to store Photos and Photo-related utilities.
 */
public class PhotoUtils implements  PhotoDB {
    private static final String TAG = "PhotoUtils";

    private static PhotoUtils sPhotoUtils;

    private Context mContext;
    SQLiteDatabase mDatabase;
    SQLiteDatabase mCache;

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
     * @return the list of all photos
     */
    public List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList<>();

        PhotoDBCursorWrapper cursor = queryMain(null, null);

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

    /**
     * Returns a list of Photos that were previously used as wallpaper.
     *
     * @return
     */
    public List<Photo> getCache() {
        List<Photo> cache = new ArrayList<>();

        PhotoDBCursorWrapper cursor = queryCache(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cache.add(cursor.getPhoto());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return cache;
    }


    @Override
    public int getPosition() {
        return 0;
    }

    /** * Returns a single Photo object according to the supplied UUID.
     *
     * @param id a photo's unique identifier
     * @return the photo with matching id
     */
    public Photo getPhoto(UUID id) {
        PhotoDBCursorWrapper cursor = queryMain(
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

        File file = new File(photo.getPath());
        ExifInterface exif = null;
        String date;
        double lat;

        if (file != null) {
            try {
                exif = new ExifInterface(photo.getPath());

                if (exif != null) {
                    date = exif.getAttribute(ExifInterface.TAG_DATETIME);

                    if (date != null)
                        Log.v(TAG, "DATE: " + date.toString());

                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


        ContentValues values = getContentValues(photo);
        mDatabase.insert(PhotoTable.MAIN_NAME, null, values);
    }

    /**
     * Removes a given Photo from the database
     *
     * @param photo the photo to be added
     */
    public void removePhoto(Photo photo) {
        String id = photo.getId().toString();

        mDatabase.delete(PhotoTable.MAIN_NAME, PhotoTable.Cols.UUID + " =?",
                new String[] { id });
    }

    /**
     * Update an existing database entry
     *
     * @param photo
     */
    public boolean updatePhoto(Photo photo) {
        String id = photo.getId().toString();
        ContentValues values = getContentValues(photo);

        int updated = mDatabase.update(PhotoTable.MAIN_NAME, values, PhotoTable.Cols.UUID + " = ?",

                new String[] { id });

        return updated > 0;
    }

    /**
     *
     * @param cache
     */
    @Override
    public void setCache(List<Photo> cache) {

        /* Erase all data in current cache */
        mDatabase.delete(CacheTable.CACHE_NAME, null, null);

        /* Populate cache from Photo list*/
        for (Photo p : cache) {
            mDatabase.insert(CacheTable.CACHE_NAME, null, getContentValues(p);
        }

    }

    /**
     *
     * @param pos
     */
    @Override
    public void setPosition(int pos) {
        ContentValues values = new ContentValues();
        values.put(PrevIndexTable.Cols.IDX, pos);

        mDatabase.update(PrevIndexTable.PREV_NAME,values, PrevIndexTable.Cols.IDX + " = ?",
                new String[] {String.valueOf(pos)});
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
        values.put(PhotoTable.Cols.KARMA, photo.getKarma());
        values.put(PhotoTable.Cols.WEIGHT, photo.getWeight());

        return values;
    }

    /**
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    private PhotoDBCursorWrapper queryMain(String whereClause, String[] whereArgs) {
        Cursor cursor =
                mDatabase.query(PhotoTable.MAIN_NAME, null, // columns parameter - null selects all columns
                        whereClause, whereArgs, null, // groupBy
                        null, // having
                        null  // orderBy
                );

        return new PhotoDBCursorWrapper(cursor);
    }

    /**
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    private PhotoDBCursorWrapper queryCache(String whereClause, String[] whereArgs) {
        Cursor cursor =
                mCache.query(PhotoTable.MAIN_NAME, null, // columns parameter - null selects all columns
                        whereClause, whereArgs, null, // groupBy
                        null, // having
                        null  // orderBy
                );

        return new PhotoDBCursorWrapper(cursor);

    }


}