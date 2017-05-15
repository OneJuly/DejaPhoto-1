package team4.cse110.dejaphoto.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import team4.cse110.dejaphoto.database.PhotoDBSchema.PhotoTable;
import team4.cse110.dejaphoto.database.PhotoDBSchema.PrevIndexTable;

/**
 * Created by Sean on 5/13/2017.
 */

public class PhotoDBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
//    public static final String DATABASE_NAME = "dejaPhotoDatabase.db";

    public PhotoDBHelper(Context context, String dbName) {
        super(context, dbName, null, VERSION);
    }

    /* Called when DB is created for the first time */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PhotoTable.NAME + "(" +
                "_id integer primary key autoIncrement, " +
                PhotoTable.Cols.UUID + ", " +
                PhotoTable.Cols.PATH+ ", " +
                PhotoTable.Cols.LAT+ ", " +
                PhotoTable.Cols.LON + ", " +
                PhotoTable.Cols.KARMA + ", " +
                PhotoTable.Cols.WEIGHT + ")"
        );

        db.execSQL("create table " + PrevIndexTable.NAME + "(" +
                "_id integer primary key autoIncrement, " +
                PrevIndexTable.Cols.INDEX + ", " + ")"
        );
    }

    /* TODO worry about this later? */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
