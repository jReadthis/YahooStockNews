package com.example.nano1.yahooxml;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class XMLParser {
    private static final String ns = null;
    private static final String TAG_RSS = "rss";
    private static final String TAG_CHANNEL = "channel";
    private static final String TAG_ITEM = "item";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LINK = "link";
    private static final String TAG_DATE = "pubDate";

    public ArrayList<Article> parse(InputStream in) throws IOException, XmlPullParserException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList<Article> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<Article> articles = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, TAG_RSS);
        while (parser.next() != XmlPullParser.END_TAG) {
            parser.require(XmlPullParser.START_TAG, ns, TAG_CHANNEL);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals(TAG_ITEM)) {
                    articles.add(readEntry(parser));
                } else {
                    skip(parser);
                }
            }
        }
        return articles;
    }

    private Article readEntry(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, TAG_ITEM);
        String title = null;
        String link = null;
        Date pubDate = null;
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case TAG_TITLE:
                    title = readTitle(parser);
                    break;
                case TAG_LINK:
                    link = readLink(parser);
                    break;
                case TAG_DATE:
                    pubDate = readPubDate(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new Article(title,link,pubDate);
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG,ns, TAG_TITLE);
        return title;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_LINK);
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_LINK);
        return link;
    }

    private Date readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, TAG_DATE);
        String rawDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_DATE);

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, DD MMM yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date pubDate = null;
        try {
            pubDate = dateFormat.parse(rawDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pubDate;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT){
            result = parser.getText();
            parser.nextTag();
        }
        return  result;
    }

    private void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (parser.getEventType() != XmlPullParser.START_TAG){
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0){
            switch (parser.next()){
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
