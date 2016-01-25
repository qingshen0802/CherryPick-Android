/**
 * Cherrypick for Android
 *
 * @Author: Eastpoint <info@eastpoint.co.uk>
 * http://www.eastpoint.co.uk
 */

package uk.co.cherrypick.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import uk.co.cherrypick.android.AsyncTask.AsyncTaskCallBack;
import uk.co.cherrypick.android.AsyncTask.ReadCardsTask;
import uk.co.cherrypick.android.AsyncTask.SendEmail;
import uk.co.cherrypick.android.Custom.DataFilterDialog;
import uk.co.cherrypick.android.Utiles.ApplicationConstants;
import uk.co.cherrypick.android.Utiles.DataTemp;
import uk.co.cherrypick.android.Custom.DialogButtonListener;
import uk.co.cherrypick.android.Helper.UserDataHelper;
import uk.co.cherrypick.android.Model.CardModel;
import uk.co.cherrypick.android.Model.UserData;
import uk.co.cherrypick.android.Utiles.Tools.MenuButtonClickListener;
import uk.co.cherrypick.android.View.CardContainer;
import uk.co.cherrypick.android.View.OfferCardStackAdapter;
import uk.co.cherrypick.android.View.SwipeChangeListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static uk.co.cherrypick.android.Helper.UserCardHelpers.rebuildCardToDecide;
import static uk.co.cherrypick.android.Helper.UserCardHelpers.resetLikedCardList;
import static uk.co.cherrypick.android.Helper.UserCardHelpers.resetNotSureCardList;
import static uk.co.cherrypick.android.Helper.UserCardHelpers.resetUnlikedCardList;
import static uk.co.cherrypick.android.Helper.UserCardHelpers.updateSourceIds;
import static uk.co.cherrypick.android.Utiles.ApplicationConstants._currentUserData;
import static uk.co.cherrypick.android.Utiles.Tools.displayMenu;

public class MainActivity extends Activity implements DialogButtonListener, AsyncTaskCallBack, MenuButtonClickListener, SwipeChangeListener {

    /**
     * This variable is the container that will host our cards
     */
    // private CardContainer mCardContainer;
    CardContainer mCardContainer;

    private ProgressBar spinner;
    private PopupWindow menu;
    final LogActions userAction = new LogActions();

    private Context _context = this;
    private DialogButtonListener dialogButtonListenerCallBack = this;
    private AsyncTaskCallBack _asyncTaskCallBack = this;
    private File _userPreferences;
    private OfferCardStackAdapter _offerCardStackAdapter;
    private ReadCardsTask readCardsTask = null;
    private MenuButtonClickListener _menuButtonClickListener = this;
    private ImageView likeIndicator, dislikeIndicator, notsureIndicator;

    ApplicationConstants applicationConstants = null;

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
        checkForUpdates();
    }

    private void checkForCrashes() {
//        CrashManager.register(this, "bb3a43cbc9d4b95980a673d631b0492f");
    }

    private void checkForUpdates() {
        // Remove this for store builds!
//        UpdateManager.register(this, "bb3a43cbc9d4b95980a673d631b0492f");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.mainlayout);

        applicationConstants = ApplicationConstants.get_sharedInstance(_context);

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        mCardContainer = (CardContainer) findViewById(R.id.layoutview);
        likeIndicator = (ImageView)findViewById(R.id.like_indicator);
        dislikeIndicator = (ImageView)findViewById(R.id.dislike_indicator);
        notsureIndicator = (ImageView)findViewById(R.id.notsure_indicator);
        _offerCardStackAdapter = new OfferCardStackAdapter(_context);

        _userPreferences = UserDataHelper.getUserPreferences(_context);
        _currentUserData = UserDataHelper.getUserData(_context);
        // Handle earlier selections.

        if (_currentUserData.getSourceCards().size() == 0 && (_currentUserData.getLikedCards().size() + _currentUserData.getNotSureCards().size() >= 0)) {
            _currentUserData.getSourceCards().addAll(_currentUserData.getLikedCards());
            _currentUserData.getSourceCards().addAll(_currentUserData.getNotSureCards());
            _currentUserData.setDislikedCards(new ArrayList<Integer>());
            _currentUserData.setLikedCards(new ArrayList<Integer>());
            _currentUserData.setNotSureCards(new ArrayList<Integer>());

            //Update User Cards info : updateCards() for previous version
            UserDataHelper.updateData(this, _currentUserData);
        }

        if (readCardsTask == null){
            readCardsTask = new ReadCardsTask(_context, _asyncTaskCallBack);
            readCardsTask.execute();
        }
    }

    @NonNull
    private void attachCardListeners(final File userPreferences, final UserData userData,
                                     final CardModel cardModel) {
        // setup the listener that responds to a user pressing the card (it should flip over to view the reverse)
        cardModel.setOnClickListener(new CardModel.OnClickListener() {
            @Override
            public void OnClickListener() {
                Log.d("ClickListener", "Card clicked");
            }
        });

        // setup the listener that responds when a card is 'dropped' i.e. the user has said Yes, No or Maybe.
        cardModel.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
            @Override
            public void onLike() {
                Integer cardId = cardModel.getId();
                userData.getLikedCards().add(cardId);
                updateSourceIds(cardId, userData);
                Log.d("Card Swipe", "User liked the card " + cardId);
                userAction.logAction(_context, "CardStack", "LikedOffer", "" + cardModel.getId());
                UserDataHelper.updateLike(userPreferences, cardId);
                handleDismiss(userData);
            }

            @Override
            public void onDislike() {
                Integer cardId = cardModel.getId();
                userData.getDislikedCards().add(cardId);
                updateSourceIds(cardId, userData);
                userAction.logAction(_context, "CardStack", "DislikedOffer", "" + cardModel.getId());
                UserDataHelper.updateDislike(userPreferences, cardId);
                handleDismiss(userData);
            }

            @Override
            public void onMaybe() {
                Integer cardId = cardModel.getId();
                userData.getNotSureCards().add(cardId);
                updateSourceIds(cardId, userData);
                userAction.logAction(_context, "CardStack", "NotSureOffer", "" + cardModel.getId());
                UserDataHelper.updateNotSure(userPreferences, cardId);
                handleDismiss(userData);
            }
        });
    }

    private void handleDismiss(UserData data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        // if we've got to the bottom of the pile. check the status.
        if (0 == data.getSourceCards().size()) {
            Integer likes = (data.getLikedCards().size() + data.getNotSureCards().size());
            //Integer cardsLength = data.getLikedCards().size()+data.getNotSureCards().size();
            if (likes <= 10) {
                handleUserSelection(builder);
            } else {

                data.getSourceCards().addAll(data.getLikedCards());
                data.getSourceCards().addAll(data.getNotSureCards());

                data.setDislikedCards(new ArrayList<Integer>());
                data.setLikedCards(new ArrayList<Integer>());
                data.setNotSureCards(new ArrayList<Integer>());

                //Update User card : updateCards() for previous version
                UserDataHelper.updateData(this, data);
                builder.setMessage("You have choosen " + likes + ". Please narrow down your selection.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                refresh();
                            }
                        });
            }
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void handleUserSelection(AlertDialog.Builder builder) {
        builder.setMessage("Great... you have got it down to just a few cards! Please sort these three next...")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Intent intent = new Intent(_context, CardListActivity.class);
                        startActivity(intent);
                    }
                });
        LogActions.logAction(_context, "MainActivity", "CardStack completed", null);
    }

    private void refresh() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    private void showMenu(){
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
        menu = displayMenu(_context, _menuButtonClickListener);
        menu.showAtLocation(MainActivity.this.getWindow().getDecorView().findViewById(android.R.id.content), Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){
            showMenu();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        showMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogActions.logAction(_context, "Application", "App Paused", null);
    }

    @Override
    public void onPositiveButtonClicked(int dialogType) {
        switch (dialogType){
            case DATA_FILTER:
                //Update User data filter
                UserDataHelper.updateData(_context, _currentUserData);
                if (rebuildCardToDecide(_context)) {
                    spinner.setVisibility(View.VISIBLE);
                    readCardsTask = new ReadCardsTask(_context, _asyncTaskCallBack);
                    readCardsTask.execute();
                }
                break;
            default:
                return;
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogType) {
        switch (dialogType){
            case DATA_FILTER:
                break;
            default:
                return;
        }
    }

    @Override
    public void onSuccess(int taskType, DataTemp dataTemp) {
        switch (taskType) {
            case READ_CARDS:
                HashMap<Integer, CardModel> offerCards = (HashMap<Integer, CardModel>) dataTemp.get();
                _offerCardStackAdapter = new OfferCardStackAdapter(_context);
                for (Integer key : offerCards.keySet()) {
                    CardModel card = offerCards.get(key);
                    attachCardListeners(_userPreferences, _currentUserData, card);
                    _offerCardStackAdapter.add(card);
                }
                if (mCardContainer.getChildCount() > 0) {
                    mCardContainer.removeViews(0, mCardContainer.getChildCount() - 1);
                }
                mCardContainer.setAdapter(_offerCardStackAdapter);
                spinner.setVisibility(View.GONE);
                readCardsTask = null;
                LogActions.logAction(_context, "MainActivity", "CardStack Opened", null);
                break;
        }
    }

    @Override
    public void onFailed(int taskType) {
        switch (taskType){
            case READ_CARDS:
                spinner.setVisibility(View.GONE);
                break;
        }
    }
    // Menu Button Click Listeners
    @Override
    public void onReset() {
        AlertDialog.Builder resetBuilder = new AlertDialog.Builder(_context);
        resetBuilder.setTitle("Are you sure you want to clear your selections?");
        resetBuilder.setCancelable(false);
        resetBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (resetLikedCardList(_context) && resetNotSureCardList(_context) && resetUnlikedCardList(_context) && rebuildCardToDecide(_context)){
                    UserDataHelper.updateData(_context, _currentUserData);//Store updated user data to json file
                    spinner.setVisibility(View.VISIBLE);
                    readCardsTask = new ReadCardsTask(_context, _asyncTaskCallBack);
                    readCardsTask.execute();
                }
            }
        });
        resetBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog resetAlert = resetBuilder.create();
        resetAlert.show();
        menu.dismiss();
    }

    @Override
    public void onExport() {
        menu.dismiss();
        SendEmail sendEmail = new SendEmail(_context);
        sendEmail.execute();
    }

    @Override
    public void onFilter() {
        menu.dismiss();
        DataFilterDialog dataFilterDialog = new DataFilterDialog(_context, dialogButtonListenerCallBack);
        dataFilterDialog.show();
    }

    @Override
    public void onLogout() {
        AlertDialog.Builder logOutBuilder = new AlertDialog.Builder(_context);
        logOutBuilder.setTitle("Are you sure you wish to logout?");
        logOutBuilder.setCancelable(false);
        logOutBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserDataHelper.updateData(_context, _currentUserData);
                Intent intent = new Intent(_context, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        logOutBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog logOutAlert = logOutBuilder.create();
        logOutAlert.show();
        menu.dismiss();
    }

    @Override
    public void onSwipeChange(int imageIndex, float alpha) {
        switch (imageIndex){
            case LIKE:
                likeIndicator.setAlpha(alpha);
                dislikeIndicator.setAlpha(0.0f);
                notsureIndicator.setAlpha(0.0f);
                break;
            case DISLIKE:
                likeIndicator.setAlpha(0.0f);
                dislikeIndicator.setAlpha(alpha);
                notsureIndicator.setAlpha(0.0f);
                break;
            case NOTSURE:
                likeIndicator.setAlpha(0.0f);
                dislikeIndicator.setAlpha(0.0f);
                notsureIndicator.setAlpha(alpha);
                break;
            case ALL:
                likeIndicator.setAlpha(0.0f);
                dislikeIndicator.setAlpha(0.0f);
                notsureIndicator.setAlpha(0.0f);
                break;
            default:
                return;
        }
    }
}
