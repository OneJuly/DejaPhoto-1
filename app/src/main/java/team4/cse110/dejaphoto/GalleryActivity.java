package team4.cse110.dejaphoto;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.File;
import java.util.List;

import team4.cse110.dejaphoto.database.PhotoDBHelper;

/**
 * This class sets up the app's homepage, where photos from the phone's camera
 * album will be displayed in a scrollable grid.
 */
public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";
    private static final int GRID_SPAN = 3; // number of columns for ImageViews

    private List<Photo> photos;

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

        /* Initialize database if necessary */
        if (!getDatabasePath(PhotoDBHelper.DATABASE_NAME).exists()) {
            Log.v(TAG, "Initializing DB!\n");
            PhotoUtils.getInstance(this).initFromCameraRoll();
        }

        /* Get active photos */
        photos = PhotoUtils.getInstance(this).getPhotos();

        /* Hook up the adapter to the RecyclerView */
        PhotoAdapter adapter = new PhotoAdapter(this, photos);
        rvPhotos.setAdapter(adapter);
        rvPhotos.setLayoutManager(new GridLayoutManager(this, GRID_SPAN));
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