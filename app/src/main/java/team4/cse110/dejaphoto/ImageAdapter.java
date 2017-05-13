package team4.cse110.dejaphoto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * This is the adapter for the RecyclerView thumbnails in GalleryActivity.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    public ImageAdapter() {
    }

    /* Internal photo viewholder */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);

            photo = (ImageView) itemView.findViewById(R.id.gallery_photo);
        }
    }

    private Context context;
    private List<Photo> photos;

    public ImageAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
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

    // Attach photo data to the vewholder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = photos.get(position);
        ImageView imageView = holder.photo;
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


}
