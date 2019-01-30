package home.lali.darts;

class DartsPlayer {
    private String playerName;
    private int score;
    private int legW;
    private int lastScore;
    private double legAvg;
    private double matchAvg;
    private boolean legStart;

    DartsPlayer(){
        playerName = "Darts Player";
        score = 0;
        legW = 0;
        legAvg = 0;
        matchAvg = 0;
        lastScore = 0;
        legStart = false;
    }

    String getPlayerName(){
        return playerName;
    }

    void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    int getScore(){
        return score;
    }

    void setScore(int score){
        this.score = score;
    }

    int getLegW () {
        return legW;
    }

    void setLegW(int legW){
        this.legW = legW;
    }

    double getLegAvg(){
        return legAvg;
    }

    void setLegAvg(double legAvg){
        this.legAvg = legAvg;
    }

    double getMatchAvg(){
        return matchAvg;
    }

    void setMatchAvg(double matchAvg){
        this.matchAvg = matchAvg;
    }

    int getLastScore(){
        return lastScore;
    }

    void setLastScore(int lastScore){
        this.lastScore = lastScore;
    }

    boolean getLegStart() {
        return legStart;
    }

    void setLegStart(boolean legStart){
        this.legStart = legStart;
    }
}