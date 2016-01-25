package uk.co.cherrypick.android.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

import uk.co.cherrypick.android.Animation.AnimationFactory;
import uk.co.cherrypick.android.Helper.CardsHelper;
import uk.co.cherrypick.android.Helper.CardsViewHelpers;
import uk.co.cherrypick.android.Model.CardModel;
import uk.co.cherrypick.android.R;
import uk.co.cherrypick.android.Model.DeviceModel;

public final class OfferCardStackAdapter extends CardStackAdapter {


    Context mContext;

    public OfferCardStackAdapter(Context context) {
		super(context);
        mContext = context;
	}

	@Override
	public View getCardView(int position, final CardModel model, View convertView, ViewGroup parent) {

		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.card_front, parent, false);
			assert convertView != null;
		}

        final DeviceModel device = CardsHelper.getDeviceById(mContext, model.getDeviceId());
        final ViewAnimator viewFlipper = (ViewAnimator) convertView.findViewById(R.id.viewFlipper2);
        CardsViewHelpers.setCardView(convertView, model, device);

        LayoutInflater factory = LayoutInflater.from(mContext);
        View detailView = factory.inflate(
                R.layout.card_back_dialog, null);

        ((TextView) detailView.findViewById(R.id.title)).setText(model.getTitle());
        StringBuilder html = new StringBuilder();

        if(device.getDimensions()!="") {
            html.append("<b>Handset Dimensions</b><br />");
            html.append(device.getDimensions());
            html.append("<br /><br />");
        }

        if(device.getScreenSize()!="") {
            html.append("<b>Screen Size</b><br />");
            html.append(device.getScreenSize());
            html.append("<br /><br />");
        }

        if(device.getCameraDescription()!="") {
            html.append("<b>Camera</b><br />");
            html.append(device.getCameraResolution());
            html.append(" : ");
            html.append(device.getCameraDescription());
            html.append("<br /><br />");
        }

        if(device.getLaunchDate()!="") {
            html.append("<b>Launch Date</b><br />");
            html.append(device.getLaunchDate());
            html.append("<br /><br />");
        }

        if(device.getOperatingSystemVersion()!="") {
            html.append("<b>Operating System Version</b><br />");
            html.append(device.getOperatingSystemVersion());
            html.append("<br /><br />");
        }


//
//        cameraDescription = o.getString("cameraDescription");
//        batteryLifeTalk = o.getString("battee");
//        screenSize = o.getString("screenSizryLifeTalk");
//        launchDate = o.getString("launchDate");
//        operatingSystemVersion = o.getString("operatingSystemVersion");
//        colour = o.getString("colour");


        String cameraDescription = device.getCameraDescription();

                ((TextView) detailView.findViewById(R.id.deviceText)).setText(Html.fromHtml(html.toString()));

        viewFlipper.addView(detailView);

        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationFactory.flipTransition(viewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                offerInfoDialog(model, device);
                AnimationFactory.flipTransition(viewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT);
                return false;
            }
        });

        return convertView;
	}

    private void offerInfoDialog(CardModel cardModel, DeviceModel device) {
        LayoutInflater factory = LayoutInflater.from(mContext);
        final View detailView = factory.inflate(
                R.layout.card_back_dialog, null);

        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.setView(detailView);

        ((TextView) detailView.findViewById(R.id.title)).setText(cardModel.getTitle());
        StringBuilder html = new StringBuilder();
        html.append("<b>Dimensions</b><br />");
        html.append(device.getDimensions());
        ((TextView) detailView.findViewById(R.id.deviceText)).setText(Html.fromHtml(html.toString()));

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.show();
    }
}
