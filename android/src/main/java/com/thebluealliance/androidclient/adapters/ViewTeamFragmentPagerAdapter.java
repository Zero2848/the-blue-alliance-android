package com.thebluealliance.androidclient.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thebluealliance.androidclient.R;
import com.thebluealliance.androidclient.fragments.team.TeamEventsFragment;
import com.thebluealliance.androidclient.fragments.team.TeamInfoFragment;
import com.thebluealliance.androidclient.fragments.team.TeamMediaFragment;

/**
 * Created by Nathan on 4/22/2014.
 */
public class ViewTeamFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLE_RES_IDS = {R.string.view_team_tab_info,
            R.string.view_team_tab_events,
            R.string.view_team_tab_media};

    private static final int TAB_INFO = 0,
            TAB_EVENTS = 1,
            TAB_MEDIA = 2;

    private String mTeamKey;
    private Context context;

    public ViewTeamFragmentPagerAdapter(Context context, FragmentManager fm, String teamKey) {
        super(fm);
        this.context = context.getApplicationContext();
        mTeamKey = teamKey;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(TAB_TITLE_RES_IDS[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLE_RES_IDS.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TAB_INFO:
                return TeamInfoFragment.newInstance(mTeamKey);
            case TAB_EVENTS:
                return TeamEventsFragment.newInstance(mTeamKey, -1);
            case TAB_MEDIA:
            default:
                return TeamMediaFragment.newInstance(mTeamKey, -1);
        }

    }
}
