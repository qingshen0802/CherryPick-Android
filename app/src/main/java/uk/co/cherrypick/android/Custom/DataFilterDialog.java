package uk.co.cherrypick.android.Custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.cherrypick.android.Helper.UserDataHelper;
import uk.co.cherrypick.android.MainActivity;
import uk.co.cherrypick.android.Model.UserData;
import uk.co.cherrypick.android.R;

import static uk.co.cherrypick.android.Helper.UserCardHelpers.rebuildCardToDecide;
import static uk.co.cherrypick.android.Utiles.ApplicationConstants._currentUserData;
import static uk.co.cherrypick.android.Utiles.Tools.backUpUserData;
import static uk.co.cherrypick.android.Utiles.Tools.matchCardFilter;
import static uk.co.cherrypick.android.Utiles.Tools.updateDataFilter;

/**
 * Created by Simon on 10/14/2015.
 */
public class DataFilterDialog extends Dialog implements AdapterView.OnItemSelectedListener, TextWatcher, View.OnClickListener {

    private Spinner networkSpinner, systemSpinner, memorySpinner, dataAllowanceSpinner;
    private EditText monthlyPrice;
    private TextView matchCardsNumber;
    private ProgressBar calculatorBar;
    private Context _context;
    private DialogButtonListener _dialogButtonListener;

    private UserData userDataTemp;

    public DataFilterDialog(Context context, DialogButtonListener dialogButtonListener) {
        super(context);
        _context = context;
        _dialogButtonListener = dialogButtonListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_data_filter);

        Button btnSave = (Button) this.findViewById(R.id.btn_save);
        Button btnCancel = (Button) this.findViewById(R.id.btn_cancel);
        monthlyPrice = (EditText) this.findViewById(R.id.edt_monthly_price);
        networkSpinner = (Spinner) this.findViewById(R.id.network_spinner);
        systemSpinner = (Spinner) this.findViewById(R.id.system_spinner);
        memorySpinner = (Spinner) this.findViewById(R.id.memory_spinner);
        dataAllowanceSpinner = (Spinner) this.findViewById(R.id.data_allowance_spinner);
        matchCardsNumber = (TextView) this.findViewById(R.id.match_result);
        calculatorBar = (ProgressBar) this.findViewById(R.id.calculation_progress_bar);

        networkSpinner.setSelection(_currentUserData.getNetworkFilter());
        systemSpinner.setSelection(_currentUserData.getSystemFilter());
        memorySpinner.setSelection(_currentUserData.getMemoryFilter());
        dataAllowanceSpinner.setSelection(_currentUserData.getDataAllowanceFilter());
        monthlyPrice.setText(String.valueOf(_currentUserData.getPriceFilter()));

        networkSpinner.setOnItemSelectedListener(this);
        systemSpinner.setOnItemSelectedListener(this);
        memorySpinner.setOnItemSelectedListener(this);
        dataAllowanceSpinner.setOnItemSelectedListener(this);
        monthlyPrice.addTextChangedListener(this);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        calculatorBar.setVisibility(View.GONE);
        userDataTemp = backUpUserData(_currentUserData);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        calculatorBar.setVisibility(View.VISIBLE);
        matchCardsNumber.setText("Matching Offers: ");
        String price = monthlyPrice.getText().toString();
        if (price.equals("")){
            price = "0";
        }
        boolean result = updateDataFilter(
                networkSpinner.getSelectedItemPosition(),
                systemSpinner.getSelectedItemPosition(),
                memorySpinner.getSelectedItemPosition(),
                dataAllowanceSpinner.getSelectedItemPosition(),
                Integer.parseInt(price)
        );
        if (rebuildCardToDecide(_context) && result) {
            calculatorBar.setVisibility(View.GONE);
            matchCardsNumber.setText("Matching Offers: " + String.valueOf(_currentUserData.getSourceCards().size()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() != 0) {
            calculatorBar.setVisibility(View.VISIBLE);
            matchCardsNumber.setText("Matching Offers: ");
            boolean result = updateDataFilter(
                    networkSpinner.getSelectedItemPosition(),
                    systemSpinner.getSelectedItemPosition(),
                    memorySpinner.getSelectedItemPosition(),
                    dataAllowanceSpinner.getSelectedItemPosition(),
                    Integer.parseInt(monthlyPrice.getText().toString())
            );
            if (rebuildCardToDecide(_context) && result) {
                calculatorBar.setVisibility(View.GONE);
                matchCardsNumber.setText("Matching Offers: " + String.valueOf(_currentUserData.getSourceCards().size()));
            }
        } else {
            calculatorBar.setVisibility(View.VISIBLE);
            matchCardsNumber.setText("Matching Offers: ");
            boolean result = updateDataFilter(
                    networkSpinner.getSelectedItemPosition(),
                    systemSpinner.getSelectedItemPosition(),
                    memorySpinner.getSelectedItemPosition(),
                    dataAllowanceSpinner.getSelectedItemPosition(),
                    0
            );
            if (rebuildCardToDecide(_context) && result) {
                calculatorBar.setVisibility(View.GONE);
                matchCardsNumber.setText("Matching Offers: " + String.valueOf(_currentUserData.getSourceCards().size()));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                _currentUserData = backUpUserData(userDataTemp);
                this.dismiss();
                break;
            case R.id.btn_save:
                UserDataHelper.updateData(_context, _currentUserData);
                if (_dialogButtonListener != null)
                    if (!monthlyPrice.getText().toString().equals("")) {
                        boolean result = updateDataFilter(
                                networkSpinner.getSelectedItemPosition(),
                                systemSpinner.getSelectedItemPosition(),
                                memorySpinner.getSelectedItemPosition(),
                                dataAllowanceSpinner.getSelectedItemPosition(),
                                Integer.parseInt(monthlyPrice.getText().toString())
                        );
                        if (result) {
                            _dialogButtonListener.onPositiveButtonClicked(DialogButtonListener.DATA_FILTER);
                        } else {
                            _dialogButtonListener.onNegativeButtonClicked(DialogButtonListener.DATA_FILTER);
                        }
                    } else {
                        boolean result = updateDataFilter(
                                networkSpinner.getSelectedItemPosition(),
                                systemSpinner.getSelectedItemPosition(),
                                memorySpinner.getSelectedItemPosition(),
                                dataAllowanceSpinner.getSelectedItemPosition(),
                                0);
                        if (result) {
                            _dialogButtonListener.onPositiveButtonClicked(DialogButtonListener.DATA_FILTER);
                        } else {
                            _dialogButtonListener.onNegativeButtonClicked(DialogButtonListener.DATA_FILTER);
                        }
                    }
                this.dismiss();
                break;
            default:
                return;
        }
    }
}
