/**
 * Created by Sorka on 10.12.2016.
 */
public class Coordinator {

    public void coordinator(String line, Window window){
        String[] lineArr = line.trim().split("\\s+");
        //System.out.println(lineArr[0]+ "," + lineArr[1]);
        if (lineArr[0].equals("i")) {
            System.out.println(lineArr[0]+ "," + lineArr[1]);//+ "," + lineArr[2]);
            switch ( lineArr[1] )
            {
                case "player":
                    window.getTablePanel().setPlayer(Integer.parseInt(lineArr[2]));
                    //организовать вывод заставки в зависимости от игрока!!!!
                    break;
                case "ballSize":
                    window.getTablePanel().ball.setSize(Integer.parseInt(lineArr[2]));
                    break;
                case "firstR":
                    //System.out.println(lineArr[0]+ ",..." + lineArr[1]+ "," + lineArr[2]+ "," + lineArr[3]);
                    if (window.getTablePanel().getPlayer() == 1) {
                        window.getTablePanel().firstRacket.setX(Integer.parseInt(lineArr[2]) - 10);
                        window.getTablePanel().firstRacket.setY(Integer.parseInt(lineArr[3]));
                    }
                    else {
                        window.getTablePanel().firstRacket.setX(Integer.parseInt(lineArr[2]));
                        window.getTablePanel().firstRacket.setY(Integer.parseInt(lineArr[3]));
                    }
                    //System.out.println("firstRacket " + window.getTablePanel().firstRacket.getX()+ "," + window.getTablePanel().firstRacket.getY());
                    break;
                case "secondR":
                    //System.out.println(lineArr[0]+ ",,,," + lineArr[1]+ "," + lineArr[2]+ "," + lineArr[3]);
                    if (window.getTablePanel().getPlayer() == 2) {
                        window.getTablePanel().secondRacket.setX(Integer.parseInt(lineArr[2]) - 10);
                        window.getTablePanel().secondRacket.setY(Integer.parseInt(lineArr[3]));
                    } else {
                        window.getTablePanel().secondRacket.setX(Integer.parseInt(lineArr[2]));
                        window.getTablePanel().secondRacket.setY(Integer.parseInt(lineArr[3]));
                    }
                    //System.out.println("secondRacket " + window.getTablePanel().secondRacket.getX()+ "," + window.getTablePanel().secondRacket.getY());
                    break;
                case "count":
                    window.getTablePanel().state.setScore1(Integer.parseInt(lineArr[2]));
                    window.getTablePanel().state.setScore2(Integer.parseInt(lineArr[3]));
                    break;
                case "status":
                    window.getTablePanel().state.setStatus(Integer.parseInt(lineArr[2]));
                    break;
                case "winer":
                case "loser":
                    window.getTablePanel().state.setWinLos(lineArr[1]);
                    System.out.println("WinLos " + window.getTablePanel().state.getWinLos());
                    break;
                default:
            }
            //window.getTablePanel().repaint();

        }


        else {
            //System.out.println(lineArr[0]+ " " + lineArr[1]+ " " + lineArr[2]);
            window.getTablePanel().ball.setX(Integer.parseInt(lineArr[0]));
            window.getTablePanel().ball.setY(Integer.parseInt(lineArr[1]));
            window.getTablePanel().secondRacket.setY(Integer.parseInt(lineArr[2]));

        }
    }
}
