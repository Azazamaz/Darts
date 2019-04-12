package home.lali.darts.model;

public class DartsPlayer {
    private String playerName;
    private int score;
    private int legW;
    private int lastScore;
    private double legAvg;
    private double matchAvg;
    private boolean legStart;

    public DartsPlayer(){
        playerName = "Darts Player";
        score = 0;
        legW = 0;
        legAvg = 0;
        matchAvg = 0;
        lastScore = 0;
        legStart = false;
    }

    public DartsPlayer(String name, int score) {
        this.playerName = name;
        this.score = score;
        this.legW = 0;
    }

    public DartsPlayer(String name, int legW, double matchAvg) {
        this.playerName = name;
        this.legW = legW;
        this.matchAvg = matchAvg;
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getLegW () {
        return legW;
    }

    public void setLegW(int legW){
        this.legW = legW;
    }

    public double getLegAvg(){
        return legAvg;
    }

    public void setLegAvg(double legAvg){
        this.legAvg = legAvg;
    }

    public double getMatchAvg(){
        return matchAvg;
    }

    public void setMatchAvg(double matchAvg){
        this.matchAvg = matchAvg;
    }

    public int getLastScore(){
        return lastScore;
    }

    public void setLastScore(int lastScore){
        this.lastScore = lastScore;
    }

    public boolean getLegStart() {
        return legStart;
    }

    public void setLegStart(boolean legStart){
        this.legStart = legStart;
    }
}