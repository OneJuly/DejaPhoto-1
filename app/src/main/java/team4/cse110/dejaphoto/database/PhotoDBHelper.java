package team4.cse110.dejaphoto.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import team4.cse110.dejaphoto.database.PhotoDBSchema.PhotoTable;
import team4.cse110.dejaphoto.database.PhotoDBSchema.PrevIndexTable;

import static team4.cse110.dejaphoto.database.PhotoDBSchema.CacheTable;

/**
 * Created by Sean on 5/13/2017.
 */

public class PhotoDBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "dejaPhotoDatabase.db";

    public PhotoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /* Called when DB is created for the first time */
    @Override
    public void onCreate(SQLiteDatabase db) {

        /* Create main Photo table*/
        db.execSQL("create table " + PhotoTable.MAIN_NAME + "(" +
                "_id integer primary key autoIncrement, " +
                PhotoTable.Cols.UUID + ", " +
                PhotoTable.Cols.PATH+ ", " +
                PhotoTable.Cols.LAT + ", " +
                PhotoTable.Cols.LON + ", " +
                PhotoTable.Cols.KARMA + ", " +
                PhotoTable.Cols.WEIGHT + ")"
        );

        /* Create main Photo table*/
        db.execSQL("create table " + CacheTable.CACHE_NAME + "(" +
                "_id integer primary key autoIncrement, " +
                CacheTable.Cols.C_UUID + ", " +
                CacheTable.Cols.C_PATH+ ", " +
                CacheTable.Cols.C_LAT + ", " +
                CacheTable.Cols.C_LON + ", " +
                CacheTable.Cols.C_KARMA + ", " +
                CacheTable.Cols.C_WEIGHT + ")"
        );

        /* Create previous index table  */
        db.execSQL("create table " + PrevIndexTable.PREV_NAME + "(" +
                "_id integer primary autoIncrement, " +
                PrevIndexTable.Cols.INDEX + ")"
        );
    }

    /* TODO worry about this later? */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
