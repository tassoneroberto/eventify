package com.company.eventify.utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.company.eventify.user.UserFragmentCalendar;
import com.company.eventify.user.UserFragmentHome;
import com.company.eventify.user.UserFragmentNear;
import com.company.eventify.user.UserFragmentSearch;
import com.company.eventify.user.UserFragmentSettings;

/**
 * Created by marco on 23/03/2017.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
        case 0:
            return UserFragmentHome.newInstance();
        case 1:
            return UserFragmentNear.newInstance();
        case 2:
            return UserFragmentCalendar.newInstance();
        case 3:
            return UserFragmentSearch.newInstance();
        case 4:
            return UserFragmentSettings.newInstance();
        default:
            return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
