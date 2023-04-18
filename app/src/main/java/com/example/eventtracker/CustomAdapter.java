package com.example.eventtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<EventList> eventLists;

    public CustomAdapter(Context context, ArrayList<EventList> eventLists) {
        this.context = context;
        this.eventLists = eventLists;
    }

    @Override
    public int getCount() {
        return eventLists.size();
    }

    @Override
    public Object getItem(int position) {
        return eventLists.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HolderView holderView;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_layout_with_cardview,
                    parent,false);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        }

        else {
            holderView = (HolderView) convertView.getTag();
        }

        EventList list = eventLists.get(position);
//        holderView.iconList.setImageResource(list.getSocialMediaIcon());
        holderView.eventTitle.setText(list.getEventTitle());
        holderView.eventTDesc.setText(list.getEventDesc());

        return convertView;
    }

    private static class HolderView{
//        private final ImageView iconList;
        private final TextView eventTitle;
        private final TextView eventTDesc;

        public HolderView(View view){
//            iconList = view.findViewById(R.id.social_media_icon);
            eventTitle = view.findViewById(R.id.list_event_name);
            eventTDesc = view.findViewById(R.id.list_event_desc);
        }
    }

}
