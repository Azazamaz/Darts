package home.lali.darts.model;

public class OnlineMatches {
    private String name1;
    private int legW_1;
    private double avg_1;
    private int score1;
    private String name2;
    private int legW_2;
    private double avg_2;
    private int score2;
    private boolean live;

    public OnlineMatches() {}

    /** Constructor for save local stat */
    public OnlineMatches(boolean live, DartsPlayer p1, DartsPlayer p2) {
        this.live = live;
        this.name1 = p1.getPlayerName();
        this.legW_1 = p1.getLegW();
        this.avg_1 = p1.getMatchAvg();
        this.name2 = p2.getPlayerName();
        this.legW_2 = p2.getLegW();
        this.avg_2 = p2.getMatchAvg();
    }

    /** Constructor for StartOnlineMatch */
    public OnlineMatches(DartsPlayer p1, DartsPlayer p2, boolean live) {
        this.name1 = p1.getPlayerName();
        this.legW_1 = p1.getLegW();
        this.score1 = p1.getScore();
        this.name2 = p2.getPlayerName();
        this.legW_2 = p2.getLegW();
        this.score2 = p2.getScore();
        this.live = live;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public int getLegW_1() {
        return legW_1;
    }

    public void setLegW_1(int legW_1) {
        this.legW_1 = legW_1;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public double getAvg_1() {
        return avg_1;
    }

    public void setAvg_1(double avg_1) {
        this.avg_1 = avg_1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getLegW_2() {
        return legW_2;
    }

    public void setLegW_2(int legW_2) {
        this.legW_2 = legW_2;
    }

    public double getAvg_2() {
        return avg_2;
    }

    public void setAvg_2(double avg_2) {
        this.avg_2 = avg_2;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
}
