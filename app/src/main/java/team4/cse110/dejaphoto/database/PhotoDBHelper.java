package team4.cse110.dejaphoto.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import team4.cse110.dejaphoto.database.PhotoDBSchema.PhotoTable;

/**
 * Created by Sean on 5/13/2017.
 */

public class PhotoDBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "dejaPhotoDatabase.db";

    public PhotoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /* Called when DB is created for the first time */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PhotoTable.NAME + "(" +
                "_id integer primary key autoIncrement, " +
                PhotoTable.Cols.UUID + ", " +
//                PhotoTable.Cols.DATE+ ", " +
                PhotoTable.Cols.PATH+ ", " +
                PhotoTable.Cols.LAT+ ", " +
                PhotoTable.Cols.LON + ", " +
                PhotoTable.Cols.KARMA+ ", " +
                PhotoTable.Cols.ACTIVE + ", " +
                PhotoTable.Cols.WEIGHT + ")"
        );
    }

    /* TODO worry about this later? */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
