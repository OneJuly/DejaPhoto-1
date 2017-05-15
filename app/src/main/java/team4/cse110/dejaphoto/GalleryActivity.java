package team4.cse110.dejaphoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

/**
 * This class sets up the app's homepage, where photos from the phone's camera
 * album will be displayed in a scrollable grid.
 */
public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";
    public static int PHOTO_REQUEST_CODE = 2929;
    private static final int GRID_SPAN = 3; // number of columns for ImageViews

    private List<Photo> photos;
    private ArrayList<String> paths;

    /** Create a new directory to store selected folders. */
    static final String dirName = "DejaPhoto";

    /** Get the DejaPhoto directory; create if non-existent */
    File dejaAlbum = getDejaAlbumDir(dirName);

    /**
     * This method sets up the app's home page with thumbnails of the photos
     * from the camera album.
     * @param savedInstanceState - TODO
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvPhotos = (RecyclerView) findViewById(R.id.rv_gallery);
        PhotoUtils.getInstance(this);
        photos = new ArrayList<>();

        /* Instantiate an interactive image picker on startup */
        /* TODO skip this if db is already initialized */
            FilePickerBuilder.getInstance().setMaxCount(10)
                    .setSelectedFiles(paths)
                    .setActivityTheme(R.style.AppTheme)
                    .pickPhoto(this);

        /* Hook up the adapter to the RecyclerView */
        PhotoAdapter adapter = new PhotoAdapter(this, photos);
        rvPhotos.setAdapter(adapter);
        rvPhotos.setLayoutManager(new GridLayoutManager(this, GRID_SPAN));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null)
                {
                    paths = new ArrayList<>();
                    paths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                }
                break;
        }

        /* Populate Photo array */
        for (String path : paths) {
            photos.add(new Photo(path));
        }
    }

    /* Get the external DejaPhoto album and create it if it doesn't exist */
    /* TODO */
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