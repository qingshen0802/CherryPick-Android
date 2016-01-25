package uk.co.cherrypick.android.View;

/**
 * Created by KKX on 10/24/2015.
 */
public interface SwipeChangeListener {
    public static int LIKE = 0;
    public static int DISLIKE = 1;
    public static int NOTSURE = 2;
    public static int ALL = 3;
    public void onSwipeChange(int imageIndex, float alpha);
}
