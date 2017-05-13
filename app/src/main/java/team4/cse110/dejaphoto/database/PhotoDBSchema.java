package team4.cse110.dejaphoto.database;

/**
 * Created by Sean on 5/13/2017.
 */

public class PhotoDBSchema {

    public static final class PhotoTable {
        public static final String NAME = "photos";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PATH = "path";
//            public static final String DATE = "date";
            public static final String LAT = "lat";
            public static final String LON = "long";
            public static final String KARMA = "karma";
            public static final String WEIGHT = "weight";
            public static final String ACTIVE = "active";
        }
    }
}
