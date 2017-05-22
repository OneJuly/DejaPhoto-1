package team4.cse110.dejaphoto.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import team4.cse110.dejaphoto.Photo;

/**
 * Created by Sean on 5/21/2017.
 */

public class FirebasePhotoDatabase implements PhotoDatabase {

    private final DatabaseReference photosDB;

    public FirebasePhotoDatabase(FirebaseDatabase firebaseDatabase) {
        photosDB = firebaseDatabase.getReference("photos");
    }

    @Override
    public List<Photo> getPhotos() {
        return null;
    }

    @Override
    public void removePhoto(Photo p) {

    }

    @Override
    public void addPhoto(Photo p) {
        photosDB.child(String.valueOf(p.getId())).setValue(p);
    }

    @Override
    public List<Photo> getCache() {
        return null;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public boolean updatePhoto(Photo p) {
        return false;
    }

    @Override
    public void setCache(List<Photo> cache) {

    }

    @Override
    public void setPosition(int pos) {

    }
}
