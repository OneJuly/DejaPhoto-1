package team4.cse110.dejaphoto.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import team4.cse110.dejaphoto.R;

/**
 * Created by Sean on 5/23/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
