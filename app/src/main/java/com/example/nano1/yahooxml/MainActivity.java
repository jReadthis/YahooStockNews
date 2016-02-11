package com.example.nano1.yahooxml;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView;
    ArrayList<Article> listData;
    MyAdapter myAdapter;
    boolean sortTitle = true;
    boolean sortDate = true;
    public static final String BASE_URL = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=aapl,yhoo,fb,goog,hbcp,mcd,msft,nflx,t&region=US&lang=en-US";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        DownloadXmlTask downloadXmlTask = new DownloadXmlTask(this);
        downloadXmlTask.execute(BASE_URL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.articles_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.sortTitle:
                if (sortTitle){
                    Collections.sort(listData, new Comparator<Article>() {
                        @Override
                        public int compare(Article lhs, Article rhs) {
                            if (lhs == null || rhs == null)
                                return 0;
                            return lhs.title.compareTo(rhs.title);
                        }
                    });
                    sortTitle = false;
                }
                else {
                    Collections.sort(listData, Collections.reverseOrder(new Comparator<Article>() {
                        @Override
                        public int compare(Article lhs, Article rhs) {
                            if (lhs == null || rhs == null)
                                return 0;
                            return lhs.title.compareTo(rhs.title);
                        }
                    }));
                    sortTitle = true;
                }
                break;
            case R.id.sortDate:
                if (sortDate){
                    Collections.sort(listData, new Comparator<Article>() {
                        @Override
                        public int compare(Article lhs, Article rhs) {
                            if (lhs == null || rhs == null)
                                return 0;
                            return lhs.pubDate.compareTo(rhs.pubDate);
                        }
                    });
                    sortDate = false;
                }else {
                    Collections.sort(listData, Collections.reverseOrder(new Comparator<Article>() {
                        @Override
                        public int compare(Article lhs, Article rhs) {
                            if (lhs == null || rhs == null)
                                return 0;
                            return lhs.pubDate.compareTo(rhs.pubDate);
                        }
                    }));
                    sortDate = true;
                }
                break;
        }
        myAdapter.notifyDataSetChanged();
        listView.invalidateViews();
        listView.refreshDrawableState();
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("article", listData.get(position));
        startActivity(intent);
    }

    public void loadNews(ArrayList<Article> articles){
        this.listData = articles;
        myAdapter = new MyAdapter(this, articles);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);
    }
}
