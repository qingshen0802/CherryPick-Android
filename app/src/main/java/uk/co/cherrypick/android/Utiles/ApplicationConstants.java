package uk.co.cherrypick.android.Utiles;

import android.content.Context;
import uk.co.cherrypick.android.Model.UserData;

/**
 * Created by Simon on 10/9/2015.
 */
public class ApplicationConstants {

    private Context _context = null;
    private static ApplicationConstants _sharedInstance = null;
    public static UserData _currentUserData = null;

    public enum Network{
        All, O2, VODAFONE, EE, THREE
    }

    public enum OperatingSystemType{
        All, ANDROID, IOS, WINDOWS
    }

    public static final String SENDGRID_USERNAME = "cherrypickapp";
    public static final String SENDGRID_PASSWORD = "ghabd78678^&*UHkjNJKHG78t786tuybjk:LKJ";

    public static ApplicationConstants get_sharedInstance(Context context){
        if (_sharedInstance == null){
            _sharedInstance = new ApplicationConstants();
            _sharedInstance._context = context;
            _currentUserData = new UserData();
        }
        return _sharedInstance;
    }
}
