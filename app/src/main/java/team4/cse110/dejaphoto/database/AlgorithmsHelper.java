package team4.cse110.dejaphoto.database;

import android.content.Context;
import android.graphics.Bitmap;

import team4.cse110.dejaphoto.Algorithm;

/**
 * Created by Sean on 5/14/2017.
 */

public class AlgorithmsHelper implements Algorithm {

    public AlgorithmsHelper(Context context) {

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
}
