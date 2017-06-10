package team4.cse110.dejaphoto.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import team4.cse110.dejaphoto.photo.Photo;

/**
 * This class connects the app with FireBase and keeps tracks of the photos in
 * the display cycle.
 */

public class FirebasePhotoDatabase implements PhotoDatabase {

    private static final String TAG = "FirebasePhotoDatabase";

    private static final String LOCAL_DIR = "local-photos";

    private DatabaseReference localPhotos;
    private StorageReference fbStorageRef;
    private FirebaseUser user;

    /**
     * This constructor initializes the user and storage.
     * @param firebaseDatabase
     */
    public FirebasePhotoDatabase(FirebaseDatabase firebaseDatabase) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            localPhotos = firebaseDatabase.getReference().child(LOCAL_DIR).child(uid);
            fbStorageRef = FirebaseStorage.getInstance().getReference();
        }
    }

    /**
     * This method retrieves a list of photos to be placed in the display
     * cycle.
     * @return the list of photos to be placed in the display cycle.
     */
    @Override
    public List<Photo> getPhotos() {
        return null;
    }  // TODO - ?????

    /**
     * Delete a photo from the display cycle.
     * @param p photo to delete
     */
    @Override
    public void removePhoto(Photo p) {

    }

    /**
     * Add a photo to the display cycle.
     * @param p the Photo to add
     */
    @Override
    public void addPhoto(Photo p) {
        localPhotos.child(String.valueOf(p.hashCode())).setValue(p);

        // TODO - add to remote storage? Only if sharing enabled?
        // fbStorageRef.putFile(...)
    }

    /**
     * This method retrieves the list of photos to display.
     * @return the list of photos to display.
     */
    @Override
    public List<Photo> getCache() {
        return null;
    }

    /**
     * This method identifies the position of a photo in the cache.
     * @return the position of the photo.
     */
    @Override
    public int getPosition() {
        return 0;
    }

    /**
     * Signals to the current photo to update its info.
     * @param p photo to update
     * @return true if updated successfully; false otherwise.
     */
    @Override
    public boolean updatePhoto(Photo p) {
        return false;
    }

    /**
     * Create a new list of photos to be displayed.
     * @param cache new cache
     */
    @Override
    public void setCache(List<Photo> cache) {

    }

    /**
     * Put a photo in a particular position in the cache.
     * @param pos new position
     */
    @Override
    public void setPosition(int pos) {

    }
}
