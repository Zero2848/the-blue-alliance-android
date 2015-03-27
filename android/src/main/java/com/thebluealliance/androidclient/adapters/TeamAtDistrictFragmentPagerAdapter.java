package com.thebluealliance.androidclient.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thebluealliance.androidclient.R;
import com.thebluealliance.androidclient.fragments.district.TeamAtDistrictBreakdownFragment;
import com.thebluealliance.androidclient.fragments.district.TeamAtDistrictSummaryFragment;

/**
 * File created by phil on 7/26/14.
 */
public class TeamAtDistrictFragmentPagerAdapter extends FragmentPagerAdapter {

    public final static int[] TAB_TITLE_RES_IDS = {R.string.team_at_district_tab_summary, R.string.team_at_district_tab_breakdown};

    private static final int TAB_SUMMARY = 0, TAB_BREAKDOWN = 1;

    private String mDistrictKey, mTeamKey;

    private Context context;

    public TeamAtDistrictFragmentPagerAdapter(Context context, FragmentManager fm, String teamKey, String districtKey) {
        super(fm);
        this.context = context.getApplicationContext();
        mTeamKey = teamKey;
        mDistrictKey = districtKey;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f;
        switch (position) {
            default:
            case TAB_SUMMARY:
                f = TeamAtDistrictSummaryFragment.newInstance(mTeamKey, mDistrictKey);
                break;
            case TAB_BREAKDOWN:
                f = TeamAtDistrictBreakdownFragment.newInstance(mTeamKey, mDistrictKey);
                break;
        }
        return f;
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
