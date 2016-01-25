package uk.co.cherrypick.android.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

import uk.co.cherrypick.android.Helper.FileHelpers;
import uk.co.cherrypick.android.Helper.UserDataHelper;
import uk.co.cherrypick.android.Model.Session;
import uk.co.cherrypick.android.Model.User;

import static uk.co.cherrypick.android.Utiles.ApplicationConstants.SENDGRID_PASSWORD;
import static uk.co.cherrypick.android.Utiles.ApplicationConstants.SENDGRID_USERNAME;

/**
 * Created by Simon on 10/18/2015.
 */
public class SendEmail extends AsyncTask<Void, Void, Void> {

    private Context context;

    public SendEmail(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            String userEmail = User.getSession().getName();
            String fileName = "user-" + userEmail + "-data.json";
            File cherryPickDir = context.getFilesDir();
            File userDataFile = new File(cherryPickDir,fileName);
            if(!userDataFile.exists()){
                userDataFile.createNewFile();
            }
            String logFileName = "users-" + userEmail + ".log";
            File logFile = new File(cherryPickDir, logFileName);

            if(!logFile.exists()){
                logFile.createNewFile();
            }
            SendGrid sendgrid = new SendGrid(SENDGRID_USERNAME, SENDGRID_PASSWORD);
            SendGrid.Email email = new SendGrid.Email();

            // Get values from edit text to compose email
            // TODO: Validate edit texts
            email.addTo("john@eastpoint.co.uk");
            email.addTo("dave.norwell@eastpoint.co.uk");
            email.addTo("lawrencede.ath@ifgconsulting.co.uk");
            email.addTo("Ian.Ginn@ifgconsulting.co.uk");
            email.setFrom("noreply@cherrypick.co.uk");
            email.setSubject("Cherrypick User Data");
            email.setText(String.format("User data for %s", userEmail));
            email.addAttachment("User JSON file.json", userDataFile);
            email.addAttachment("User Log File.log", logFile);

            // Send email, execute http request
            SendGrid.Response response = sendgrid.send(email);
            String msgResponse = response.getMessage();

            Log.d("SendEmail", msgResponse);
            //Toast.makeText(context, "Data exported by email. Thanks!", Toast.LENGTH_SHORT).show();

        } catch (SendGridException e) {
            Log.e("SendEmail", e.toString());
        } catch (JSONException e) {
            Log.e("SendEmail", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
