package com.thebluealliance.androidclient.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.thebluealliance.androidclient.Constants;
import com.thebluealliance.androidclient.R;
import com.thebluealliance.androidclient.activities.RefreshableHostActivity;
import com.thebluealliance.androidclient.adapters.ListViewAdapter;
import com.thebluealliance.androidclient.background.PopulateRecentNotifications;
import com.thebluealliance.androidclient.eventbus.NotificationsUpdatedEvent;
import com.thebluealliance.androidclient.interfaces.RefreshListener;

import de.greenrobot.event.EventBus;

/**
 * Created by phil on 2/3/15.
 */
public class RecentNotificationsFragment extends Fragment implements RefreshListener {

    private PopulateRecentNotifications task;
    private Activity parent;

    private Parcelable mListState;
    private ListViewAdapter mAdapter;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parent = getActivity();
        if (parent instanceof RefreshableHostActivity) {
            ((RefreshableHostActivity) parent).registerRefreshListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_carded, null);
        mListView = (ListView) view.findViewById(R.id.list);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mListView.onRestoreInstanceState(mListState);
            progressBar.setVisibility(View.GONE);
        }
        return view;
    }

    private void startRefresh() {
        if (parent instanceof RefreshableHostActivity) {
            ((RefreshableHostActivity) parent).startRefresh(this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        startRefresh();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        if (task != null) {
            task.cancel(false);
        }
        if (mListView != null) {
            mAdapter = (ListViewAdapter) mListView.getAdapter();
            mListState = mListView.onSaveInstanceState();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((RefreshableHostActivity) parent).unregisterRefreshListener(this);
    }

    @Override
    public void onRefreshStart(boolean actionIconPressed) {
        Log.i(Constants.REFRESH_LOG, "Loading recent notifications info");
        task = new PopulateRecentNotifications(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onRefreshStop() {
        if (task != null) {
            task.cancel(false);
        }
    }

    public void setAdapter(ListViewAdapter adapter) {
        mAdapter = adapter;
    }

    public void updateTask(PopulateRecentNotifications task) {
        this.task = task;
    }

    public void onEventMainThread(NotificationsUpdatedEvent event) {
        startRefresh();
    }
}
