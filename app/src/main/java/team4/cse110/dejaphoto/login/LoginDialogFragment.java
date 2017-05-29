package team4.cse110.dejaphoto.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import team4.cse110.dejaphoto.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginDialogFragment extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get the layout inflator
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Build a custom Google sign in AlertDialog with inflated View
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setView(inflater.inflate(R.layout.fragment_login_dialog, null));

        return alertBuilder.create();

    }
}

