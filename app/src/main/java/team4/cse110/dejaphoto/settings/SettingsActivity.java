package team4.cse110.dejaphoto.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;

import team4.cse110.dejaphoto.BaseActivity;
import team4.cse110.dejaphoto.R;

/**
 * Created by Sean on 5/23/2017.
 */

public class SettingsActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show the preferences fragment
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();

        // Update the activity title
        setTitle(R.string.title_activity_settings);
    }


}
