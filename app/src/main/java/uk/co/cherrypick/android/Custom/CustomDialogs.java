package uk.co.cherrypick.android.Custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.cherrypick.android.AsyncTask.AsyncTaskCallBack;
import uk.co.cherrypick.android.LogActions;
import uk.co.cherrypick.android.R;
import uk.co.cherrypick.android.Utiles.ApplicationConstants;
import uk.co.cherrypick.android.Helper.CardsHelper;
import uk.co.cherrypick.android.Helper.CardsViewHelpers;
import uk.co.cherrypick.android.Model.CardModel;
import uk.co.cherrypick.android.Model.DeviceModel;
import uk.co.cherrypick.android.Utiles.DataTemp;

import static uk.co.cherrypick.android.Utiles.ApplicationConstants._currentUserData;
import static uk.co.cherrypick.android.Utiles.Tools.updateDataFilter;

/**
 * Created by Simon on 10/8/2015.
 */
public class CustomDialogs {

    public static void offerInfoDialog(final Context context, final int id, int width, int height, final DialogButtonListener callback) {

        View detailView = LayoutInflater.from(context).inflate(R.layout.card_details_dialog, null);
        final Dialog offerDialog = new Dialog(context);
        offerDialog.setCancelable(true);
        offerDialog.setCanceledOnTouchOutside(false);
        offerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        offerDialog.setContentView(detailView);
        Window window = offerDialog.getWindow();
        window.setLayout(width - 16, height - 48);

        CardModel card = CardsHelper.getCardById(context,id);
        DeviceModel device = CardsHelper.getDeviceById(context, card.getDeviceId());
        CardsViewHelpers.setCardView(detailView, card, device);

        Button btnClose = (Button)detailView.findViewById(R.id.btn_close);
        Button btnBuy = (Button)detailView.findViewById(R.id.btn_buy);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onPositiveButtonClicked(DialogButtonListener.OFFER_CARD);
                    offerDialog.dismiss();
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onNegativeButtonClicked(DialogButtonListener.OFFER_CARD);
                    offerDialog.dismiss();
                }
            }
        });

        offerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LogActions.logAction(context, "Card List Items", "Card closed to return to list", String.valueOf(id));
            }
        });
        LogActions.logAction(context, "Card List Items", "Flip to back completed", String.valueOf(id));
        offerDialog.show();
    }
}
