package uk.co.cherrypick.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import uk.co.cherrypick.android.Model.Session;

public class IntroActivity extends FragmentActivity {

    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_intro);


        // write user actions to file
        Log.d("UserActionLog", "Started logging user action....");
        // get user session object
        Session session = uk.co.cherrypick.android.Model.User.getSession();
        LogActions userAction = new LogActions();
        userAction.logAction(this,session.getName(),"InstructionView","Instruction page viewed");


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user clicks the Send button
     */
    public void sendProceed(View view) {
        // write user actions to file
        Log.d("UserActionLog", "Started logging user action....");
        // get user session object
        Session session = uk.co.cherrypick.android.Model.User.getSession();
        LogActions userAction = new LogActions();
        Context current = this;
        userAction.logAction(this,session.getName(),"InstructionView","Instruction completed");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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

    @Override
    protected void onPause() {
        super.onPause();
        LogActions.logAction(this, "Application", "App Paused", null);
    }
}
