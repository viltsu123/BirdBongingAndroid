package com.example.viltsu.birdbonging;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ville-pekkapalmgren on 19/03/18.
 */

public class SimpleFragmentAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"List", "Birds Info", "Points"};

    public SimpleFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new ListFragment();
        }else if(position == 1){
            return new BirdInfoFragment();
        }else{
            return new PointsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
