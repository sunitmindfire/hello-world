package com.nikhil.spycam.activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.nikhil.spycam.R;
import com.nikhil.spycam.fragments.NavigationDrawerFragment;
import com.nikhil.spycam.utils.SpyCamUtility;

public class ContactUsActivity extends MyBaseActivity implements NavigationDrawerFragment.OnMyNavigationItemSelectedSelectedListener {

    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //disable Action Bar default Title
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        //set Text of Toolbar Tittle Text
        ((TextView) findViewById(R.id.toolbar_textView)).setText(R.string.contact_us);

        //attach Navigation Drawer to action bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_contact_us);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        SpyCamUtility.setNavigationDrawerToActionBar(mDrawerLayout, actionBarDrawerToggle);

        //add Navigation View
        SpyCamUtility.inflateFragment(new NavigationDrawerFragment(), getSupportFragmentManager(), R.id.navigation_view_container, false, false, getString(R.string.navigation_fragment_tag));
    }

    @Override
    public void onMyNavigationViewItemSelected() {
        mDrawerLayout.closeDrawer(GravityCompat.START);

    }
}
