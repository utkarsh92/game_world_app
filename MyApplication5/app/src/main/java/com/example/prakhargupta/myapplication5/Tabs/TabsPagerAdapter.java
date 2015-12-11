package com.example.prakhargupta.myapplication5.Tabs;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Prakhar Gupta on 30/11/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new Tab1();
            case 1:
                // Games fragment activity
                return new Tab2();
            case 2:
                // Movies fragment activity
                return new Tab3();
            case 3:
                return new Tab4();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}
