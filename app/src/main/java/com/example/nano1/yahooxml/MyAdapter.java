package com.example.nano1.yahooxml;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by nano1 on 2/7/2016.
 */

public class MyAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Article> mArrayOfArticles;

    MyAdapter(Context c, ArrayList<Article> articles){
        this.mContext = c;
        this.mArrayOfArticles = articles;
    }

    class ViewHolder{
        TextView title, pubDate;

        ViewHolder(View row) {
        title = (TextView) row.findViewById(R.id.textView4);
        pubDate = (TextView) row.findViewById(R.id.textView6);
        }
    }

    @Override
    public int getCount() {
        return mArrayOfArticles.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayOfArticles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;
        if (row == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_row,parent,false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        Article mArticle = mArrayOfArticles.get(position);
        holder.title.setText(mArticle.title);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, DD MMM yyyy KK:mm a", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        String datetime = simpleDateFormat.format(mArticle.pubDate);
        holder.pubDate.setText(datetime);
        return row;
    }
}
