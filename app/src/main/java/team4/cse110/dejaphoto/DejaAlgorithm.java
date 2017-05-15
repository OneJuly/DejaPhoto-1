package team4.cse110.dejaphoto;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * This class calculates the weight of a photo, which the app uses to determine
 * the probability of a photo becoming the phone's wallpaper. It also handles
 * releasing photos from the display cycle.
 */
public class DejaAlgorithm implements Algorithm {
    // The max photos in the display history.
    private static final int CACHE_SIZE = 10;

    private Context context;
    private PhotoDB db;

    // Database dependent variables
    private List<Photo> album;
    private List<Photo> cache;

    // Last returned by prev(), next() or release(). -1 if cache is empty.
    private int cachePos;

    public DejaAlgorithm(Context context) {
        this.context = context;
        this.db = PhotoUtils.getInstance(context);
        load();
    }

    @Override
    public Bitmap next() {
        if (album.isEmpty()) return null;

        // Handle non DejaVu next
        if (!PrefUtils.dejaVuEnabled(context)) {
            Random random = new Random();
            Photo photo = album.get(random.nextInt(album.size()));
            addToCache(photo);
            return photo.getBitmap();
        }

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

        return chosenPhoto.getBitmap();
    }

    @Override
    public Bitmap prev() {
        if (cache.size() <= 1) return null;

        cachePos--;
        Photo photo = cache.get(cachePos);
        db.setPosition(cachePos);
        return photo.getBitmap();
    }

    @Override
    public void incKarma() {
        Photo photo = getCurrentPhoto();
        if (photo != null) {
            photo.setKarma(1);
            db.updatePhoto(photo);
        }
    }

    @Override
    public boolean hasKarma() {
        Photo photo = getCurrentPhoto();
        return photo != null && photo.getKarma() == 1;
    }

    @Override
    public Bitmap release() {
        Photo photo = getCurrentPhoto();
        if (photo == null) {
            return null;
        }

        // Update cache. db update done in next()
        cache.remove(photo);
        cachePos = cache.size() - 1;

        // Update album
        album.remove(photo);
        db.removePhoto(photo);

        // Get the next photo to display
        return next();
    }

    @Override
    public void save() {
        db.setCache(cache);
        db.setPosition(cachePos);
    }

    @Override
    public void load() {
        album = db.getPhotos();
        cache = db.getCache();
        cachePos = db.getPosition();
    }

    private Photo getCurrentPhoto() {
        if (0 <= cachePos && cachePos < cache.size()) {
            return cache.get(cachePos);
        } else {
            return null;
        }
    }

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

        cache.add(photo);

        // Fix if we are over max size
        while (cache.size() > CACHE_SIZE) {
            cache.remove(0);
        }
        cachePos = cache.size() - 1;

        db.setCache(cache);
        db.setPosition(cachePos);
    }
}
