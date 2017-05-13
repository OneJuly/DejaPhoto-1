package team4.cse110.dejaphoto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * This is the adapter for the RecyclerView thumbnails in GalleryActivity.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private static final String TAG = "PhotoAdapter";

    private Context context;
    private List<Photo> photos;

    public PhotoAdapter() {
    }

    /* Internal clickable photo viewholder */
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView photo;
        private Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.gallery_photo);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Photo photo = photos.get(pos);
                Toast.makeText(context, photo.getPath(), Toast.LENGTH_LONG).show();
            }

        }
    }


    public PhotoAdapter(Context context, List<Photo> photos) {
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
        ViewHolder holder = new ViewHolder(context, photoView);
        return holder;
    }

    // Attach photo data to the viewholder
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

