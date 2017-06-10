package team4.cse110.dejaphoto.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import team4.cse110.dejaphoto.photo.Photo;

/**
 * Created by Sean on 5/21/2017.
 */

public class FirebasePhotoDatabase implements DatabaseInterface{

    private static final String TAG = "FirebasePhotoDatabase";
    private static final String PHOTOS_CHILD = "photosList";

    private DatabaseReference dbRef;
    private StorageReference storageRef;
    private FirebaseUser user;

    public FirebasePhotoDatabase() {
        this.dbRef = FirebaseDatabase.getInstance().getReference();
        this.storageRef = FirebaseStorage.getInstance().getReference();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void addPhoto(Photo photo) {
        // Store photo in cloud and get pointer
        Uri file = Uri.fromFile(new File(photo.getLocalPath()));
        StorageReference photoRef = storageRef.child(user.getUid());
        photo.setStorageRef(photoRef);

        // see firebase.google.com/docs/storage/android/upload-files
        UploadTask uploadTask = storageRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

        // Add photo info to the database
        dbRef.child(user.getUid()).child(PHOTOS_CHILD)
                .child(String.valueOf(photo.hashCode())).setValue(photo);
    }

    @Override
    public void deletePhoto(Photo photo) {
        // Delete from the cloud
        StorageReference photoRef = photo.getStorageRef();
        photoRef.delete();

        // Remove photo info from database
        dbRef.child(user.getUid()).child(PHOTOS_CHILD)
                .child(String.valueOf(photo.hashCode())).removeValue();
    }

    @Override
    public void setSharePhotos(boolean value) {
    }

    @Override
    public List<Photo> getPhotoList() {
        final List<Photo> photoList = new ArrayList<>();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // nasty jank
                if (dataSnapshot.hasChildren()) {
                   for (DataSnapshot user : dataSnapshot.getChildren()){
                        if (user.hasChildren()) {

                            for (DataSnapshot photo : user.getChildren()) {
                                photoList.add(photo.getValue(Photo.class));
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return photoList;
    }

    @Override
    public Bitmap fetchBitmap(Photo photo) {
        StreamDownloadTask streamTask = photo.getStorageRef().getStream();
        final InputStream[] is = new InputStream[1];

        streamTask.addOnSuccessListener(new OnSuccessListener<StreamDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(StreamDownloadTask.TaskSnapshot taskSnapshot) {
                is[0] = taskSnapshot.getStream();
            }
        });

        return BitmapFactory.decodeStream(is[0]);

    }

    @Override
    public void storePreviousList(ArrayList<Photo> list) {

    }

    @Override
    public ArrayList<Photo> getPreviousList() {
        return null;
    }

    @Override
    public void storePreviousIndex(int index) {

    }

    @Override
    public int getPreviousIndex() {
        return 0;
    }

}


