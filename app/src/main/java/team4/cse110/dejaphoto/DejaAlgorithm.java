package team4.cse110.dejaphoto;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class DejaAlgorithm implements Algorithm {

    private Context context;
    private PhotoDB db;

    // Database dependent variables
    private ArrayList<Photo> album;
    private ArrayList<Photo> cache;
    private int cachePos;


    public DejaAlgorithm(Context context) {
        this.context = context;
        this.db = PhotoUtils.getInstance(context);
        load();
    }

    @Override
    public Bitmap next() {
        return null;
    }

    @Override
    public Bitmap prev() {
        return null;
    }

    @Override
    public void incKarma() {

    }

    @Override
    public boolean hasKarma() {
        return false;
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
}
