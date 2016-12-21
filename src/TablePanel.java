import javax.swing.*;
import java.awt.*;
import java.awt.Window;
import java.awt.event.*;

/**
 * Created by Sorka on 10.12.2016.
 */
public class TablePanel extends JPanel {

    private int player;

    //private int status = 3;// 3-2-1
    public Ball ball = new Ball();
    public State state = new State();
    public SecondRacket secondRacket = new SecondRacket();
    public FirstRacket firstRacket = new FirstRacket();//left


    private Window window;
    private String lineToServer = "";
    Model model = new Model(secondRacket, firstRacket, ball);

    public TablePanel(Window window) {
        this.addMouseListener(new MyMouse());
        this.addMouseMotionListener(new MyMouse());
        this.window = window;
        model.setCoord();
    }
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Drawer.drawLine(g);
        Drawer.drawScore(g, state.getScore1(), state.getScore2());
        Drawer.drawBall(g, ball.getX(), ball.getY(), ball.getSize());
        //System.out.print("FR " + firstRacket.getX()+ "," + firstRacket.getY());
        //System.out.println("   SR " + secondRacket.getX()+ "," + secondRacket.getY());
        Drawer.drawFirstRacket(g, firstRacket.getX(), firstRacket.getY());
        Drawer.drawSecondRacket(g, secondRacket.getX(), secondRacket.getY());
        Drawer.drawWall(g);
        if (state.getStatus() > 0) {
            Drawer.drawSatus(g, state.getStatus());
        }
        repaint();
    }
    public void setPlayer(int player) {
        this.player = player;
    }
    public int getPlayer() {
        return this.player;
    }

    public String getLineToServer() {
        String lineToServer = "";
        String message = "";
        //System.out.println("1");
        if (!(state.getWinLos().isEmpty())) {
            if (state.getWinLos().equals("winer")) {
                message = "Win!!! Another?";
            }
            if (state.getWinLos().equals("loser")) {
                message = "Loose.  Another?";
            }
            //lineToServer = "play";
            /*
            System.out.println("state.getWinLos() " + state.getWinLos() + " " + (state.getWinLos().isEmpty()) + " message " + message);
            JFrame frame = new JFrame();*/


            int answer = JOptionPane.showConfirmDialog(this,
                    message,
                    "",  // TITLE_confirm
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            //int answer = JOptionPane.showConfirmDialog(frame, message);
            if (answer == JOptionPane.YES_OPTION) {
                // User clicked YES.
                lineToServer = "play";
            } else if (answer == JOptionPane.NO_OPTION) {
                // User clicked NO.
                lineToServer = "close";
            }
            state.setWinLos("");//чтоб больше не справшивать
            System.out.println("lineToServer " + lineToServer);
            return lineToServer;
        } else {
            return this.firstRacket.getYString();
        }


    }

    public class MyMouse implements MouseListener, MouseMotionListener {
        private int mouseX;
        private int mouseY;
        private int dy;


        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();

            if (Model.isInFirstRacket(mouseX, mouseY)) {
                dy = mouseY - firstRacket.getY();
                model.setFlagFirstRacket(true);
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            model.setFlagFirstRacket(false);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(model.getFlagFirstRacket()){
                int newY;
                newY = e.getY();
                firstRacket.setY(newY - dy);
                repaint();
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            dy = mouseY - firstRacket.getY();
        }
    }

}
