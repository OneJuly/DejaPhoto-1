package team4.cse110.dejaphoto;

import android.app.ProgressDialog;
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
    public ProgressDialog progressDialog;

    /**
     * Get the layout resource id of the child activity
     * @return
     */
    protected abstract int getLayoutResource();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            if (getLayoutResource() != R.layout.activity_gallery) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    /**
     *
     */
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.progress_loading));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.show();
    }

    /**
     *
     */
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
