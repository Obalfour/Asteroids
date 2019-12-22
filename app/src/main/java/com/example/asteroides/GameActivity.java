package com.example.asteroides;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {

    private GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        gameView = (GameView) findViewById(R.id.gameview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.getThread().pause();
    }

    @Override protected void onResume() {
        super.onResume();
        gameView.getThread().resumeGame();
    }

    @Override protected void onDestroy() {
        gameView.getThread().stopGame();
        super.onDestroy();
    }


}
