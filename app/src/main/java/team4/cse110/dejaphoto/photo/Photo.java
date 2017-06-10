package team4.cse110.dejaphoto.photo;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;

@IgnoreExtraProperties
public class Photo {

    @SuppressWarnings("unused")
    public static final String TAG = "Photo";

    private String customLoc;
    private String refPath;
    private String uid;
    private String localPath;
    public double weight;
    private double lat;
    private double lon;
    private long time;

    private Context context;

    public String getRefPath() {
        return refPath;
    }

    public void setRefPath(String refPath) {
        this.refPath = refPath;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getCustomLoc() {
        return customLoc;
    }

    public void setCustomLoc(String customLoc) {
        this.customLoc = customLoc;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return double representing the weight of the photo
     */
    public double calcWeight() {
        double weight = 300;
        if (is_same_time()) {
            weight += 100;
        }
        if(is_same_weekday()){
            weight += 100;
        }
        if (is_same_location()) {
            weight += 100;
        }
//        if(karma == 1){
//            weight += 200;
//        }
        if(is_recently_shown()){
            weight -= 200;
        }
        weight += recentlyTakenWeight();
        Log.v("weight", "weight+ " + weight);
        return weight;
    }

    @Exclude
    private Location getCurrentLocation() {
        // http://stackoverflow.com/questions/32491960/android-check-permission-for-locationmanager
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            Log.v("ImageAlgorithms", "getCurrentLocation(): SecurityException caught.");
            System.out.println("getCurrentLocajtion(): SecurityException caught.");
            return null;
        }
    }

    @Exclude
    private Calendar getCurrentDate() {
        try {
            return Calendar.getInstance();
        } catch (Exception e) {
            Log.v("ImageAlgorithms", "getCurrentDate(): Exception caught.");
            System.out.println("getCurrentDate(): Exception caught.");
            return null;
        }
    }

    @Exclude
    private boolean is_same_time() {
        //7,200,000 = milliseconds in 2 hours
        Calendar calendar = getCurrentDate();
        if (calendar == null) {
            return false;
        }
        return ((calendar.getTimeInMillis() - time) < 7200000 &&
                (calendar.getTimeInMillis() - time) > -7200000);
    }

    @Exclude
    private boolean is_same_weekday() {
        Calendar calendar = getCurrentDate();
        if (calendar == null) {
            return false;
        }

        return false;
    }

    @Exclude
    private boolean is_same_location() {

        Location photoLocation = new Location("");
        photoLocation.setLatitude(lat);
        photoLocation.setLongitude(lon);
        Location currLocation = getCurrentLocation();

        if (currLocation == null) {
            return false;
        }

        float distanceInMeters = photoLocation.distanceTo(currLocation);
        return (distanceInMeters < 5000);
    }

    @Exclude
    private boolean is_recently_shown() {
        return false;
    }

    @Exclude
    private double recentlyTakenWeight() {

        //creates a calendar object and error checks
        Calendar calendar = getCurrentDate();
        if (calendar == null) {
            return 0;
        }
        //604800000 = milliseconds in a week
        if ((calendar.getTimeInMillis() - time) < 604800000L) {
            return 200;
        }
        //2600640000L = milliseconds in a month
        else if ((calendar.getTimeInMillis() - time) < 2600640000L) {
            return 100;
        }
        //31449600000L = milliseconds in a year
        else if ((calendar.getTimeInMillis() - time) < 31449600000L) {
            return 50;
        }
        //returns 0 if photo was not taken within a year
        else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        if (Double.compare(photo.lat, lat) != 0) return false;
        if (Double.compare(photo.lon, lon) != 0) return false;
        if (time != photo.time) return false;
        if (customLoc != null ? !customLoc.equals(photo.customLoc) : photo.customLoc != null)
            return false;
        if (refPath != null ? !refPath.equals(photo.refPath) : photo.refPath != null) return false;
        return localPath != null ? localPath.equals(photo.localPath) : photo.localPath == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = customLoc != null ? customLoc.hashCode() : 0;
        result = 31 * result + (refPath != null ? refPath.hashCode() : 0);
        result = 31 * result + (localPath != null ? localPath.hashCode() : 0);
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }
}
