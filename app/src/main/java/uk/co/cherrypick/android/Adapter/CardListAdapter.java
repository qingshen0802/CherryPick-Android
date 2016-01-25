package uk.co.cherrypick.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.cherrypick.android.Model.CardItem;
import uk.co.cherrypick.android.R;

/**
 * Created by Simon on 10/28/2015.
 */
public class CardListAdapter extends BaseAdapter {

    private static final CharSequence UNLIMTED_VALUE_STRING = "∞";
    private ArrayList<CardItem> selectedCards;
    private Context context;

    public CardListAdapter(Context context, ArrayList<CardItem> selectedCards) {
        this.selectedCards = selectedCards;
        this.context = context;
    }

    @Override
    public int getCount() {
        return selectedCards.size();
    }

    @Override
    public CardItem getItem(int position) {
        return selectedCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(CardItem item){
        selectedCards.remove(item);
        notifyDataSetChanged();
    }
    public void insert(CardItem item, int index){
        selectedCards.add(index, item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_card_list, null);

            TextView phoneTitle = (TextView) convertView.findViewById(R.id.item_title);
            TextView deviceMemory = (TextView) convertView.findViewById(R.id.item_device_memory);
            TextView deviceColor = (TextView) convertView.findViewById(R.id.item_device_color);
            TextView upfrontCost = (TextView) convertView.findViewById(R.id.item_upfront_cost);
            TextView monthlyCost = (TextView) convertView.findViewById(R.id.item_monthly_cost);
            TextView dataAllowance = (TextView) convertView.findViewById(R.id.item_data_allowance);
            TextView talkTimeAllowance = (TextView) convertView.findViewById(R.id.item_talk_time_allowance);
            TextView smsAllowance = (TextView) convertView.findViewById(R.id.item_sms_allowance);
            TextView contractDuration = (TextView) convertView.findViewById(R.id.item_contract_duration);

            CardItem cardItem = selectedCards.get(position);
            phoneTitle.setText(cardItem.getTitle());
            deviceMemory.setText(cardItem.getMemory());
//            deviceColor.setText(cardItem.getColor());
            upfrontCost.setText(cardItem.getUpfront());
            monthlyCost.setText(cardItem.getMonthly());

            dataAllowance.setText(cardItem.getDataAllowance());
            talkTimeAllowance.setText(cardItem.getTalkTimeAllowance());
            smsAllowance.setText(cardItem.getSmsAllowance());
            contractDuration.setText(cardItem.getContractDuration());

            if(talkTimeAllowance.getText().equals(UNLIMTED_VALUE_STRING)) {
                talkTimeAllowance.setVisibility(View.GONE);
                convertView.findViewById((R.id.talktimeAllowanceUnlimited)).setVisibility(View.VISIBLE);
            }
            if(dataAllowance.getText().equals(UNLIMTED_VALUE_STRING)) {
                dataAllowance.setVisibility(View.GONE);
                convertView.findViewById((R.id.dataAllowanceUnlimited)).setVisibility(View.VISIBLE);
            }
            if(smsAllowance.getText().equals(UNLIMTED_VALUE_STRING)) {
                smsAllowance.setVisibility(View.GONE);
                convertView.findViewById((R.id.smsAllowanceUnlimited)).setVisibility(View.VISIBLE);
            }
//        }
        return convertView;
    }
}
