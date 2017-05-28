package team4.cse110.dejaphoto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * All DejaPhoto Activities will extend BaseActivity
 *
 * This Activity ensures that application dependencies are
 * properly initialized.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
