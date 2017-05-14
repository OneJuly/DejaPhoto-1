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
 * This class is the adapter for the RecyclerView thumbnails in GalleryActivity.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private static final String TAG = "ImageAdapter";

    /**
     * Empty constructor.
     */
    public ImageAdapter() {
    }

    /**
     * This class is an internal photo viewholder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView photo;

        /**
         * This method sets an item's appearance and place within RecyclerView.
         * @param itemView - the item's appearance and place.
         */
        public ViewHolder(View itemView) {
            super(itemView);

            photo = (ImageView) itemView.findViewById(R.id.gallery_photo);
        }
    }

    private Context context;
    // A collection of photos.
    private List<Photo> photos;

    /**
     * This constructor sets the environment data and the collection of photos.
     * @param context - the environment data of an object.
     * @param photos - the collection of photos.
     */
    public ImageAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    private Context getContext() {
        return context;
    }

    /**
     * This method inflates the gallery_photo layout into a new ViewHolder.
     * @param parent -
     * @param viewType -
     * @return the new ViewHolder.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);

        // Inflate the gallery_photo layout.
        View photoView = inflater.inflate(R.layout.gallery_photo, parent, false);

        // Return the new ViewHolder.
        ViewHolder holder = new ViewHolder(photoView);

        return holder;
    }

    /**
     * This method attaches photo data to the ViewHolder.
     * @param holder is the Viewholder to attach photo data to.
     * @param position is the position of the photo in the photo collection.
     */
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

