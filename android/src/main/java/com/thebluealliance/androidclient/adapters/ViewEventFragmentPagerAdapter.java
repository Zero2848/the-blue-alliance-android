package com.thebluealliance.androidclient.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thebluealliance.androidclient.R;
import com.thebluealliance.androidclient.fragments.event.EventAlliancesFragment;
import com.thebluealliance.androidclient.fragments.event.EventAwardsFragment;
import com.thebluealliance.androidclient.fragments.event.EventDistrictPointsFragment;
import com.thebluealliance.androidclient.fragments.event.EventInfoFragment;
import com.thebluealliance.androidclient.fragments.event.EventMatchesFragment;
import com.thebluealliance.androidclient.fragments.event.EventRankingsFragment;
import com.thebluealliance.androidclient.fragments.event.EventStatsFragment;
import com.thebluealliance.androidclient.fragments.event.EventTeamsFragment;

/**
 * Created by Nathan on 4/22/2014.
 */
public class ViewEventFragmentPagerAdapter extends FragmentPagerAdapter {

    public final int[] TAB_TITLE_RES_IDS = {R.string.tab_event_info,
            R.string.tab_event_teams,
            R.string.tab_event_rankings,
            R.string.tab_event_matches,
            R.string.tab_event_alliances,
            R.string.tab_event_district_points,
            R.string.tab_event_stats,
            R.string.tab_event_awards};

    public static final int TAB_INFO = 0,
            TAB_TEAMS = 1,
            TAB_RANKINGS = 2,
            TAB_MATCHES = 3,
            TAB_ALLIANCES = 4,
            TAB_DISTRICT_POINTS = 5,
            TAB_STATS = 6,
            TAB_AWARDS = 7;

    private String mEventKey;
    private Context c;

    public ViewEventFragmentPagerAdapter(Context c, FragmentManager fm, String eventKey) {
        super(fm);
        this.c = c.getApplicationContext();
        mEventKey = eventKey;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return c.getString(TAB_TITLE_RES_IDS[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLE_RES_IDS.length;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            default:
            case TAB_INFO: //event info
                fragment = EventInfoFragment.newInstance(mEventKey);
                break;
            case TAB_TEAMS: //teams
                fragment = EventTeamsFragment.newInstance(mEventKey);
                break;
            case TAB_RANKINGS: //rankings
                fragment = EventRankingsFragment.newInstance(mEventKey);
                break;
            case TAB_MATCHES: //results
                fragment = EventMatchesFragment.newInstance(mEventKey);
                break;
            case TAB_ALLIANCES: //alliances
                fragment = EventAlliancesFragment.newInstance(mEventKey);
                break;
            case TAB_DISTRICT_POINTS: //district points
                fragment = EventDistrictPointsFragment.newInstance(mEventKey);
                break;
            case TAB_STATS: //stats
                fragment = EventStatsFragment.newInstance(mEventKey);
                break;
            case TAB_AWARDS: //awards
                fragment = EventAwardsFragment.newInstance(mEventKey);
                break;
        }
        return fragment;
    }
}
