package team4.cse110.dejaphoto.database;

/**
 * Created by Sean on 5/13/2017.
 */

public class PhotoDBSchema {

    /* Main Photo table schema */
    public static final class PhotoTable {
        public static final String MAIN_NAME = "DejaPhotoDB";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PATH = "path";
            public static final String LAT = "lat";
            public static final String LON = "long";
            public static final String KARMA = "karma";
            public static final String WEIGHT = "weight";
        }
    }

    /* Cached photos table schema */
    public static final class CacheTable {
        public static final String CACHE_NAME = "CacheDB";

        public static final class Cols {
            public static final String C_UUID = "uuid";
            public static final String C_PATH = "path";
            public static final String C_LAT = "lat";
            public static final String C_LON = "long";
            public static final String C_KARMA = "karma";
            public static final String C_WEIGHT = "weight";
        }
    }


    /* Previous index table schema */
    public static final class PrevIndexTable{
        public static final String PREV_NAME = "PrevIndex";

        public static final class Cols {
            public static final String INDEX = "index";
        }
    }
}
