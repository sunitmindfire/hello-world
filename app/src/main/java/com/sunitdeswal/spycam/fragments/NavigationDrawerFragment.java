package com.sunitdeswal.spycam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sunitdeswal.spycam.R;
import com.sunitdeswal.spycam.activities.HelpActivity;
import com.sunitdeswal.spycam.activities.HomeActivity;
import com.sunitdeswal.spycam.activities.MyVideosActivity;
import com.sunitdeswal.spycam.activities.SettingActivity;

/**
 * Created by Sunit deswal on 2/27/2017.
 */

public class NavigationDrawerFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{

    private View mRootView;
    private OnMyNavigationItemSelectedSelectedListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.navigationdrawer,container,false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((NavigationView)mRootView.findViewById(R.id.navigation_view)).setNavigationItemSelectedListener(this);
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
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        mListener.onMyNavigationViewItemSelected();
        switch (item.getItemId()){
            case R.id.setting :
                startActivity(new Intent(getActivity(),SettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.my_videos:
                startActivity(new Intent(getActivity(),MyVideosActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.home:
                startActivity(new Intent(getActivity(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.help:
                startActivity(new Intent(getActivity(), HelpActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            default:
                break;
        }
        return false;
    }
}
