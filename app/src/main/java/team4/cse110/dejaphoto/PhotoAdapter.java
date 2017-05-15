package team4.cse110.dejaphoto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * This is the adapter for the RecyclerView thumbnails in GalleryActivity.
 */


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private static final String TAG = "PhotoAdapter";

    private List<Photo> photos;
    private PhotoUtils utils;
    private Context context;

    /* Internal clickable photo viewholder */
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView photo;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.gallery_photo);
            checkBox = (CheckBox) itemView.findViewById(R.id.gallery_checkbox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {
                Photo photo = photos.get(pos);
                toggleCheckbox(checkBox);
                if (photo.isActive()) {
                    Log.v(TAG, "Photo is active " + pos);
                    photo.setPrev(0);
                } else {
                    Log.v(TAG, "Photo is inactive " + pos);
                    photo.setPrev(1);
                }
            }
        }

    }

    /* PhotoAdapter constructor */
    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
        utils = PhotoUtils.getInstance(context);
    }

    private Context getContext() {
        return context;
    }

    // Inflate the gallery_photo layout into a new viewholder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);

        // Inflate the gallery_photo layout
        View photoView = inflater.inflate(R.layout.gallery_photo, parent, false);

        // Return the new viewholder
        ViewHolder holder = new ViewHolder(photoView);
        return holder;
    }

    // Attach photo data to the viewholder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = photos.get(position);
        ImageView imageView = holder.photo;

        /* Get checkbox info */
        if (photo.isActive()) {
            holder.checkBox.setChecked(true);
        }

        /* Load the image */
        Glide
                .with(context)
                .load(photo.getPath())
                .into(imageView);
    }

    // Get the number of photos in the photo list
    @Override
    public int getItemCount() {
        return photos.size();
    }

    /* Toggle imageview checkbox */
    private void toggleCheckbox(CheckBox cb) {
        if (cb.isChecked()) {
            cb.setChecked(false);
        } else {
            cb.setChecked(true);
        }
    }

}

