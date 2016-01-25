package uk.co.cherrypick.android.Utiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import java.util.ArrayList;

import uk.co.cherrypick.android.Helper.CardsHelper;
import uk.co.cherrypick.android.Model.CardModel;
import uk.co.cherrypick.android.Model.DeviceModel;
import uk.co.cherrypick.android.Model.User;
import uk.co.cherrypick.android.Model.UserData;
import uk.co.cherrypick.android.R;
import uk.co.cherrypick.android.Utiles.ApplicationConstants.Network;
import uk.co.cherrypick.android.Utiles.ApplicationConstants.OperatingSystemType;

import static uk.co.cherrypick.android.Utiles.ApplicationConstants._currentUserData;
/**
 * Created by Simon on 10/13/2015.
 */
public class Tools {

    public interface MenuButtonClickListener {
        public void onReset();
        public void onExport();
        public void onFilter();
        public void onLogout();
    }

    public static boolean updateDataFilter(int network, int system, int memory, int data, int price){
        if (_currentUserData != null){
            _currentUserData.setNetworkFilter(network);
            _currentUserData.setSystemFilter(system);
            _currentUserData.setMemoryFilter(memory);
            _currentUserData.setDataAllowanceFilter(data);
            _currentUserData.setPriceFilter(price);
            return true;
        } else{
            return false;
        }
    }

    public static boolean matchCardFilter(Context context, CardModel card){
        DeviceModel deviceModel = CardsHelper.getDeviceById(context, card.getDeviceId());
        Network network = card.getNetwork();
        OperatingSystemType system = deviceModel.getOperatingSystemType();
        String dataAllowance = card.getDataAllowance();
        String memory = deviceModel.getDeviceMemory();
        double price = card.getMonthlyCost();
        String[] dataAllowances = context.getResources().getStringArray(R.array.minimum_data_allowance);
        String[] memories = context.getResources().getStringArray(R.array.minimum_memory);

        if (network != null && system != null && dataAllowance != null && memory != null) {
            if (_currentUserData.getNetworkFilter() == 0 || network.equals(Network.values()[_currentUserData.getNetworkFilter()])) {
                if (_currentUserData.getSystemFilter() == 0 || system.equals(OperatingSystemType.values()[_currentUserData.getSystemFilter()])) {
                    if (_currentUserData.getDataAllowanceFilter() == 0 || dataAllowance.equals("Unlimited") || Integer.parseInt(dataAllowance.replaceAll("[^0-9]", "")) >= Integer.parseInt(dataAllowances[_currentUserData.getDataAllowanceFilter()].replaceAll("[^0-9]", ""))) {
                        if (_currentUserData.getMemoryFilter() == 0 || Integer.parseInt(memory.replaceAll("[^0-9]", "")) >= Integer.parseInt(memories[_currentUserData.getMemoryFilter()].replaceAll("[^0-9]", ""))) {
                            if (_currentUserData.getPriceFilter() == 0 || price <= _currentUserData.getPriceFilter()) {
                                return true;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public static PopupWindow displayMenu(Context context, final MenuButtonClickListener callback){
        PopupWindow popupWindow = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.menu_layout, null);
        Button menuReset = (Button)view.findViewById(R.id.menu_btn_reset);
        Button menuExport = (Button)view.findViewById(R.id.menu_btn_export);
        Button menuFilter = (Button)view.findViewById(R.id.menu_btn_filter);
        Button menuLogOut = (Button)view.findViewById(R.id.menu_btn_logout);

        menuReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onReset();
            }
        });
        menuExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onExport();
            }
        });
        menuFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onFilter();
            }
        });
        menuLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onLogout();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setWidth(640);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        return popupWindow;
    }

    public static UserData backUpUserData(UserData userData){
        UserData temp = new UserData();
        temp.setDataAllowanceFilter(userData.getDataAllowanceFilter());
        temp.setDislikedCards(userData.getDislikedCards());
        temp.setIsRevisiting(userData.isRevisiting());
        temp.setLikedCards(userData.getLikedCards());
        temp.setMemoryFilter(userData.getMemoryFilter());
        temp.setNetworkFilter(userData.getNetworkFilter());
        temp.setNotSureCards(userData.getNotSureCards());
        temp.setPriceFilter(userData.getPriceFilter());
        temp.setSourceCards(userData.getSourceCards());
        temp.setSystemFilter(userData.getSystemFilter());

        return temp;
    }
}
