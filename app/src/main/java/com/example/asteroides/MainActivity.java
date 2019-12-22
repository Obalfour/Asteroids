package com.example.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    public static Scoreboard scores = new ScoreboardList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startAbout(View view) {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    public void startGame(View view) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    public void startPreferences(View view) {
        Intent i = new Intent(this, Preferences.class);
        startActivity(i);
    }

    public void startScoreboard(View view) {
        Intent i = new Intent(this, ScoreboardActivity.class);
        startActivity(i);
    }

}
