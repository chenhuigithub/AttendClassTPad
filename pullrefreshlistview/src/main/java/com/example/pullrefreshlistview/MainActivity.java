package com.example.pullrefreshlistview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ArrayAdapter;

import com.example.pullrefreshlistview.PullRefreshListView.OnRefreshListener;

import java.util.Arrays;
import java.util.LinkedList;

public class MainActivity extends Activity {

    private int newData = 21;
    private LinkedList<String> mListItems;
    private PullRefreshListView listView;
    public String[] mStrings = { "1","2","3","4","5"
            ,"6","7","8","9","10"
            ,"11","12","13","14","15"
            ,"16","17","18","19","20"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_library_main02);

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListItems);
        listView = (PullRefreshListView) findViewById(R.id.list);
        listView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                new RefreshNetWork().execute();
            }

            @Override
            public void onLoadMore() {
                new LoadMoreNetWork().execute();
            }
        });
        listView.setAdapter(adapter);

    }

    private class RefreshNetWork extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SystemClock.sleep(3000);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            listView.refreshComplete();
        }

    }

    private class LoadMoreNetWork extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SystemClock.sleep(3000);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            mListItems.addLast( (newData++) + "");
            listView.loadMoreComplete();
            super.onPostExecute(result);
        }

    }
}
