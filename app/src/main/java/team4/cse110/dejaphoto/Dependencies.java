package team4.cse110.dejaphoto;

import android.content.Context;

/**
 * Using the singleton model from Effective Java Ed. 2
 */

public enum Dependencies {

    INSTANCE;


    private void init(Context context) {
    }




    private boolean isInitialized() {
        return false;
    }


}
