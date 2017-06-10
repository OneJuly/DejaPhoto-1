package team4.cse110.dejaphoto.utilities;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import team4.cse110.dejaphoto.database.DatabaseInterface;
import team4.cse110.dejaphoto.photo.Photo;
import team4.cse110.dejaphoto.settings.PrefUtils;

/**
 * This class calculates the weight of a photo, which the app uses to determine
 * the probability of a photo becoming the phone's wallpaper. It also handles
 * releasing photos from the display cycle.
 */
public class DejaAlgorithm implements Algorithm {
    // The max photos in the display history.
    private static final int CACHE_SIZE = 10;

    private Context context;
    private DatabaseInterface db;

    // Database dependent variables.
    private List<Photo> album;
    private ArrayList<Photo> cache;
    private int cachePos; // last returned by prev(), next() or release(). -1 if cache is empty.

    public DejaAlgorithm(Context context, DatabaseInterface db) {
        this.context = context;
        this.db = db;
        load();
    }

    @Override
    public Photo next() {
        if (album.isEmpty()) return null;

        // See if we can just move forward in the cache
        if (cachePos < cache.size() - 1 && cachePos != -1) {
            cachePos++;
            db.storePreviousIndex(cachePos);
            return cache.get(cachePos);
        }

        // Handle non DejaVu next
        if (!PrefUtils.dejaVuEnabled(context)) {
            Random random = new Random();
            Photo photo = album.get(random.nextInt(album.size()));
            addToCache(photo);
            return photo;
        }

        // Compute weight for all images
        for (Photo photo : album) {
            photo.weight = photo.calcWeight();
        }

        // Pick an image randomly while factoring in weights
        double totalWeight = 0.0;
        for (Photo photo : album) {
            totalWeight += photo.weight;
        }

        Photo chosenPhoto = null;
        double random = totalWeight * Math.random();
        for (Photo photo : album) {
            random -= photo.weight;
            if (random <= 0) {
                chosenPhoto = photo;
                break;
            }
        }
        assert chosenPhoto != null;

        // Update cache
        addToCache(chosenPhoto);

        return chosenPhoto;
    }

    @Override
    public Photo prev() {
        if (cache.size() <= 1) return null;

        cachePos--;
        Photo photo = cache.get(cachePos);
        db.storePreviousIndex(cachePos);
        return photo;
    }

    @Override
    public void incKarma() {
//        Photo photo = getCurrentPhoto();
//        if (photo != null) {
//            photo.setKarma(1);
//            db.updatePhoto(photo);
//        }
        // TODO implement
    }

    @Override
    public boolean hasKarma() {
//        Photo photo = getCurrentPhoto();
//        return photo != null && photo.getKarma() == 1;
        return false; // TODO implement
    }

    @Override
    public Photo release() {
        Photo photo = getCurrentPhoto();
        if (photo == null) {
            return null;
        }

        // Update cache. db update done in next()
        cache.remove(photo);
        if (cache.isEmpty()) cachePos = -1;

        // Update album
        album.remove(photo);
        db.deletePhoto(photo);
        save();

        // Get the next photo to display
        return next();
    }

    @Override
    public void save() {
        db.storePreviousList(cache);
        db.storePreviousIndex(cachePos);
    }

    @Override
    public void load() {
        album = db.getPhotoList();
        cache = db.getPreviousList();
        cachePos = db.getPreviousIndex();
    }

    @Override
    public DatabaseInterface getDatabase() {
        return db;
    }

    private Photo getCurrentPhoto() {
        if (!cache.isEmpty() && cachePos != -1) {
            return cache.get(cachePos);
        } else {
            return null;
        }
    }

    private void addToCache(Photo photo) {
        cache.add(photo);

        // Fix if we are over max size
        while (cache.size() > CACHE_SIZE) {
            cache.remove(0);
        }
        cachePos = cache.size() - 1;

        save();
    }
}
