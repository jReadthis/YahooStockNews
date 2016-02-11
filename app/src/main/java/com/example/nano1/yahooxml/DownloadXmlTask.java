package com.example.nano1.yahooxml;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadXmlTask extends AsyncTask<String, Void, Void> {
    MainActivity context;
    ArrayList<Article> articles;
    ProgressDialog pDialog;

    DownloadXmlTask(MainActivity context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        articles = new ArrayList<>();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
           articles = loadFromNetwork(params[0]);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pDialog.dismiss();
        context.loadNews(articles);
    }

    public ArrayList<Article> loadFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XMLParser xmlParser = new XMLParser();
        try {
            stream = downloadUrl(urlString);
            return xmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}