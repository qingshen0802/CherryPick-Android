package uk.co.cherrypick.android.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;

import uk.co.cherrypick.android.Utiles.DataTemp;
import uk.co.cherrypick.android.Helper.CardsHelper;
import uk.co.cherrypick.android.Model.CardModel;

import static uk.co.cherrypick.android.Utiles.ApplicationConstants._currentUserData;

/**
 * Created by Simon on 10/12/2015.
 */
public class ReadCardsTask extends AsyncTask <Void, Void, HashMap<Integer, CardModel>>{

    private Context context;
    private AsyncTaskCallBack callBack;

    public ReadCardsTask(Context context, AsyncTaskCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    protected HashMap<Integer, CardModel> doInBackground(Void... params) {
        CardsHelper cardsHelper = new CardsHelper();
        HashMap<Integer, CardModel> result = cardsHelper.getCards(context, _currentUserData.getSourceCards());
        if (result.size() >= 0){
            return result;
        } else {
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(HashMap<Integer, CardModel> cards) {
        super.onPostExecute(cards);
        if(callBack != null){
            if (cards != null) {
                DataTemp<HashMap<Integer, CardModel>> cardResult = new DataTemp<>();
                cardResult.set(cards);
                callBack.onSuccess(AsyncTaskCallBack.READ_CARDS, cardResult);
            } else {
                callBack.onFailed(AsyncTaskCallBack.READ_CARDS);
            }
        }
    }
}
