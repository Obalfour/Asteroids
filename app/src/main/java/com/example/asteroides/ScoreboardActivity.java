package com.example.asteroides;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ScoreboardActivity extends ListActivity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);
        setListAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                MainActivity.scores.scoreboard(10)));
    }
}