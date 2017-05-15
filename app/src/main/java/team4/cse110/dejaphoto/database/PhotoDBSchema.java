package team4.cse110.dejaphoto.database;

/**
 * Created by Sean on 5/13/2017.
 */

public class PhotoDBSchema {

    /* Main Photo table schema */
    public static final class PhotoTable {
        public static final String NAME = "DejaPhotoDB";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PATH = "path";
            public static final String LAT = "lat";
            public static final String LON = "long";
            public static final String KARMA = "karma";
            public static final String WEIGHT = "weight";
        }
    }

    /* Cache table schema */
    public static final class PrevIndexTable{
        public static final String NAME = "PrevIndex";

        public static final class Cols {
            public static final String INDEX = "index";
        }
    }
}
