package com.example.nano1.yahooxml;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;


/**
 * Created by nano1 on 2/7/2016.
 */
public class Article implements Parcelable{
    String title;
    String link;
    Date pubDate;

    public Article(String title, String link, Date pubDate) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
    }

    protected Article(Parcel in) {
        title = in.readString();
        link = in.readString();
        long tmpPostDate = in.readLong();
        pubDate = tmpPostDate != -1? new Date(tmpPostDate) : null;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
        dest.writeLong(pubDate != null ? pubDate.getTime() : -1L);
    }
}
