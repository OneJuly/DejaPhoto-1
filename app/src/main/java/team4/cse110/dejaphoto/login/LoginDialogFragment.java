package team4.cse110.dejaphoto.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

import team4.cse110.dejaphoto.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginDialogFragment extends DialogFragment{

    private static final String TAG = "LoginDialogFragment";

    private static final int REQUEST_SIGN_IN = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get the layout inflator
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Build a custom Google sign in AlertDialog with inflated View
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        View fragmentView = inflater.inflate(R.layout.fragment_login_dialog, null);
        alertBuilder.setView(fragmentView);

        // Set the Google sign in button listener

        SignInButton signInButton =
                (SignInButton) fragmentView.findViewById(R.id.button_google_login);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        return alertBuilder.create();

    }


    /**
     *
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi
                .getSignInIntent(LoginActivity.getGoogleApiClient());

        startActivityForResult(signInIntent, REQUEST_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     *
     * @param result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            //TODO
            Toast.makeText(getActivity(), "Login success", Toast.LENGTH_SHORT).show();

        } else {
            //TODO
            Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
        }

    }
}

