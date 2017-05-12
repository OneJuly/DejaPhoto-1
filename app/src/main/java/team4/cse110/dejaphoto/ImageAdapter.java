package team4.cse110.dejaphoto;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


/**
 * This is the adapter for the GridView thumbnails in GalleryActivity.
 */

class ImageAdapter extends BaseAdapter {

    private Context context;
    private PhotoUtils photoUtils;
    private ArrayList<String> cameraPhotos;


    public ImageAdapter(Context context) {
        this.context = context;
        photoUtils = new PhotoUtils(context);
        cameraPhotos = photoUtils.getCameraPhotos();


    }

    @Override
    public int getCount() {
        return cameraPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(12, 12, 12, 12);
        } else {
            imageView = (ImageView) convertView;
        }

        Glide
                .with(context)
                .load(cameraPhotos.get(position))
                .into(imageView);

        return imageView;
    }

}
