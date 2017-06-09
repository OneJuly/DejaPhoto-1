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
 * Created by Sean on 5/21/2017.
 */

public class FirebasePhotoDatabase implements PhotoDatabase {

    private static final String TAG = "FirebasePhotoDatabase";

    private static final String LOCAL_DIR = "local-photos";

    private DatabaseReference localPhotos;
    private StorageReference fbStorageRef;
    private FirebaseUser user;

    public FirebasePhotoDatabase(FirebaseDatabase firebaseDatabase) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            localPhotos = firebaseDatabase.getReference().child(LOCAL_DIR).child(uid);
            fbStorageRef = FirebaseStorage.getInstance().getReference();
        }
    }

    @Override
    public List<Photo> getPhotos() {
        return null;
    }  // TODO - ?????

    @Override
    public void removePhoto(Photo p) {

    }

    @Override
    public void addPhoto(Photo p) {
        localPhotos.child(String.valueOf(p.hashCode())).setValue(p);

        // TODO add to remote storage? only if sharing enabled?
        // fbStorageRef.putFile(...)
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
