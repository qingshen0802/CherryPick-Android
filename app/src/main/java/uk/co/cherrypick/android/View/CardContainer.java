package uk.co.cherrypick.android.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import java.util.Random;

import uk.co.cherrypick.android.Model.CardModel;
import uk.co.cherrypick.android.Model.Orientations.Orientation;
import uk.co.cherrypick.android.R;

public class CardContainer extends AdapterView<ListAdapter> {
    public static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private static final double DISORDERED_MAX_ROTATION_RADIANS = Math.PI / 128;
    private int mNumberOfCards = -1;
    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            clearStack();
            ensureFull();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            clearStack();
        }
    };
    private final Random mRandom = new Random();
    private final Rect boundsRect = new Rect();
    private final Rect childRect = new Rect();
    private final Matrix mMatrix = new Matrix();


    //TODO: determine max dynamically based on device speed
    private int mMaxVisible = 3;
    private GestureDetector mGestureDetector;
    private int mFlingSlop;
    private Orientation mOrientation;
    private ListAdapter mListAdapter;
    private float mLastTouchX;
    private float mLastTouchY;
    private View mTopCard;
    private int mTouchSlop;
    private int mGravity;
    private int mNextAdapterPosition;
    private boolean mDragging;
    private int itemOffset = 0;
    private Context mContext;
    private SwipeChangeListener callback;

    public CardContainer(Context context) {
        super(context);
        mContext = context;
        setOrientation(Orientation.Disordered);
        setGravity(Gravity.CENTER);
        init();

    }

    public CardContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initFromXml(attrs);
        init();
    }


    public CardContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initFromXml(attrs);
        init();
    }

    private void init() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());

        mFlingSlop = viewConfiguration.getScaledMinimumFlingVelocity();
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mGestureDetector = new GestureDetector(getContext(), new GestureListener());
        itemOffset = 0;
        callback = (SwipeChangeListener) getContext();
    }

    private void initFromXml(AttributeSet attr) {
        TypedArray a = getContext().obtainStyledAttributes(attr,
                R.styleable.CardContainer);

        setGravity(a.getInteger(R.styleable.CardContainer_android_gravity, Gravity.CENTER));
        int orientation = a.getInteger(R.styleable.CardContainer_orientation, 1);
        setOrientation(Orientation.fromIndex(orientation));
        a.recycle();
    }


    @Override
    public ListAdapter getAdapter() {
        return mListAdapter;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (mListAdapter != null)
            mListAdapter.unregisterDataSetObserver(mDataSetObserver);

        clearStack();
        mTopCard = null;
        mListAdapter = adapter;
        mNextAdapterPosition = 0;
        adapter.registerDataSetObserver(mDataSetObserver);

        ensureFull();

        if (getChildCount() != 0) {
            mTopCard = getChildAt(getChildCount() - 1);
            mTopCard.setLayerType(LAYER_TYPE_HARDWARE, null);
        }
        mNumberOfCards = getAdapter().getCount();
        requestLayout();
    }

    private void ensureFull() {
        while (mNextAdapterPosition < mListAdapter.getCount() && getChildCount() < mMaxVisible) {
            View view = mListAdapter.getView(mNextAdapterPosition, null, this);
            view.setLayerType(LAYER_TYPE_SOFTWARE, null);
            if (mOrientation == Orientation.Disordered) {
                view.setRotation(getDisorderedRotation());
            }
            addViewInLayout(view, 0, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                    mListAdapter.getItemViewType(mNextAdapterPosition)), false);

            requestLayout();
            mNextAdapterPosition += 1;
        }
    }

    private void clearStack() {
        removeAllViewsInLayout();
        mNextAdapterPosition = 0;
        mTopCard = null;
        itemOffset = 0;
    }

    public Orientation getOrientation() {
        return mOrientation;
    }

    public void setOrientation(Orientation orientation) {
        if (orientation == null)
            throw new NullPointerException("Orientation may not be null");
        if (mOrientation != orientation) {
            this.mOrientation = orientation;
            if (orientation == Orientation.Disordered) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.setRotation(getDisorderedRotation());
                }
            } else {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.setRotation(0);
                }
            }
            requestLayout();
        }

    }

    private float getDisorderedRotation() {
        return (float) Math.toDegrees(mRandom.nextGaussian() * DISORDERED_MAX_ROTATION_RADIANS);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int requestedWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int requestedHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int childWidth, childHeight;

        if (mOrientation == Orientation.Disordered) {
            int R1, R2;
            if (requestedWidth >= requestedHeight) {
                R1 = requestedHeight;
                R2 = requestedWidth;
            } else {
                R1 = requestedWidth;
                R2 = requestedHeight;
            }
            childWidth = (int) ((R1 * Math.cos(DISORDERED_MAX_ROTATION_RADIANS) - R2 * Math.sin(DISORDERED_MAX_ROTATION_RADIANS)) / Math.cos(2 * DISORDERED_MAX_ROTATION_RADIANS));
            childHeight = (int) ((R2 * Math.cos(DISORDERED_MAX_ROTATION_RADIANS) - R1 * Math.sin(DISORDERED_MAX_ROTATION_RADIANS)) / Math.cos(2 * DISORDERED_MAX_ROTATION_RADIANS));
        } else {
            childWidth = requestedWidth;
            childHeight = requestedHeight;
        }

        int childWidthMeasureSpec, childHeightMeasureSpec;
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            assert child != null;
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        for (int i = 0; i < getChildCount(); i++) {
            boundsRect.set(0, 0, getWidth(), getHeight());

            View view = getChildAt(i);
            int w, h;
            w = view.getMeasuredWidth();
            h = view.getMeasuredHeight();

            Gravity.apply(mGravity, w, h, boundsRect, childRect);
            view.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTopCard == null) {
            return false;
        }
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        //Log.d("Touch Event", MotionEvent.actionToString(event.getActionMasked()) + " ");
        final int pointerIndex;
        final float x, y;
        final float dx, dy;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //handler.postDelayed(mLongPressed, 1000);
                mTopCard.getHitRect(childRect);

                pointerIndex = event.getActionIndex();
                x = event.getX(pointerIndex);
                y = event.getY(pointerIndex);

                if (!childRect.contains((int) x, (int) y)) {
                    return false;
                }
                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = event.getPointerId(pointerIndex);


                float[] points = new float[]{x - mTopCard.getLeft(), y - mTopCard.getTop()};
                mTopCard.getMatrix().invert(mMatrix);
                mMatrix.mapPoints(points);
                mTopCard.setPivotX(points[0]);
                mTopCard.setPivotY(points[1]);

                break;
            case MotionEvent.ACTION_MOVE:
                //handler.removeCallbacks(mLongPressed);
                pointerIndex = event.findPointerIndex(mActivePointerId);
                x = event.getX(pointerIndex);
                y = event.getY(pointerIndex);

                dx = x - mLastTouchX;
                dy = y - mLastTouchY;

                if (Math.abs(dx) > mTouchSlop || Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }

                if (!mDragging) {
                    return true;
                }
                // Showing arrow iamges
                mTopCard.setTranslationX(mTopCard.getTranslationX() + dx);
                mTopCard.setTranslationY(mTopCard.getTranslationY() + dy);
                mTopCard.setRotation(20 * mTopCard.getTranslationX() / (getWidth() / 2.f));

                if (Math.abs(dy) > mTouchSlop && mTopCard.getTranslationY() < 0){
//                    mTopCard.findViewById(R.id.notsure_indicator).setAlpha(Math.abs(mTopCard.getTranslationY() / boundsRect.height() * 3));
//                    mTopCard.findViewById(R.id.like_indicator).setAlpha(0.0f);
//                    mTopCard.findViewById(R.id.dislike_indicator).setAlpha(0.0f);
                    if (callback != null){
                        callback.onSwipeChange(SwipeChangeListener.NOTSURE, Math.abs(mTopCard.getTranslationY() / boundsRect.height() * 3));
                    }
                }
//
                if (mTopCard.getTranslationX() < 0 && Math.abs(dx) > mTouchSlop) {
//                    mTopCard.findViewById(R.id.dislike_indicator).setAlpha(Math.abs(mTopCard.getTranslationX() / boundsRect.width() * 3));
//                    mTopCard.findViewById(R.id.like_indicator).setAlpha(0.0f);
//                    mTopCard.findViewById(R.id.notsure_indicator).setAlpha(0.0f);
                    if (callback != null){
                        callback.onSwipeChange(SwipeChangeListener.DISLIKE, Math.abs(mTopCard.getTranslationX() / boundsRect.width() * 3));
                    }
                }else if(mTopCard.getTranslationX() > 0 && Math.abs(dx) > mTouchSlop) {
//                    mTopCard.findViewById(R.id.like_indicator).setAlpha(mTopCard.getTranslationX() / boundsRect.width() * 3);
//                    mTopCard.findViewById(R.id.dislike_indicator).setAlpha(0.0f);
//                    mTopCard.findViewById(R.id.notsure_indicator).setAlpha(0.0f);
                    if (callback != null){
                        callback.onSwipeChange(SwipeChangeListener.LIKE, mTopCard.getTranslationX() / boundsRect.width() * 3);
                    }
                }

                mLastTouchX = x;
                mLastTouchY = y;
                break;
            case MotionEvent.ACTION_UP:
                //handler.removeCallbacks(mLongPressed);
            case MotionEvent.ACTION_CANCEL:
                if (!mDragging) {
                    return true;
                }
                mDragging = false;
                mActivePointerId = INVALID_POINTER_ID;
                ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mTopCard,
                        PropertyValuesHolder.ofFloat("translationX", 0),
                        PropertyValuesHolder.ofFloat("translationY", 0),
                        PropertyValuesHolder.ofFloat("rotation", (float) Math.toDegrees(mRandom.nextGaussian() * DISORDERED_MAX_ROTATION_RADIANS)),
                        PropertyValuesHolder.ofFloat("pivotX", mTopCard.getWidth() / 2.f),
                        PropertyValuesHolder.ofFloat("pivotY", mTopCard.getHeight() / 2.f)
                ).setDuration(250);
//                mTopCard.findViewById(R.id.like_indicator).setAlpha(0.0f);
//                mTopCard.findViewById(R.id.dislike_indicator).setAlpha(0.0f);
//                mTopCard.findViewById(R.id.notsure_indicator).setAlpha(0.0f);
                if (callback != null){
                    callback.onSwipeChange(SwipeChangeListener.ALL, 0);
                }
                animator.setInterpolator(new AccelerateInterpolator());
                animator.start();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointerIndex = event.getActionIndex();
                Log.d("", "");
                final int pointerId = event.getPointerId(pointerIndex);

                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);

                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mTopCard == null) {
            return false;
        }
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        final int pointerIndex;
        final float x, y;
        final float dx, dy;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //  mTopCard.getHitRect(childRect);

                CardModel cardModel = (CardModel) getAdapter().getItem(getChildCount() - 1);

                if (cardModel.getOnClickListener() != null) {
                    cardModel.getOnClickListener().OnClickListener();
                }
                pointerIndex = event.getActionIndex();
                x = event.getX(pointerIndex);
                y = event.getY(pointerIndex);

                // allow all screen touchs to count as touching the top card.
//                if (!childRect.contains((int) x, (int) y)) {
//                    return false;
//                }

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = event.getPointerId(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == -1) {
                    return false;
                }
                try {
                    pointerIndex = event.findPointerIndex(mActivePointerId);
                    if (pointerIndex == -1) {
                        return false;
                    }
                    x = event.getX(pointerIndex);
                    y = event.getY(pointerIndex);
                    if (Math.abs(x - mLastTouchX) > mTouchSlop || Math.abs(y - mLastTouchY) > mTouchSlop) {
                        float[] points = new float[]{x - mTopCard.getLeft(), y - mTopCard.getTop()};
                        mTopCard.getMatrix().invert(mMatrix);
                        mMatrix.mapPoints(points);
                        mTopCard.setPivotX(points[0]);
                        mTopCard.setPivotY(points[1]);
                        return true;
                    }
                }
                catch(Exception e)
                {
                    Log.d("Swipe error", e.getMessage());
                }
        }

        return false;
    }

    @Override
    public View getSelectedView() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSelection(int position) {
        throw new UnsupportedOperationException();
    }

    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        int viewType;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(int w, int h, int viewType) {
            super(w, h);
            this.viewType = viewType;
        }
    }

    private class GestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("Fling", "Fling with " + velocityX + ", " + velocityY);
            final View topCard = mTopCard;
            float dx = e2.getX() - e1.getX();
            float dy = e2.getY() - e1.getY();

            float targetX = topCard.getX();
            float targetY = topCard.getY();
            long duration = 0;

            if (Math.abs(dx) > mTouchSlop &&
                    Math.abs(velocityX) > Math.abs(velocityY) &&
                    Math.abs(velocityX) > mFlingSlop * 3) {

                Log.d("GestureListener", "In first If");
                boundsRect.set(0 - topCard.getWidth() - 100, 0 - topCard.getHeight() - 100, getWidth() + 100, getHeight() + 100);

                while (boundsRect.contains((int) targetX, (int) targetY)) {
                    targetX += velocityX / 10;
                    targetY += velocityY / 10;
                    duration += 100;
                }

                duration = Math.min(500, duration);

                mTopCard = getChildAt(getChildCount() - 2);
                // CardModel cardModel = (CardModel)getAdapter().getItem(getChildCount() - 1);
                CardModel cardModel = (CardModel) getAdapter().getItem(itemOffset);

                if (mTopCard != null)
                    mTopCard.setLayerType(LAYER_TYPE_HARDWARE, null);

                if (cardModel.getOnCardDismissedListener() != null) {
                    if (targetX > 0) {
                        cardModel.getOnCardDismissedListener().onLike();
                    } else {
                        cardModel.getOnCardDismissedListener().onDislike();
                    }
                }

                animateCard(velocityX, topCard, targetX, targetY, duration);
                return true;
            } else if (Math.abs(dy) > mTouchSlop &&
                    Math.abs(velocityY) > Math.abs(velocityX) &&
                    Math.abs(velocityY) > mFlingSlop * 3) {

                while (boundsRect.contains((int) targetX, (int) targetY)) {
                    targetX += velocityX / 10;
                    targetY += velocityY / 10;
                    duration += 100;
                }

                duration = Math.min(500, duration);

                if (targetY > 0) {
                    // swiping down. don't do anything.
                    return false;
                } else {
                    mTopCard = getChildAt(getChildCount() - 2);
                    CardModel cardModel = (CardModel) getAdapter().getItem(itemOffset);
                    if (cardModel.getOnCardDismissedListener() != null) {
                        if (mTopCard != null)
                            mTopCard.setLayerType(LAYER_TYPE_HARDWARE, null);
                        boundsRect.set(0 - topCard.getWidth() - 100, 0 - topCard.getHeight() - 100, getWidth() + 100, getHeight() + 100);
                        cardModel.getOnCardDismissedListener().onMaybe();
                    }
                    animateCard(velocityX, topCard, targetX, targetY, duration);
                }
                return true;
            } else
                return false;
        }

        private void animateCard(float velocityX, final View topCard, float targetX, float targetY, long duration) {
            topCard.animate()
                    .setDuration(duration)
                    .alpha(.75f)
                    .setInterpolator(new LinearInterpolator())
                    .x(targetX)
                    .y(targetY)
                    .rotation(Math.copySign(45, velocityX))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            removeViewInLayout(topCard);
                            itemOffset++;
                            ensureFull();
                        }
                        @Override
                        public void onAnimationCancel(Animator animation) {
                            onAnimationEnd(animation);
                        }
                    });
            if (callback != null){
                callback.onSwipeChange(SwipeChangeListener.ALL, 0.0f);
            }
        }
    }

    /**
     * A handler object, used for deferring UI operations.
     */
    private Handler mHandler = new Handler();

    /**
     * Whether or not we're showing the back of the card (otherwise showing the front).
     */
    private boolean mShowingBack = false;
    /*private void flipCard() {
        if (mShowingBack) {
           // getFragmentManager().popBackStack();
            return;
        }

        // Flip to the back.

        mShowingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.container, new CardFlipActivity.CardBackFragment())

                        // Add this transaction to the back stack, allowing users to press Back
                        // to get to the front of the card.
                .addToBackStack(null)

                        // Commit the transaction.
                .commit();

        // Defer an invalidation of the options menu (on modern devices, the action bar). This
        // can't be done immediately because the transaction may not yet be committed. Commits
        // are asynchronous in that they are posted to the main thread's message loop.
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    invalidateOptionsMenu();
//                }
//            });
    }*/

    //@Override
    public void onBackStackChanged() {
        // mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
    }
}
