package team4.cse110.dejaphoto;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * All DejaPhoto Activities will extend BaseActivity
 *
 * This Activity ensures that application dependencies are
 * properly initialized.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = BaseActivity.this;
        setContentView(getLayoutResource());
        initToolbar();
    }

    /**
     * Sets the activity toolbar
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Get the layout resource id of the child activity
     * @return
     */
    protected abstract int getLayoutResource();

}
