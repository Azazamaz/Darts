package home.lali.darts;

/**
 * Created by Lali on 2018. 03. 26..
 */

public class DartsPlayer {
    private String playerName;
    private int score;
    private int legW;
    private int setW;
    private int lastScore;
    private double avg3dart;
    private double legAvg;
    private double matchAvg;

    public DartsPlayer(){
        playerName = "Darts Player";
        score = 0;
        legW = 0;
        setW = 0;
        avg3dart = 0;
        legAvg = 0;
        matchAvg = 0;
        lastScore = 0;
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

    public int getSetW(){
        return setW;
    }

    public void setSetW(int setW){
        this.setW = setW;
    }

    public double getAvg3dart(){
        return avg3dart;
    }

    public void setAvg3dart(double avg3){
        this.avg3dart = avg3;
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
}