package uk.co.cherrypick.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by john on 03/10/15.
 */
public class IntroSliderContentsFragment extends Fragment {

    public IntroSliderContentsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        int pageNumber = getArguments().getInt("pageNumber");


        Log.d("SliderPage", "Loading page number: " + pageNumber);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_intro_slide_page, container, false);

        switch (pageNumber) {
            case 0:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.first_intro, container, false);

                break;

            case 1:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.swipe_right_intro, container, false);

                break;
            case 2:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.swipe_left_intro, container, false);
                break;
            case 3:
                rootView  = (ViewGroup) inflater.inflate(
                        R.layout.swipe_up_intro, container, false);
                break;
            case 4:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.swipe_tap, container, false);
                break;
        }
//
//        if(pageNumber < 4)
//        {
//            // show the next icon, but not the button if the user is on page 1..4
//            Button submitButton = (Button) rootView.findViewById(R.id.submitButton);
//            submitButton.setVisibility(View.GONE);
//        }
//
//        else
//        {
//            // show the button, but not the next icon if the user is on page 5
//            TextView continueIndicator = (TextView) rootView.findViewById(R.id.continueIndicator);
//            continueIndicator.setVisibility(View.GONE);
//
//        }
      return rootView;
    }
}
