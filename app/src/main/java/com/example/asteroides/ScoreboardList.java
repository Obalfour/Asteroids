package com.example.asteroides;

import java.util.LinkedList;
import java.util.List;

public class ScoreboardList implements Scoreboard{
    private List<String> scores;

    public ScoreboardList() {
        scores= new LinkedList<>();
        scores.add("123000 Pepito Domingez");
        scores.add("111000 Pedro Martinez");
        scores.add("011000 Paco Pérez");
    }

    @Override
    public void saveScore(int score, String name, long date) {
        scores.add(0, score + " "+ name);
    }

    @Override
    public List<String> scoreboard(int quantity) {
        return scores;
    }

}