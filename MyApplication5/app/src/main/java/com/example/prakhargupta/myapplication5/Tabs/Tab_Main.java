package com.example.prakhargupta.myapplication5.Tabs;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.example.prakhargupta.myapplication5.MainActivity;
import com.example.prakhargupta.myapplication5.R;

public class Tab_Main extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    Intent intent;
//    private ActionBar actionBar;
    // Tab titles
//    private String[] tabs = { "Top Rated", "Games", "Movies" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
//        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        final SharedPreferences.Editor ed = sp.edit();

        String str = sp.getString("username", "N/A");
        if (!str.equals("N/A"))
        {
            intent = new Intent(Tab_Main.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        // Adding Tabs
//        for (String tab_name : tabs) {
//            actionBar.addTab(actionBar.newTab().setText(tab_name)
//                    .setTabListener(this));
//        }
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
//                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
