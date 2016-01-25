package uk.co.cherrypick.android.Helper;

import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import uk.co.cherrypick.android.R;
import uk.co.cherrypick.android.Model.CardModel;
import uk.co.cherrypick.android.Model.DeviceModel;

import java.text.DecimalFormat;

/**
 * Created by KKX on 05/10/2015.
 */
public class CardsViewHelpers {

    final private static String UNLIMTED_VALUE_STRING = "\u221e";

    public static String unlimitedCurrent(String value)
    {
        if((value.trim().equalsIgnoreCase("unlimited")))
        {
            return UNLIMTED_VALUE_STRING;
        }
        else
        {
            return value;
        }
    }

    @Nullable
    public static void setCardView( View convertView, CardModel model, DeviceModel device) {
        ((TextView) convertView.findViewById(R.id.title)).setText(model.getTitle());

        DecimalFormat df = new DecimalFormat("0.00");

        String upFrontCost = "";
        if(model.getUpfrontCost() > 0.0)
        {
            upFrontCost = "£" + df.format(model.getUpfrontCost());
        }
        else
        {
            upFrontCost = "FREE";
        }
        ((TextView) convertView.findViewById(R.id.upfrontCost)).setText(upFrontCost);


        String monthlyCost = "";
        if(model.getMonthlyCost() > 0.0)
        {
            monthlyCost = "£" + df.format(model.getMonthlyCost());
        }
        else
        {
            monthlyCost = "FREE";
        }
        ((TextView) convertView.findViewById(R.id.monthlyCost)).setText(monthlyCost);

        ((TextView) convertView.findViewById(R.id.contactDurationValue)).setText(model.getContactLength().replace(" Months", "M"));


        TextView talkTimeAllowanceView =(TextView) convertView.findViewById(R.id.talktimeAllowanceLabelValue);
        TextView dataAllowanceView =(TextView) convertView.findViewById(R.id.dataAllowanceValue);
        TextView smsAllowanceView =(TextView) convertView.findViewById(R.id.smsAllowanceValue);

        talkTimeAllowanceView.setText(unlimitedCurrent(model.getTalkTimeAllowance()));
        dataAllowanceView.setText(unlimitedCurrent(model.getDataAllowance()));
        smsAllowanceView.setText(unlimitedCurrent(model.getSmsAllowance()));

        if(talkTimeAllowanceView.getText().equals(UNLIMTED_VALUE_STRING)) {
            talkTimeAllowanceView.setVisibility(View.GONE);
            ( (TextView)convertView.findViewById((R.id.talktimeAllowanceUnlimited))).setVisibility(View.VISIBLE);
        }
        if(dataAllowanceView.getText().equals(UNLIMTED_VALUE_STRING)) {
            dataAllowanceView.setVisibility(View.GONE);
            ( (TextView)convertView.findViewById((R.id.dataAllowanceUnlimited))).setVisibility(View.VISIBLE);
        }
        if(smsAllowanceView.getText().equals(UNLIMTED_VALUE_STRING)) {
            smsAllowanceView.setVisibility(View.GONE);
            ( (TextView)convertView.findViewById((R.id.smsAllowanceUnlimited))).setVisibility(View.VISIBLE);
        }


        // load the device specific bits
        if(device != null) {

            StringBuilder deviceTextString = new StringBuilder();
            deviceTextString.append(device.getCameraResolution() + " camera\r\n");


            ImageView osLogo = (ImageView) convertView.findViewById(R.id.deviceOSImage);

            if (device.getOperatingSystemType() != null) {
                switch (device.getOperatingSystemType()) {
                    case ANDROID:
                        if (device.getOperatingSystemVersion() != null && device.getOperatingSystemVersion() != "") {
                            deviceTextString.append("Android ");
                            deviceTextString.append(device.getOperatingSystemVersion());
                        }
                        osLogo.setImageResource(R.drawable.operating_system_android);
                        break;

                    case IOS:
                        if (device.getOperatingSystemVersion() != null && device.getOperatingSystemVersion() != "") {
                            deviceTextString.append("iOS");
                            deviceTextString.append(device.getOperatingSystemVersion());
                        }
                        osLogo.setImageResource(R.drawable.operating_system_ios);
                        break;

                    case WINDOWS:
                        if (device.getOperatingSystemVersion() != null && device.getOperatingSystemVersion() != "") {
                            deviceTextString.append("Windows ");
                            deviceTextString.append(device.getOperatingSystemVersion());
                        }
                        osLogo.setImageResource(R.drawable.operating_system_windows);
                        break;
                    default:
                }
            }


            ((TextView) convertView.findViewById(R.id.cameraSpecValue)).setText(device.getCameraResolution());
          //  ((TextView) convertView.findViewById(R.id.deviceText)).setText(deviceTextString);

            ImageView imageView = ((ImageView) convertView.findViewById(R.id.image));
            imageView.setImageDrawable(device.getImage());

        }
    }
}
