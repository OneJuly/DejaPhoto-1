package team4.cse110.dejaphoto.database;

import android.graphics.Bitmap;
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
        this.storageRef = FirebaseStorage.getInstance().getReference("/users/");
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void addPhoto(final Photo photo) {
        // Store photo in cloud and get pointer
        Uri file = Uri.fromFile(new File(photo.getLocalPath()));
        final StorageReference photoRef = storageRef.child(user.getUid()
                + "/" + file.getLastPathSegment());
        photoRef.putFile(file);
        photo.setUid(user.getUid());
        photo.setRefPath(photo.getUid() + file.getLastPathSegment());

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
//        dbRef.child(user.getUid()).child(PHOTOS_CHILD)
//                .setValue(photo);
        dbRef.child(user.getUid()).child("photosList").child(
        String.valueOf(photo.hashCode())).setValue(photo);
    }

    @Override
    public void deletePhoto(Photo photo) {
        // Delete from the cloud

        StorageReference photoRef = storageRef.child(user.getUid()
                + "/" + photo.getRefPath());
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

                // nasty jank shit
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
        StorageReference ref =
                storageRef.child(photo.getRefPath());

        StreamDownloadTask streamTask =
                storageRef.child(String.valueOf(ref)).getStream();

        streamTask.addOnSuccessListener(new OnSuccessListener<StreamDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(StreamDownloadTask.TaskSnapshot taskSnapshot) {
                //getStream();
            }
        });

        return null;
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

    private void addToRemoteStorage(Photo photo) {

    }
}


