package uk.co.cherrypick.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.cherrypick.android.Model.Session;

public class IntroScreenActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final int NUM_PAGES = 5;
    private ViewPager introScreens;
    private PagerAdapter myPagerAdapter;
    private TextView btnNext, btnPrevious;
    private Button btnOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_s);

        introScreens = (ViewPager) this.findViewById(R.id.viewPager);
        btnNext = (TextView) this.findViewById(R.id.btn_next);
        btnPrevious = (TextView) this.findViewById(R.id.btn_previous);
        btnOk = (Button) this.findViewById(R.id.btn_intro_ok);

        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        myPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        introScreens.setAdapter(myPagerAdapter);
        refreshView();

        introScreens.addOnPageChangeListener(this);

        // write user actions to file
        Log.d("UserActionLog", "Started logging user action....");
        // get user session object
        Session session = uk.co.cherrypick.android.Model.User.getSession();
        LogActions userAction = new LogActions();
        userAction.logAction(this, session.getName(), "InstructionView", "Instruction page viewed");
    }

    private void refreshView(){
        if (introScreens.getCurrentItem() == 0){
            btnPrevious.setVisibility(View.GONE);
            btnOk.setVisibility(View.GONE);
        } else if (introScreens.getCurrentItem() < 4){
            btnOk.setVisibility(View.GONE);
            btnPrevious.setVisibility(View.VISIBLE);
        } else {
            btnOk.setVisibility(View.VISIBLE);
            btnPrevious.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                introScreens.setCurrentItem(introScreens.getCurrentItem() + 1);
                refreshView();
                break;
            case R.id.btn_previous:
                introScreens.setCurrentItem(introScreens.getCurrentItem() - 1);
                refreshView();
                break;
            case R.id.btn_intro_ok:
                Session session = uk.co.cherrypick.android.Model.User.getSession();
                LogActions userAction = new LogActions();
                userAction.logAction(IntroScreenActivity.this, session.getName(),"InstructionView","Instruction completed");
                Intent intent = new Intent(IntroScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                return;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        refreshView();
    }

    @Override
    public void onPageSelected(int position) {
        refreshView();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            IntroSliderContentsFragment sliderPage = new IntroSliderContentsFragment();
            Bundle args = new Bundle();
            Log.d("Slider call", "trying to load" + position);
            args.putInt("pageNumber", position);
            sliderPage.setArguments(args);

            return sliderPage;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
