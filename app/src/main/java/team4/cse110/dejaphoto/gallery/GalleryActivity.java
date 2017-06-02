package team4.cse110.dejaphoto.gallery;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.views.SmoothCheckBox;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import team4.cse110.dejaphoto.BaseActivity;
import team4.cse110.dejaphoto.R;
import team4.cse110.dejaphoto.database.FirebasePhotoDatabase;
import team4.cse110.dejaphoto.login.LoginActivity;
import team4.cse110.dejaphoto.photo.Photo;
import team4.cse110.dejaphoto.settings.PrefUtils;
import team4.cse110.dejaphoto.settings.SettingsActivity;

/**
 * This class sets up the app's homepage, where photos from the phone's camera
 * album will be displayed in a scrollable grid.
 */
@RuntimePermissions
public class GalleryActivity extends BaseActivity {

    private static final String TAG = "GalleryActivity";

    private static final int REQUEST_CAMERA = 9000;
    private static final int REQUEST_SETTINGS = 9001;
    private static final int REQUEST_GOOGLE_LOGIN = 9002;

    private static final int GRID_SPAN = 3; // number of columns for ImageViews
    private static final int MAX_COUNT_PICKER = 20;  // max number of photos selectable in filepicker

    private List<Photo> photos;
    private ArrayList<String> paths;
    private PrefUtils prefUtils;
    private MaterialSheetFab materialSheetFab;

    private FirebasePhotoDatabase fbPhotoDatabase;
    private StorageReference storageRef;
    private DatabaseReference fbRef;
    private RecyclerView.Adapter fbAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_gallery;
    }

    /**
     * This method sets up the app's home page with thumbnails of the photos
     * from the camera album.
     *
     * @param savedInstanceState - the previously saved state of the app.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the Firebase database
        fbPhotoDatabase = new FirebasePhotoDatabase(FirebaseDatabase.getInstance());
        fbRef = FirebaseDatabase.getInstance().getReference().child("local-photos");

        // Get the cloud storage reference for remote photos
        storageRef = FirebaseStorage.getInstance().getReference();


        // Get a preference prefUtils reference
        prefUtils = new PrefUtils(this);

        // Get a reference to the RecyclerView
        RecyclerView rvPhotos = (RecyclerView) findViewById(R.id.rv_gallery);
        rvPhotos.setHasFixedSize(false);
        rvPhotos.setLayoutManager(new GridLayoutManager(this, GRID_SPAN));

        fbAdapter = new FirebaseRecyclerAdapter<Photo, PhotoHolder>(Photo.class, R.layout.photo_viewholder,
                PhotoHolder.class, fbRef) {

            @Override
            protected void populateViewHolder(PhotoHolder holder, Photo photo, int position) {
                // Load images
                // TODO handle the case where file doesn't exist; currently adds empty view
                Glide
                        .with(GalleryActivity.this)
                        .load(photo.getPath())
                        .into(holder.photo);

            }

            @Override
            protected void onDataChanged() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    hideProgressDialog();
                }

            }
        };

        // Hook up the adapter
        showProgressDialog();
        rvPhotos.setAdapter(fbAdapter);

        photos = new ArrayList<>();

        // Setup the floating action button
        initFAB();

    }

    /**
     * Custom Photo PhotoHolder
     */
    public static class PhotoHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private ImageView photo;
        private SmoothCheckBox checkBox;

        public PhotoHolder(final View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.gallery_photo);
            checkBox = (SmoothCheckBox) itemView.findViewById(R.id.gallery_checkbox);

            // TODO Save the checkBox/selection overlay on rotation!
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox cb, boolean isChecked) {
                    toggleSelected(cb, isChecked);

                    // dim/undim photo if selected/unselected
                    if (isChecked) {
                        photo.setColorFilter(R.color.bg_gray);
                    } else {
                        photo.clearColorFilter();
                    }
                }
            });

        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {
                checkBox.setChecked(!checkBox.isChecked(), true);
            }
        }

        private void toggleSelected(SmoothCheckBox cb, boolean isChecked) {
            cb.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            itemView.setBackgroundResource(isChecked? R.color.bg_gray : android.R.color.white);
        }

    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.menu_settings:
                // Start the default Settings activity
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsIntent, REQUEST_SETTINGS);
                break;

            case R.id.menu_google_login:
                // Start the Google login activity
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivityForResult(loginIntent, REQUEST_GOOGLE_LOGIN);


        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get incoming activity results.
     *
     * @param requestCode - TODO
     * @param resultCode - TODO
     * @param data - TODO
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == RESULT_OK && data != null)
                {
                    paths = new ArrayList<>();
                    paths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));

                    for (String path : paths) {
                        Photo photo = new Photo(this, path);
                        fbPhotoDatabase.addPhoto(photo);

                        // TODO enable this only if user wants to share photos?
                        addToRemoteStorage(photo);
                    }
                }

                break;

            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK && data != null) {
                    Uri image = data.getData();
                    if (image != null) {
                        String path = getPathFromUri(image);
                        fbPhotoDatabase.addPhoto(new Photo(this, path));
                    }
                }
                break;
        }

    }

    // Get absolute filepath from image URI returned from camera intent
    private String getPathFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
        return cursor.getString(index);
    }


    /**
     *  Initialize the Floating Action Button
     */
    private void initFAB() {
        // Get reference to FAB, sheet, and overlay
        GalleryFab fab = (GalleryFab) findViewById(R.id.fab_gallery);
        View sheet = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.fab_overlay);
        int sheetColor = getResources().getColor(R.color.colorFabSheet);
        int fabColor = getResources().getColor(R.color.colorAccent);

        materialSheetFab = new MaterialSheetFab<>(fab, sheet, overlay, sheetColor, fabColor);

        // Hook up FAB action click listeners
        TextView pickPhotosTextView = (TextView) findViewById(R.id.fab_sheet_pick_photos);
        TextView startCameraTextView = (TextView) findViewById(R.id.fab_sheet_camera);

        // Pick photos
        pickPhotosTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delegate to compiled permissions handling method
                GalleryActivityPermissionsDispatcher.pickPhotosWithCheck(GalleryActivity.this);
            }
        });

        // Launch camera app
        startCameraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryActivityPermissionsDispatcher.showCameraWithCheck(GalleryActivity.this);
            }
        });
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    protected void showCamera() {
        final Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhoto, REQUEST_CAMERA);
    }

    /**
     *
     */
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    protected void pickPhotos() {

        // Instantiate an interactive photo picker
        FilePickerBuilder.getInstance()
                .setSelectedFiles(paths)
                .setActivityTheme(R.style.AppTheme)
                .setMaxCount(MAX_COUNT_PICKER)
                .pickPhoto(GalleryActivity.this);

    }

    /****************************** Permissions Handling ******************************/

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForReadStorage(final PermissionRequest request) {
        showRationaleDialog(R.string.permissions_read_storage_rationale, request);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
        showRationaleDialog(R.string.permission_camera_rationale, request);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GalleryActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * Helper to show permissions rationale dialogs
     *
     * @param messageResId
     * @param request
     */
    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();

    }

    /**
     *
     * @param photo
     */
    private void addToRemoteStorage(Photo photo) {
        Uri file = Uri.fromFile(new File(photo.getPath()));
        StorageReference remote =
                storageRef.child(photo.getUserUID() + "/photos/"  + file.getLastPathSegment());

        // see firebase.google.com/docs/storage/android/upload-files
        UploadTask uploadTask = remote.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // TODO handle failed upload

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(GalleryActivity.this, "Success!", Toast.LENGTH_SHORT).show();

            }
        });


    }
}