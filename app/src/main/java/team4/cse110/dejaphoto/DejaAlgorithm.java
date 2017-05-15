package team4.cse110.dejaphoto;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;
import java.util.ListIterator;

public class DejaAlgorithm implements Algorithm {

    private Context context;
    private PhotoDB db;

    // Database dependent variables
    private List<Photo> album;
    private List<Photo> cache;
    private int cachePos;   // last returned by prev() or next() or release(). -1 if cache empty

    public DejaAlgorithm(Context context) {
        this.context = context;
        this.db = PhotoUtils.getInstance(context);
        load();
    }

    @Override
    public Bitmap next() {
        if (album.isEmpty()) return null;

        // Compute weight for all images
        for (Photo photo : album) {
            setWeight(photo);
        }

        // Pick an image randomly while factoring in weights
        double totalWeight = 0.0;
        for (Photo photo : album) {
            totalWeight += photo.getWeight();
        }

        Photo chosenPhoto = null;
        double random = totalWeight * Math.random();
        for (Photo photo : album) {
            random -= photo.getWeight();
            if (random <= 0) {
                chosenPhoto = photo;
                break;
            }
        }
        assert chosenPhoto != null;

        // Update cache
        addToCache(chosenPhoto);
        db.setCache(cache);

        return chosenPhoto.getBitmap();
    }

    @Override
    public Bitmap prev() {
        if (cachePos < 0) return null;

        cachePos--;
        Photo photo = cache.get(cachePos);
        save();
        return photo.getBitmap();
    }

    @Override
    public void incKarma() {
        Photo photo = getCurrentPhoto();
        if (photo != null) {
            photo.setKarma(true);
            db.updatePhoto(photo);
        }
    }

    @Override
    public boolean hasKarma() {
        Photo photo = getCurrentPhoto();
        return photo != null && photo.getKarma();
    }

    @Override
    public Bitmap release() {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public void load() {

    }

    /**
     * @return photo currently set as the wallpaper
     */
    private Photo getCurrentPhoto() {
        if (0 <= cachePos && cachePos < cache.size()) {
            return cache.get(cachePos);
        } else {
            return null;
        }
    }

    /**
     * Computes the weight for every photo in the album
     */
    private void setWeight(Photo photo) {
        // TODO add time into the calculation. Android has System.currentTimeMillis()

    }

    private void addToCache(Photo photo) {
        // Check if we need to remove everything after pointer
        if (cachePos != cache.size() - 1) {
            ListIterator<Photo> itr = cache.listIterator(cachePos);
            itr.remove(); // remove self
            while (itr.hasNext()) {
                itr.next();
                itr.remove();
            }
        }

        // Add photo and reset pointer
        cache.add(photo);
        cachePos = cache.size() - 1;
    }
}
