import java.awt.*;

/**
 * Created by Sorka on 10.12.2016.
 */
public class Drawer {
    //private static int ballSize = 16;

    public static void drawFirstRacket(Graphics g, int x, int y) {
        g.setColor(Color.red);
        g.fillRect(x, y, 10, 30);
    }

    public static void drawSecondRacket(Graphics g, int x, int y) {
        g.setColor(Color.cyan);
        g.fillRect(x, y, 10, 30);
    }
    public static void drawBall(Graphics g, int x, int y, int ballSize) {
        g.setColor(Color.magenta);
        g.fillOval(x-ballSize/2, y-ballSize/2, ballSize, ballSize);
    }
    public static void drawLine(Graphics g) {
        g.setColor(Color.gray);
        g.drawLine(350, 0, 350, 400);
    }
    public static void drawWall (Graphics g){
        g.setColor(Color.red);
        g.drawLine(0, 0, 700, 0);
        g.drawLine(0, 400, 700, 400);
        g.drawLine(0, 0, 0, 400);
        g.drawLine(700, 0, 700, 400);
    }
    public static void drawScore(Graphics g, int score1, int score2) {
        g.setColor(Color.pink);
        g.setFont(new Font("Arial", 1, 50));
        g.drawString(String.valueOf(score1), 350 - 70, 50);

        g.setColor(Color.pink);
        g.setFont(new Font("Arial", 1, 50));
        g.drawString(String.valueOf(score2), 350 + 40, 50);

    }
    public static void drawSatus(Graphics g, int status) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", 1, 50));
        g.drawString(String.valueOf(status), 350 - 15, 100);

    }

}
