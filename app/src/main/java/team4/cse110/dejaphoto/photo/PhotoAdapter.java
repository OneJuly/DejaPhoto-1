package team4.cse110.dejaphoto.photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import team4.cse110.dejaphoto.R;

/**
 * RecyclerView the adapter for the thumbnails in GalleryActivity.
 */


public class PhotoAdapter extends FirebaseRecyclerAdapter<Photo, PhotoAdapter.ViewHolder> {

    private static final String TAG = "PhotoAdapter";
    private Context context;

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public PhotoAdapter(Context context, Class<Photo> modelClass, int modelLayout,
                        Class<ViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Photo model, int position) {
        // Load images
        Glide
                .with(context)
                .load(model.getPath())
                .into(viewHolder.photo);

    }


    /**
     * Custome Photo PhotoHolder
     */
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
                toggleCheckbox(checkBox);
            }
        }

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

