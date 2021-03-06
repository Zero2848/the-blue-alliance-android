package com.thebluealliance.androidclient.listitems;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.thebluealliance.androidclient.R;
import com.thebluealliance.androidclient.helpers.WebcastHelper;

/**
 * Created by phil on 3/27/15.
 */
public class WebcastListElement extends ListElement {

    private String eventKey;
    private String eventName;
    private JsonObject webcast;
    private int number;

    public WebcastListElement(String eventKey, String eventName, JsonObject webcast, int number) {
        super();
        this.eventKey = eventKey;
        this.eventName = eventName;
        this.webcast = webcast;
        this.number = number;
    }

    @Override
    public View getView(final Context c, LayoutInflater inflater, View convertView) {
        ViewHolder holder;

        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = inflater.inflate(R.layout.list_item_carded_webcast, null);

            holder = new ViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.label);
            holder.value = (TextView) convertView.findViewById(R.id.value);
            holder.container = (LinearLayout) convertView.findViewById(R.id.summary_container);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.label.setText(String.format(c.getString(R.string.webcast_event_format), eventName, number));
        final String service = webcast.get("type").getAsString();
        final WebcastHelper.TYPE type = WebcastHelper.getType(service);
        if (holder.container.getChildCount() > 2) {
            holder.container.removeViewAt(2);
        }
        if (service != null) {
            holder.value.setVisibility(View.VISIBLE);
            holder.value.setText(type.render(c));
            holder.value.setTypeface(null, Typeface.NORMAL);
            holder.container.setOnClickListener(v -> c.startActivity(WebcastHelper.getIntentForWebcast(c, eventKey, type, webcast, number)));
        } else {
            holder.value.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView label;
        TextView value;
        LinearLayout container;
    }
}
