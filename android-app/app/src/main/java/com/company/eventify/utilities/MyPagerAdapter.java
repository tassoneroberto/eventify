package com.company.eventify.utilities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.company.eventify.user.UserFragmentCalendar;
import com.company.eventify.user.UserFragmentHome;
import com.company.eventify.user.UserFragmentNear;
import com.company.eventify.user.UserFragmentSearch;
import com.company.eventify.user.UserFragmentSettings;

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
