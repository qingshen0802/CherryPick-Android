package uk.co.cherrypick.android.AsyncTask;

import uk.co.cherrypick.android.Utiles.DataTemp;

/**
 * Created by Simon on 10/12/2015.
 */
public interface AsyncTaskCallBack {

    public static int READ_CARDS = 0;

    public void onSuccess(int taskType, DataTemp dataTemp);
    public void onFailed(int taskType);
}
