package uk.co.cherrypick.android.Helper;

import android.content.Context;
import uk.co.cherrypick.android.Model.CardModel;
import uk.co.cherrypick.android.Model.UserData;

import static uk.co.cherrypick.android.Utiles.ApplicationConstants._currentUserData;
import static uk.co.cherrypick.android.Utiles.Tools.matchCardFilter;

/**
 * Created by Simon on 10/12/2015.
 */
public class UserCardHelpers {

    public static void updateSourceIds(Integer cardId, UserData userData) {
        Integer index = userData.getSourceCards().indexOf(cardId);
        userData.getSourceCards().remove((Object) cardId);
    }

    //Rebuild CardsToDecideList based on new data filter
    public static boolean rebuildCardToDecide(Context context){
        _currentUserData.setSourceCards(CardsHelper.getAllCardIds(context));
        for (int i = 0; i < _currentUserData.getSourceCards().size(); i++) {
            CardModel cardModel = CardsHelper.getCardById(context, _currentUserData.getSourceCards().get(i));
            if (!matchCardFilter(context, cardModel)){
                //Remove the card from source card list
                _currentUserData.getSourceCards().remove(i);
                i--;
            }
        }
        return true;
    }

    public static boolean resetLikedCardList(Context context){
        if (_currentUserData.getLikedCards().size() > 0){
            for (int i = 0; i < _currentUserData.getLikedCards().size(); i++) {
                CardModel cardModel = CardsHelper.getCardById(context, _currentUserData.getLikedCards().get(i));
                if (!matchCardFilter(context, cardModel)){
                    _currentUserData.getLikedCards().remove(i);
                    i--;
                }
            }
        }
        return true;
    }

    public static boolean resetUnlikedCardList(Context context){
        if (_currentUserData.getDislikedCards().size() > 0){
            for (int i = 0; i < _currentUserData.getDislikedCards().size(); i++) {
                CardModel cardModel = CardsHelper.getCardById(context, _currentUserData.getDislikedCards().get(i));
                if (!matchCardFilter(context, cardModel)){
                    _currentUserData.getDislikedCards().remove(i);
                    i--;
                }
            }
        }
        return true;
    }

    public static boolean resetNotSureCardList(Context context){
        if (_currentUserData.getNotSureCards().size() > 0){
            for (int i = 0; i < _currentUserData.getNotSureCards().size(); i++) {
                CardModel cardModel = CardsHelper.getCardById(context, _currentUserData.getNotSureCards().get(i));
                if (!matchCardFilter(context, cardModel)){
                    _currentUserData.getNotSureCards().remove(i);
                    i--;
                }
            }
        }
        return true;
    }
}
