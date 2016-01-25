package uk.co.cherrypick.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import uk.co.cherrypick.android.Helper.UserAccountUtils;
import uk.co.cherrypick.android.Model.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static uk.co.cherrypick.android.Model.User.*;

public class LoginActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.example.eastpoint.cherrypick.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Called when the user clicks the Send button
     */
    public void sendLogin(View view) {

        boolean validationFailed = false;
        EditText firstName = (EditText) findViewById(R.id.email);
        if (firstName.getText().toString().length() == 0) {
            firstName.setError("Email is required!");
            validationFailed = true;
        }

        if (validationFailed) {
            return;
        }

        //Log user details
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        Session session = new Session(firstName.getText().toString().toLowerCase().trim(), currentDateandTime);

        //Set User session so that can avialable in next activity
        setSession(session);

        // write user actions to file(Login success)
        Log.d("UserActionLog", "Started logging user action....");
        LogActions userAction = new LogActions();
        userAction.logAction(this,"LoginView","Login Success", null);

        if (UserAccountUtils.isReturningUser(this, session.getName())) {
            navigateToMain();
        } else {
            UserAccountUtils.writeToUserJson(this, session.getName());
            navigateToIntro();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateToIntro() {
        Intent intent = new Intent(this, IntroScreenActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogActions.logAction(this, "Application", "App Paused", null);
    }

    @Override
    public void finish() {
        super.finish();
        LogActions.logAction(this, "Application", "App closed", null);
    }
}
