package team4.cse110.dejaphoto;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * This is the adapter for the GridView thumbnails in GalleryActivity.
 */

class ImageAdapter extends BaseAdapter {

    private Context context;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return thumbsID.length;
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
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }

        Glide
                .with(context)
                .load(thumbsID[position])
                .into(imageView);

        return imageView;
    }

    private Integer[] thumbsID = {
            R.drawable.sample_0, R.drawable.sample_2,
            R.drawable.sample_3, R.drawable.sample_4,
            R.drawable.sample_1, R.drawable.sample_4,
            R.drawable.sample_5, R.drawable.sample_0,
            R.drawable.sample_1, R.drawable.sample_3,
            R.drawable.sample_3, R.drawable.sample_4,
            R.drawable.sample_1, R.drawable.sample_4,
            R.drawable.sample_5, R.drawable.sample_0,
            R.drawable.sample_1, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_2,
            R.drawable.sample_6, R.drawable.sample_2,
            R.drawable.sample_0, R.drawable.sample_2,
            R.drawable.sample_3, R.drawable.sample_4,
            R.drawable.sample_1, R.drawable.sample_4,
            R.drawable.sample_5, R.drawable.sample_0,
            R.drawable.sample_1, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_2,
            R.drawable.sample_1, R.drawable.sample_4,
            R.drawable.sample_1, R.drawable.sample_4,
            R.drawable.sample_3, R.drawable.sample_4,
            R.drawable.sample_1, R.drawable.sample_4,
            R.drawable.sample_5, R.drawable.sample_0,
            R.drawable.sample_1, R.drawable.sample_3,
            R.drawable.sample_6, R.drawable.sample_2,
            R.drawable.sample_1, R.drawable.sample_4,
            R.drawable.sample_1, R.drawable.sample_4,
            R.drawable.sample_1, R.drawable.sample_4,
    };
}
