package team4.cse110.dejaphoto;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";

    private static final int GRID_SPAN = 3;

    private ArrayList<Photo> photos;

    /** Create a new directory to store selected folders. */
    static final String dirName = "DejaPhoto";
    static final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), dirName);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvPhotos = (RecyclerView) findViewById(R.id.rv_gallery);

        PhotoUtils utils = new PhotoUtils(this);
        photos = utils.getCameraPhotos();

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
}