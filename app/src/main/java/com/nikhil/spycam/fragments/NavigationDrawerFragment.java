package com.nikhil.spycam.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nikhil.spycam.R;
import com.nikhil.spycam.activities.ContactUsActivity;
import com.nikhil.spycam.activities.HelpActivity;
import com.nikhil.spycam.activities.HomeActivity;
import com.nikhil.spycam.activities.MyVideosActivity;
import com.nikhil.spycam.activities.SettingActivity;
import com.nikhil.spycam.constants.SpyCamConstants;

/**
 * This class contains Navigation Drawer
 * <p>
 * Created by Sunit deswal on 2/27/2017.
 */

public class NavigationDrawerFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private View mRootView;
    private OnMyNavigationItemSelectedSelectedListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.navigationdrawer, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((NavigationView) mRootView.findViewById(R.id.navigation_view)).setNavigationItemSelectedListener(this);
    }

    // Container Activity must implement this interface
    public interface OnMyNavigationItemSelectedSelectedListener {
        void onMyNavigationViewItemSelected();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (OnMyNavigationItemSelectedSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + getString(R.string.listener_error));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        mListener.onMyNavigationViewItemSelected();
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(getActivity(), SettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.my_videos:
                startActivity(new Intent(getActivity(), MyVideosActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.home:
                startActivity(new Intent(getActivity(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.help:
                startActivity(new Intent(getActivity(), HelpActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.contact_us:
                startActivity(new Intent(getActivity(), ContactUsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.rate_the_app:
                rateApp();
                break;

            default:
                break;
        }
        return false;
    }

    private void rateApp() {
        Uri uri = Uri.parse(SpyCamConstants.MARKET_URL + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            //tale user to the app in playstore
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            //open play store
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(SpyCamConstants.PLAY_STORE_URL + getActivity().getPackageName())));
        }
    }
}
