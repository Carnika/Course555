/**
 * Created by Sorka on 11.12.2016.
 */
public class Model {
    public static SecondRacket sr;
    public static FirstRacket fr;
    public static Ball boll;
    public boolean flagFirstRacket;

    public Model(SecondRacket sr, FirstRacket fr, Ball boll) {
        this.sr = sr;
        this.fr = fr;
        this.boll = boll;
    }
    public void setCoord(){
        ////////
        sr.setX(680);
        sr.setY(200);

        fr.setX(10);
        fr.setY(200);

        boll.setX(350);
        boll.setY(200);
        /////////
    }
    public static boolean isInFirstRacket(int x, int y){
        if(y >= fr.getY()&& y <= fr.getY() + 30
                && x >= fr.getX() && x <= fr.getX() + 10){
            //System.out.println("true Left");
            return true;
        }
        return false;
    }
    public void setFlagFirstRacket(boolean flag){
        this.flagFirstRacket = flag;
    }
    public boolean getFlagFirstRacket(){
        return this.flagFirstRacket;
    }
}
