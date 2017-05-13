package team4.cse110.dejaphoto.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import team4.cse110.dejaphoto.Photo;
import team4.cse110.dejaphoto.database.PhotoDBSchema.PhotoTable;

/**
 * Created by Sean on 5/13/2017.
 */

public class PhotoDBCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public PhotoDBCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Photo getPhoto() {
        String uuidStr = getString(getColumnIndex(PhotoTable.Cols.UUID));
        String path = getString(getColumnIndex(PhotoTable.Cols.PATH));
//        Long date = getLong(getColumnIndex(PhotoTable.Cols.DATE));
        String lat = getString(getColumnIndex(PhotoTable.Cols.LAT));
        String lon = getString(getColumnIndex(PhotoTable.Cols.LON));
        Double weight = getDouble(getColumnIndex(PhotoTable.Cols.WEIGHT));
        int karma = getInt(getColumnIndex(PhotoTable.Cols.KARMA));
        int active = getInt(getColumnIndex(PhotoTable.Cols.ACTIVE));

        Photo photo = new Photo(UUID.fromString(uuidStr));
        photo.setPath(path);
        photo.setLat(lat);
        photo.setLon(lon);
        photo.setWeight(weight);
        photo.setKarma(karma);
        photo.setActive(active);

        return photo;
    }
}
