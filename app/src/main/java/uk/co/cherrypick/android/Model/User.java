package uk.co.cherrypick.android.Model;

/**
 * Created by mohanp on 18/9/15.
 */
public class User {
    private static Session   mSession;

    public static void setSession(Session session){
        mSession = session;
    }

    public static Session getSession(){
        return mSession; 
    }
}