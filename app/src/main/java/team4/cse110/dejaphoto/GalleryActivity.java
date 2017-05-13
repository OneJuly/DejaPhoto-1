package team4.cse110.dejaphoto;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";

    private static final int GRID_SPAN = 3; // number of columns for ImageViews

    private List<Photo> photos;

    /** Get the DejaPhoto directory; create if non-existent */
    String dirName = "DejaPhoto";
    File dejaAlbum = getDejaAlbumDir(dirName);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvPhotos = (RecyclerView) findViewById(R.id.rv_gallery);

        /* Initialize album if necessary */
        if (!PrefUtils.isInit(this)) {
            PhotoUtilities.getInstance(this, dejaAlbum).initFromCameraRoll();
            PrefUtils.setInit(this, true);
        }


        /* Get photos from the dejaAlbum */
        photos = PhotoUtilities.getInstance(this, dejaAlbum).getPhotos();

        PhotoAdapter adapter = new PhotoAdapter(this, photos);
        rvPhotos.setAdapter(adapter);
        rvPhotos.setLayoutManager(new GridLayoutManager(this, GRID_SPAN));

        /** Create onClickListener()'s for each photo in the GridView */
        OnItemClickListener pictureSelect
                = new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String prompt = (String)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), prompt, Toast.LENGTH_LONG).show();
            }
        };
    }

    /* Get the external DejaPhoto album and create it if it doesn't exist */
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