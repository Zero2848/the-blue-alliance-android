package com.thebluealliance.androidclient.background;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.thebluealliance.androidclient.Constants;
import com.thebluealliance.androidclient.R;
import com.thebluealliance.androidclient.activities.RefreshableHostActivity;
import com.thebluealliance.androidclient.adapters.ListViewAdapter;
import com.thebluealliance.androidclient.database.Database;
import com.thebluealliance.androidclient.fragments.RecentNotificationsFragment;
import com.thebluealliance.androidclient.helpers.AnalyticsHelper;
import com.thebluealliance.androidclient.interfaces.RefreshListener;
import com.thebluealliance.androidclient.listitems.ListItem;
import com.thebluealliance.androidclient.listitems.RecentNotificationListItem;
import com.thebluealliance.androidclient.models.StoredNotification;

import java.util.ArrayList;

/**
 * Created by phil on 2/3/15.
 */
public class PopulateRecentNotifications extends AsyncTask<Void, Void, Void> {

    private RecentNotificationsFragment fragment;
    private RefreshableHostActivity activity;
    private ArrayList<ListItem> items;
    private ListViewAdapter adapter;
    private long startTime;

    public PopulateRecentNotifications(RecentNotificationsFragment fragment) {
        super();
        this.fragment = fragment;
        this.activity = (RefreshableHostActivity) fragment.getActivity();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(Constants.LOG_TAG, "Starting to fetch notifications");
        Database.Notifications table = Database.getInstance(activity).getNotificationsTable();
        ArrayList<StoredNotification> notifications = table.get();
        items = new ArrayList<>();
        for (StoredNotification notification : notifications) {
            items.add(new RecentNotificationListItem(notification.getTitle(), notification.getBody(), notification.getIntent()));
        }
        adapter = new ListViewAdapter(activity, items);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        View view = fragment.getView();
        if (view != null) {
            //android gets angry if you modify Views off the UI thread, so we do the actual View manipulation here
            TextView noDataText = (TextView) view.findViewById(R.id.no_data);

            // If there's no awards in the adapter or if we can't download info
            // off the web, display a message.
            if (adapter.values.isEmpty()) {
                noDataText.setText(R.string.no_recent_notifications);
                noDataText.setVisibility(View.VISIBLE);
            } else {
                ListView teamList = (ListView) view.findViewById(R.id.list);
                Parcelable state = teamList.onSaveInstanceState();
                teamList.setAdapter(adapter);
                noDataText.setVisibility(View.GONE);
                teamList.onRestoreInstanceState(state);
            }

            // give the parent a reference to the adapter
            fragment.setAdapter(adapter);

            // Remove progress spinner and show content, since we're done loading the data.
            view.findViewById(R.id.progress).setVisibility(View.GONE);
            view.findViewById(R.id.list).setVisibility(View.VISIBLE);


            // Show notification if we've refreshed data.
            if (activity != null && fragment instanceof RefreshListener) {
                Log.d(Constants.REFRESH_LOG, "Recent notifications refresh complete");
                activity.notifyRefreshComplete(fragment);
            }
        }
        AnalyticsHelper.sendTimingUpdate(activity, System.currentTimeMillis() - startTime, "notification dash", "");
    }
}
