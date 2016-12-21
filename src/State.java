/**
 * Created by Sorka on 19.12.2016.
 */
public class State {

    private int score1, score2;
    private int status;
    private String winLos = "";

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setWinLos(String winLos) {
        this.winLos = winLos;
    }

    public String getWinLos() {
        return this.winLos;
    }

    public int getStatus() {
        return this.status;
    }

    public int getScore1() {
        return this.score1;
    }

    public int getScore2() {
        return this.score2;
    }

}

