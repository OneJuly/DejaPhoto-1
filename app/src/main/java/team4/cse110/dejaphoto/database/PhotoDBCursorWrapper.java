package team4.cse110.dejaphoto.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import team4.cse110.dejaphoto.photo.Photo;
import team4.cse110.dejaphoto.database.PhotoDBSchema.PhotoTable;

/**
 * Created by Sean on 5/13/2017.
 */

public class PhotoDBCursorWrapper extends CursorWrapper {

    private Context mContext;

    /**
     * Creates a cursor wrapper.
     *
     * @param context
     * @param cursor The underlying cursor to wrap.
     */
    public PhotoDBCursorWrapper(Context context, Cursor cursor) {
        super(cursor);
        mContext = context;
    }

    public Photo getPhoto() {
        String uuidStr = getString(getColumnIndex(PhotoTable.Cols.UUID));
        String path = getString(getColumnIndex(PhotoTable.Cols.PATH));
        double lat = getDouble(getColumnIndex(PhotoTable.Cols.LAT));
        double lon = getDouble(getColumnIndex(PhotoTable.Cols.LON));
        double weight = getDouble(getColumnIndex(PhotoTable.Cols.WEIGHT));
        int karma = getInt(getColumnIndex(PhotoTable.Cols.KARMA));
        long time = getLong(getColumnIndex(PhotoTable.Cols.TIME));

        Photo photo = new Photo(mContext, path);
        photo.setId(UUID.fromString(uuidStr));
        photo.setPath(path);
        photo.setLat(lat);
        photo.setLon(lon);
        photo.setKarma(karma);
        photo.setWeight(weight);
        photo.setTime(time);

        return photo;
    }
}
