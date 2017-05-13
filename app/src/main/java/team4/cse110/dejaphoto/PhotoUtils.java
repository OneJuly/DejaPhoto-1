package team4.cse110.dejaphoto;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by Sean on 5/12/2017.
 */

class PhotoUtils {

    private Context context;

    /**
     *
     * @param context
     */
    PhotoUtils(Context context) {
        this.context = context;
    }

    /* Returns a list of paths to default camera photos */
    ArrayList<Photo> getCameraPhotos() {

        ArrayList<Photo> photoPaths = new ArrayList<>();

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };

        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";

        String[] selectionArgs = new String[] {
                "Camera"
        };

        final String orderDate = MediaStore.Images.Media.DATE_ADDED;

        //Stores all the images from the gallery in Cursor
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, orderDate);

        //Total number of images
        int numPhotos = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[numPhotos];

        for (int i = 0; i < numPhotos; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            //Store the path of the image
            arrPath[i]= cursor.getString(dataColumnIndex);
            photoPaths.add(new Photo(context, arrPath[i]));

        }
        return  photoPaths;

    }


}
