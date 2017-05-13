package team4.cse110.dejaphoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.UUID;

import team4.cse110.dejaphoto.database.PhotoDBHelper;

/**
 * Singleton to store photos.
 */
public class PhotoUtilities {
    private static PhotoUtilities sPhotoUtilities;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private PhotoUtilities(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PhotoDBHelper(mContext).getWritableDatabase();
    }

    /**
     * Returns a single instance of PhotoUtilities. Creates and returns an
     * instance if one doesn't exist.
     *
     * @param context the application context
     * @return a PhotoUtilities instance
     */
    public static PhotoUtilities getInstance(Context context) {
        if (sPhotoUtilities == null) {
            sPhotoUtilities = new PhotoUtilities(context);
        }
        return sPhotoUtilities;
    }

    /**
     * Returns a List containing all the current Crime objects.
     *
     * @return the list of all crimes
     */
    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    /** * Returns a single Crime object according to the supplied UUID.
     *
     * @param id a crime's unique identifier
     * @return the crime with matching id
     */
    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    /**
     * Adds a given Crime to the crime database.
     *
     * @param c the crime to be added
     */
    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?",
                // use '?': good habit to prevent sql injection
                new String[] { uuidString });
    }

    /**
     *
     * @param crime
     * @return
     */
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();

        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        values.put(CrimeTable.Cols.TEL, crime.getTel());

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor =
                mDatabase.query(CrimeTable.NAME, null, // columns parameter - null selects all columns
                        whereClause, whereArgs, null, // groupBy
                        null, // having
                        null  // orderBy
                );

        return new CrimeCursorWrapper(cursor);
    }
}