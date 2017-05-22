package team4.cse110.dejaphoto;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by Sean on 5/15/2017.
 */

public class SetWallpaper extends AsyncTask<Photo, Void, Bitmap> {

    private static final String TAG = "SetWallpaper";

    private Context mContext;

    public SetWallpaper(Context context) {
        mContext = context;
    }

    @Override
    protected Bitmap doInBackground(Photo... params) {
        return params[0].getBitmap();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        try {
            WallpaperManager.getInstance(mContext).setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
