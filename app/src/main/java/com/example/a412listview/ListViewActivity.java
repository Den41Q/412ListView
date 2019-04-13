package com.example.a412listview;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ListViewActivity extends AppCompatActivity {

    private SharedPreferences myListSraredPref;
    private List<Map<String, String>> simpleAdapterContent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        final ListView list = findViewById(R.id.list);
        final SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);
        setSupportActionBar(toolbar);

        myListSraredPref = getSharedPreferences("List", MODE_PRIVATE);
        prepareContent();

        final SimpleAdapter listContentAdapter = createAdapter(simpleAdapterContent);

        list.setAdapter(listContentAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                simpleAdapterContent.remove(position);
                listContentAdapter.notifyDataSetChanged();
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareContent();
                final SimpleAdapter listContentAdapter = createAdapter(simpleAdapterContent);
                list.setAdapter(listContentAdapter);
                swipeLayout.setRefreshing(false);
            }
        });


    }

    @NonNull
    private SimpleAdapter createAdapter(List<Map<String, String>> data) {
        return new SimpleAdapter(this, data, R.layout.simple_list_item,
                new String[]{"text_1", "text_2"}, new int[]{R.id.text_1, R.id.text_2});

    }

    @NonNull
    private void prepareContent() {
        if (!myListSraredPref.contains("myList")) {

            String[] arrayContent = getString(R.string.large_text).split("\n\n");

            String content = getString(R.string.large_text);
            SharedPreferences.Editor myEditor = myListSraredPref.edit();
            myEditor.putString("myList", content);
            myEditor.apply();

            for (String array : arrayContent) {
                Map<String, String> map = new HashMap<>();
                map.put("text_1", array);
                map.put("text_2", Integer.toString(array.length()));
                simpleAdapterContent.add(map);
            }
        } else {
            String content = myListSraredPref.getString("myList", "");
            String[] arrayContent = content.split("\n\n");

            for (String array : arrayContent) {
                Map<String, String> map = new HashMap<>();
                map.put("text_1", array);
                map.put("text_2", Integer.toString(array.length()));
                simpleAdapterContent.add(map);
            }
        }
    }
}