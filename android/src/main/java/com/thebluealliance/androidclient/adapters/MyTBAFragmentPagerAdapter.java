package com.thebluealliance.androidclient.adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thebluealliance.androidclient.R;
import com.thebluealliance.androidclient.fragments.mytba.MyFavoritesFragment;
import com.thebluealliance.androidclient.fragments.mytba.MySubscriptionsFragment;

/**
 * File created by phil on 8/2/14.
 */
public class MyTBAFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLE_RES_IDS = {R.string.mytba_tab_favorites, R.string.mytba_tab_subscriptions};

    private static final int TAB_FAVORITES = 0, TAB_SUBSCRIPTIONS = 1;

    private Context context;

    public MyTBAFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context.getApplicationContext();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
            case TAB_FAVORITES:
                return MyFavoritesFragment.newInstance();
            case TAB_SUBSCRIPTIONS:
                return MySubscriptionsFragment.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(TAB_TITLE_RES_IDS[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLE_RES_IDS.length;
    }
}
