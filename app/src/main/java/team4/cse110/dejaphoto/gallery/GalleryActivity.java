package team4.cse110.dejaphoto.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import team4.cse110.dejaphoto.BaseActivity;
import team4.cse110.dejaphoto.R;
import team4.cse110.dejaphoto.database.FirebasePhotoDatabase;
import team4.cse110.dejaphoto.database.PhotoDBHelper;
import team4.cse110.dejaphoto.login.LoginActivity;
import team4.cse110.dejaphoto.photo.Photo;
import team4.cse110.dejaphoto.photo.PhotoAdapter;
import team4.cse110.dejaphoto.settings.PrefUtils;
import team4.cse110.dejaphoto.settings.SettingsActivity;

/**
 * This class sets up the app's homepage, where photos from the phone's camera
 * album will be displayed in a scrollable grid.
 */
@RuntimePermissions
public class GalleryActivity extends BaseActivity {

    private static final String TAG = "GalleryActivity";

    private static final int REQUEST_PHOTO = 9000;
    private static final int REQUEST_SETTINGS = 9001;
    private static final int REQUEST_GOOGLE_LOGIN = 9002;

    private static final int GRID_SPAN = 3; // number of columns for ImageViews

    private List<Photo> photos;
    private ArrayList<String> paths;
    private PrefUtils prefUtils;
    private MaterialSheetFab materialSheetFab;
    private FirebasePhotoDatabase firebasePhotoDatabase;

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

        // Get a reference to the Firebase database
        firebasePhotoDatabase = new FirebasePhotoDatabase(FirebaseDatabase.getInstance());

        // Get a preference prefUtils reference
        prefUtils = new PrefUtils(this);

        // Get a reference to the RecyclerView
        RecyclerView rvPhotos = (RecyclerView) findViewById(R.id.rv_gallery);
        photos = new ArrayList<>();

        // Clear database */
        deleteDatabase(PhotoDBHelper.DATABASE_NAME);


        // Hook up the adapter to the RecyclerView
        PhotoAdapter adapter = new PhotoAdapter(this, photos);
        rvPhotos.setAdapter(adapter);
        rvPhotos.setLayoutManager(new GridLayoutManager(this, GRID_SPAN));

        // Setup the floating action button
        initFAB();

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
                if (resultCode == Activity.RESULT_OK && data != null)
                {
                    paths = new ArrayList<>();
                    paths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));

                    for (String path : paths) {
                        Photo photo = new Photo(this, path);
                        firebasePhotoDatabase.addPhoto(photo);
                    }
                }

                break;

            case REQUEST_GOOGLE_LOGIN:
                break;
        }

    }


    /**
     *  Initialize the Floating Action Button
     */
    private void initFAB() {
        // Get reference to FAB, sheet, and overlay
        Fab fab = (Fab) findViewById(R.id.fab_gallery);
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
        startActivityForResult(takePhoto, REQUEST_PHOTO);
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
     * This method retrieves the external DejaPhoto album, or creates one if it
     * doesn't exist.
     * @param name - the name of the album.
     * @return the directory.
     */
/*    protected File getDejaAlbumDir(String name) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), name);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }*/
}