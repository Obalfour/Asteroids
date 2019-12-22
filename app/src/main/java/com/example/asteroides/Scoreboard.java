package com.example.asteroides;

import java.util.List;

public interface Scoreboard {
    void saveScore(int score,String name,long date);
    List<String> scoreboard(int quantity);
}
