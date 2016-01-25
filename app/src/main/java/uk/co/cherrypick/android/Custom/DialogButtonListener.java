package uk.co.cherrypick.android.Custom;

/**
 * Created by Simon on 10/8/2015.
 */
public interface DialogButtonListener {
    public static int DATA_FILTER = 0;
    public static int OFFER_CARD = 1;

    public void onPositiveButtonClicked(int dialogType);
    public void onNegativeButtonClicked(int dialogType);
}
