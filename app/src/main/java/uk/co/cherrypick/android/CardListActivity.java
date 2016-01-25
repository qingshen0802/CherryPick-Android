package uk.co.cherrypick.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.mobeta.android.dslv.DragSortListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.cherrypick.android.Adapter.CardListAdapter;
import uk.co.cherrypick.android.Custom.CustomDialogs;
import uk.co.cherrypick.android.Custom.DialogButtonListener;
import uk.co.cherrypick.android.Helper.CardsHelper;
import uk.co.cherrypick.android.Helper.UserDataHelper;
import uk.co.cherrypick.android.Model.CardItem;
import uk.co.cherrypick.android.Model.CardModel;
import uk.co.cherrypick.android.Model.DeviceModel;
import uk.co.cherrypick.android.Model.UserData;

import static uk.co.cherrypick.android.Helper.CardsViewHelpers.unlimitedCurrent;
import static uk.co.cherrypick.android.Helper.UserCardHelpers.updateSourceIds;
import static uk.co.cherrypick.android.Utiles.ApplicationConstants._currentUserData;

public class CardListActivity extends Activity implements DialogButtonListener, AdapterView.OnItemClickListener {

    private DragSortListView selectedCardList;
    private CardListAdapter mAdapter;
    private Context _context = this;
    private DialogButtonListener callback = this;
    private int height;
    private int width;
    final LogActions userAction = new LogActions();
    private File _userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_card_list);

        selectedCardList = (DragSortListView) this.findViewById(R.id.selected_card_list_view);
        selectedCardList.setDropListener(mDropListener);
        selectedCardList.setRemoveListener(mRemoveListener);

        selectedCardList.setOnItemClickListener(this);

        _userPreferences = UserDataHelper.getUserPreferences(_context);

        UserData userData = UserDataHelper.getUserData(this);
        try {
            List<Integer> selectedCards = new ArrayList<>();
            selectedCards.addAll(userData.getLikedCards());
//            selectedCards.addAll(userData.getNotSureCards());
            HashMap<Integer,CardModel> cardItems = CardsHelper.getCards(this, selectedCards);
            ArrayList<CardItem> items = new ArrayList<>();

            for (CardModel card:cardItems.values()) {
                CardItem item = new CardItem();
                DeviceModel device = CardsHelper.getDeviceById(_context, card.getDeviceId());
                item.setTitle(card.getTitle());
                item.setColor(device.getColour());
                item.setMemory(device.getDeviceMemory());
                if (card.getUpfrontCost() == 0){
                    item.setUpfront("Free");
                }else {
                    item.setUpfront("£ " + String.valueOf(card.getUpfrontCost()));
                }
                if (card.getMonthlyCost() == 0){
                    item.setMonthly("No monthly cost");
                } else {
                    item.setMonthly("£ " + String.valueOf(card.getMonthlyCost()));
                }
                item.setContractDuration(card.getContactLength().replaceAll("[^0-9]", ""));
                item.setDataAllowance(unlimitedCurrent(card.getDataAllowance()));
                item.setSmsAllowance(unlimitedCurrent(card.getSmsAllowance()));
                item.setTalkTimeAllowance(unlimitedCurrent(card.getTalkTimeAllowance()));
                item.setId(card.getId());
                items.add(item);
            }
            mAdapter = new CardListAdapter(_context, items);
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectedCardList.setAdapter(mAdapter);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    private final DragSortListView.DropListener mDropListener =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    CardItem item = mAdapter.getItem(from);
                    mAdapter.remove(item);
                    mAdapter.insert(item, to);
                }
            };

    private final DragSortListView.RemoveListener mRemoveListener =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    removeDislikeCard(mAdapter.getItem(which));
                    mAdapter.remove(mAdapter.getItem(which));
                }
            };

    private void removeDislikeCard(CardItem cardModel){
        Integer cardId = cardModel.getId();
        _currentUserData.getDislikedCards().add(cardId);
        _currentUserData.getLikedCards().remove(cardId);
        updateSourceIds(cardId, _currentUserData);
        userAction.logAction(_context, "CardStack", "DislikedOffer", "" + cardModel.getId());
        UserDataHelper.updateDislike(_userPreferences, cardId);
        UserDataHelper.updateData(_context, _currentUserData);
    }
    @Override
    public void onPositiveButtonClicked(int dialogType) {
        switch (dialogType){
            case OFFER_CARD:
                Uri uri = Uri.parse("http://www.awin1.com/cread.php?awinmid=3235&awinaffid=232761&clickref=&p=https%3A%2F%2Fwww.o2.co.uk%2Fshop%2Ffusepump%2Fadd%2Fproduct%2F%3Fdevice%3D0f702a24-de61-4ee3-9dcc-657e86929ebb%26plan%3D51c3fdeb-2c82-4c9b-8625-6df59abd0568%26dataallowance%3Dsp-1906eea6-87ea-442e-a72f-8a55ace82d95%26oneOff%3D109.99%26monthly%3D20.00"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogType) {
        switch (dialogType){
            case OFFER_CARD:
                //New logic here
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogActions.logAction(_context, "Application", "App Paused", null);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CardListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            CardItem selectedFromList = (CardItem) (selectedCardList.getItemAtPosition(position));
            CustomDialogs.offerInfoDialog(_context, selectedFromList.getId(), width, height, callback);
            LogActions.logAction(_context, "Card List Items", "Card opened from list", String.valueOf(selectedFromList.getId()));
        } catch (Exception ex) {
            ex.printStackTrace();
            CustomDialogs.offerInfoDialog(_context, 4, width, height, callback);
            LogActions.logAction(_context, "Card List Items", "Card opened from list", "4");
        }
    }
}
