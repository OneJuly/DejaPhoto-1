package team4.cse110.dejaphoto.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;
import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerConst;
import team4.cse110.dejaphoto.BaseActivity;
import team4.cse110.dejaphoto.R;
import team4.cse110.dejaphoto.database.FirebasePhotoDatabase;
import team4.cse110.dejaphoto.database.PhotoDBHelper;
import team4.cse110.dejaphoto.login.LoginDialogFragment;
import team4.cse110.dejaphoto.photo.Photo;
import team4.cse110.dejaphoto.photo.PhotoAdapter;
import team4.cse110.dejaphoto.settings.PrefUtils;

/**
 * This class sets up the app's homepage, where photos from the phone's camera
 * album will be displayed in a scrollable grid.
 */
public class GalleryActivity extends BaseActivity {

    private static final String TAG = "GalleryActivity";

    private static final int REQUEST_PHOTO = 0;
    private static final int GRID_SPAN = 3; // number of columns for ImageViews

    private List<Photo> photos;
    private ArrayList<String> paths;
    private PrefUtils prefUtils;
    private MaterialSheetFab materialSheetFab;
    private FirebasePhotoDatabase firebasePhotoDatabase;


    /**
     * This method sets up the app's home page with thumbnails of the photos
     * from the camera album.
     *
     * @param savedInstanceState - the previously saved state of the app.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Get a reference to the Firebase database */
        firebasePhotoDatabase = new FirebasePhotoDatabase(FirebaseDatabase.getInstance());

        /* Get a preference prefUtils reference */
        prefUtils = new PrefUtils();

        /* Get a reference to the RecyclerView */
        RecyclerView rvPhotos = (RecyclerView) findViewById(R.id.rv_gallery);
        photos = new ArrayList<>();

        /* Clear database */
        deleteDatabase(PhotoDBHelper.DATABASE_NAME);


        /* Hook up the adapter to the RecyclerView */
        PhotoAdapter adapter = new PhotoAdapter(this, photos);
        rvPhotos.setAdapter(adapter);
        rvPhotos.setLayoutManager(new GridLayoutManager(this, GRID_SPAN));

        /* Get reference to FAB, sheet, and overlay */
        Fab fab = (Fab) findViewById(R.id.fab_gallery);
        View sheet = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.fab_overlay);
        int sheetColor = getResources().getColor(R.color.fab_sheet_color);
        int fabColor = getResources().getColor(R.color.colorAccent);


        /* FAB button launches camera app */
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePhoto, REQUEST_PHOTO);
            }
        });

        /* Hook up the custom animated FAB */
        materialSheetFab = new MaterialSheetFab<>(fab, sheet, overlay, sheetColor, fabColor);
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
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

            case R.id.action_settings:
                break;


/*                // Instantiate an interactive image picker
                FilePickerBuilder.getInstance().setMaxCount(10)
                        .setSelectedFiles(paths)
                        .setActivityTheme(R.style.AppTheme)
                        .pickPhoto(this);

                break;*/

            case R.id.action_google_login:

                // Display Google login DialogFragment
                DialogFragment loginFragment = new LoginDialogFragment();
                loginFragment.show(getSupportFragmentManager(), TAG);


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
                    firebasePhotoDatabase.addPhoto(new Photo(this, data.toString()));
                    paths = new ArrayList<>();
                    paths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                }
                break;
        }

        /* Populate Photo array */
        for (String path : paths) {
            Photo photo = new Photo(this, path);
            photos.add(photo);
        }
    }

    /**
     * This method retrieves the external DejaPhoto album, or creates one if it
     * doesn't exist.
     * @param name - the name of the album.
     * @return the directory.
     */
    private File getDejaAlbumDir(String name) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), name);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }
}