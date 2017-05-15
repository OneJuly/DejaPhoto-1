package team4.cse110.dejaphoto.database;

import android.database.Cursor;
import android.database.CursorWrapper;

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
        String lat = getString(getColumnIndex(PhotoTable.Cols.LAT));
        String lon = getString(getColumnIndex(PhotoTable.Cols.LON));
        Double weight = getDouble(getColumnIndex(PhotoTable.Cols.WEIGHT));
        int karma = getInt(getColumnIndex(PhotoTable.Cols.KARMA));

        Photo photo = new Photo(path);
        photo.setPath(path);
        photo.setLat(lat);
        photo.setLon(lon);
        photo.setKarma(karma);
        photo.setWeight(weight);

        return photo;
    }
}
