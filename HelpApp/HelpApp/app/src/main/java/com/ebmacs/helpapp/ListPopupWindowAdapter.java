package com.ebmacs.helpapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

public class ListPopupWindowAdapter extends BaseAdapter {
    LayoutInflater mLayoutInflater;
    List<String> mItemList;
    Context context;

    public ListPopupWindowAdapter(Context context, List<String> itemList) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemList = itemList;
        context=context;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return mItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.popup_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(mItemList.get(position));


        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;

        ViewHolder(View view) {
            tvTitle = view.findViewById(R.id.text);
        }
    }
}