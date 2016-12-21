import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Sorka on 09.12.2016.
 */
public class GamePong extends Thread {
    private ClientServer cs1, cs2;

    private Integer fieldXMin, fieldYMin, fieldXMax, fieldYMax;

    private Integer status;//0 - play; 3,2,1 - pause 4 - конец игры

    private Integer countL, countR, ballance;//счет, счет баланса

    private Integer xBall, yBall, xBallNew, yBallNew;
    private Integer ballSize, ballSize2;
    private Integer speedBall;
    private Double alpha;

    private Integer speedRacket;
    private String line1;//строка считанная с клиента 2
    private String line2;//считанная с клиента 1
    private String line;//строка для отправки клиентам
    private String closePlay1, closePlay2;//

    //здесь держим левую и правую ракетку , что бы не переворачивать координаты
    //у клиента first and second ракетки!!!
    private Integer sizeRacketX, sizeRacketY;
    private Integer rightRacketX, rightRacketY;
    private Integer leftRacketX, leftRacketY;

    private Timer timer; //

    private Random random;

    public GamePong(ClientServer cs1, ClientServer cs2) {
        this.cs1 = cs1;
        this.cs2 = cs2;

        line1 = "";
        line2 = "";
        closePlay1 = "";
        closePlay2 = "";

        status = 3; //3-2-1

        fieldXMin = 20;
        fieldYMin = 0;
        fieldXMax = 680;
        fieldYMax = 400;

        ballSize = 10;
        ballSize2 = ballSize / 2;

        sizeRacketX = 10;
        sizeRacketY = 30;

        xBall = fieldXMin + (fieldXMax - fieldXMin) / 2;
        yBall = fieldYMin + (fieldYMax - fieldYMin) / 2;
        speedBall = 6;
        alpha = 0.0; //от 0 до Пи и от 0 до -Пи

        rightRacketX = fieldXMax;
        rightRacketY = (fieldYMax - fieldYMin)/ 2 - sizeRacketY / 2;
        leftRacketX = fieldXMin;
        leftRacketY = (fieldYMax - fieldYMin)/ 2 - sizeRacketY / 2;
        speedRacket = 1;

        countL = 0;
        countR = 0;
        ballance = 3;//11

        random = new Random();

        initTimer();
    }

    //таймер для вычисления координат мяча

    private void initTimer() {


        timer = new Timer(100, new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent e1) { // наша борода по вычислению соординат

                //сюда попадут или координаты или пусто  !!!

                //++++++++++++++++++   Вычисляем следующие координаты мяча ++++++++++++++
                calcilateCoordinationsBall();

                //++++++++++++++++++   Получаем строки с клиентов +++++++++++++++++++++++
                //line1 = cs1.getLineFromClient();
                if (!line1.isEmpty()) {
                    leftRacketY = Integer.parseInt(line1);
                }

                //line2 = cs2.getLineFromClient();
                if (!line2.isEmpty()) {
                    rightRacketY = Integer.parseInt(line2);
                }

                //++++++++++++++   Анализируем положение мяча у стенок   ++++++++++++++++

                if ((yBallNew - ballSize2) < fieldYMin) {//     Y min      нужно повернуть
                    yBallNew = yBallNew + (fieldYMin - (yBallNew - ballSize2));
                    alpha = -alpha;
                }
                if ((yBallNew + ballSize2) > fieldYMax) {//     Y max      нужно повернуть
                    yBallNew = yBallNew - ((yBallNew + ballSize2) - fieldYMax);
                    alpha = -alpha;
                }

                //++++++++++++++   Анализируем положение мяча у Right Racket   ++++++++++++++++
                if (xBallNew > fieldXMax) {//   Right Racket  нужно отразить с произвльным углом
                    int yRightR = (int) (yBall - (fieldXMax - xBall) * Math.tan(alpha));//"Минус" потому что тангенс!
                    //System.out.println(yRightR + "    " + ((yRightR >= rightRacketY) && (yRightR <= (rightRacketY + sizeRacketY))));
                    if ((yRightR >= rightRacketY) && (yRightR <= (rightRacketY + sizeRacketY))) {//попали ракеткой отражаем
                        rightNewAlpha();//
                        //xBallNew = (int) (fieldXMax - (yBallNew - yRightR) * (Math.sin(alpha)/Math.cos(alpha)));
                        xBallNew = fieldXMax;
                        yBallNew = yRightR;
                        //System.out.println("1xBall " + xBall + " yBall " + yBall + " xBallNew " + xBallNew + " yBallNew " + yBallNew + " rightRacketY " + rightRacketY + " yRightR " + yRightR  + " alpha " + alpha + " tn alpha " + Math.tan(alpha) + " sincos " + (Math.sin(alpha)/Math.cos(alpha)));


                        //yBallNew  не меняем


                    } else {// не попал!!!

                        //System.out.println("2xBall " + xBall + " yBall " + yBall + " xBallNew " + xBallNew + " yBallNew " + yBallNew + " rightRacketY " + rightRacketY + " yRightR " + yRightR  + " alpha " + alpha);

                        countL = countL + 1;
                        //rightNewAlpha();
                        xBallNew = fieldXMin + (fieldXMax - fieldXMin) / 2;
                        //xBallNew = fieldXMax;
                        yBallNew = fieldYMin + (fieldYMax - fieldYMin) / 2;
                        status = 3;



                        //отправляем счет, переустанавливаем шар
                        //try {
                            cs1.sendLineToClient("i count " + countL + " " + countR);
                            //join(500);
                            cs2.sendLineToClient("i count " + countL + " " + countR);
                            //join(500);
                        /*} catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }
                }//   Right Racket

                //++++++++++++++   Анализируем положение мяча у Left Racket   ++++++++++++++++
                if (xBallNew < fieldXMin) {//   Left Racket  нужно отразить с произвльным углом
                    int yLeftR = (int) (yBall + (xBall - fieldXMin) * Math.tan(alpha));//"Минус" потому что тангенс!

                    if ((yLeftR >= leftRacketY) && (yLeftR <= (leftRacketY + sizeRacketY))) {//попали ракеткой отражаем
                        leftNewAlpha();//
                        //xBallNew = (int) (fieldXMin - (yBallNew - yLeftR) * (Math.sin(alpha)/Math.cos(alpha)));
                        xBallNew = fieldXMin;
                        yBallNew = yLeftR;

                        //System.out.println("3xBall " + xBall + " yBall " + yBall + " xBallNew " + xBallNew + " yBallNew " + yBallNew + " leftRacketY " + leftRacketY + " yLeftR " + yLeftR  + " alpha " + alpha + " tn alpha " + Math.tan(alpha) + " sincos " + (Math.sin(alpha)/Math.cos(alpha)));
                        //yBallNew  не меняем
                    } else {// не попал!!!

                        //System.out.println("4xBall " + xBall + " yBall " + yBall + " xBallNew " + xBallNew + " yBallNew " + yBallNew + " leftRacketY " + leftRacketY + " yLeftR " + yLeftR  + " alpha " + alpha);

                        countR = countR + 1;
                        //leftNewAlpha();
                        xBallNew = fieldXMin + (fieldXMax - fieldXMin) / 2;
                        //xBallNew = fieldXMax;
                        yBallNew = fieldYMin + (fieldYMax - fieldYMin) / 2;
                        status = 3;


                        //System.out.println(xBallNew + "    " + yBallNew);

                        //отправляем счет, переустанавливаем шар
                        //try {
                            cs1.sendLineToClient("i count " + countL + " " + countR);
                            //join(500);
                            cs2.sendLineToClient("i count " + countL + " " + countR);
                            //join(500);
                        /*} catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }
                }//   Right Racket

                //++++++++++++++++++   Отправляем инф. строку игрокам   ++++++++++++++++++
                line = xBallNew + " " + yBallNew;
                //  System.out.println(xBall + " " + yBall + " " + rightRacketX + " " + rightRacketY + " " + leftRacketX + " " + leftRacketY);
                cs1.sendLineToClient(line + " " + rightRacketY);
                cs2.sendLineToClient(line + " " + leftRacketY);
                xBall = xBallNew;
                yBall = yBallNew;

            }//конец actionPerformed  с бородой


        });// от new Timmer

        timer.start();
    }






    public void run() {

        boolean status0sended = false;
        String playClose1 = "", playClose2 = ""; //ответы игроков после окончания игры

        try {
            //сообщаем игрокам кто есть кто ...
            cs1.sendLineToClient("i player 1");
//            join(500);
            cs2.sendLineToClient("i player 2");
            join(500);

            cs1.sendLineToClient("i firstR " + leftRacketX + " " + leftRacketY);//20 185
            join(500);
            cs1.sendLineToClient("i secondR " + rightRacketX + " " + rightRacketY);//680 185
            join(500);

            cs2.sendLineToClient("i firstR " + rightRacketX + " " + rightRacketY);//680 185
            join(500);
            cs2.sendLineToClient("i secondR " + leftRacketX + " " + leftRacketY);//20 185
            join(500);

            cs1.sendLineToClient("i ballSize " + ballSize);
            cs2.sendLineToClient("i ballSize " + ballSize);
            join(500);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        while (true) { // !!!!!!!!!!!!!!!!!!!!!!!  основной цикл  !!!!!!!!!!!!!!!!!!!!!!!!!!!

            //++++++++++++++++++++   Задержка   +++++++++++++++++++++++++++++++
            /*try {
                join(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/



            //++++++++++++++++++   4    3,2,1 Start  +++++++++++++++++++++++++++++++++++++
            try {join(1);


                if (cs1.getBLineFromClient()) {//есть, читаем данные 1
                    line1 = cs1.getLineFromClient();
                    cs1.setBLineFromClient(false);
                }

                if (cs2.getBLineFromClient()) {//есть, читаем данные 2
                    line2 = cs2.getLineFromClient();
                    cs2.setBLineFromClient(false);
                }


                if (cs1.getBClosePlay()) {//есть, читаем данные 1
                    closePlay1 = cs1.getClosePlay();
                    cs1.setBClosePlay(false);
                }

                if (cs2.getBClosePlay()) {//есть, читаем данные 2
                    closePlay2 = cs2.getClosePlay();
                    cs2.setBClosePlay(false);
                }


                if ((status == 4)) {//нужно получить ответ о новой игре или отказе

                   // System.out.println("line1 " + line1);

                    //line1 = cs1.getLineFromClient();
                    if (closePlay1.equals("play")) {System.out.println("closePlay1 " + closePlay1);}
                    if (!closePlay1.isEmpty()) {
                        if (closePlay1.equals("play")) {
                            playClose1 = "play";
                            //cs1.setLineToClient("i play");
                        }
                        if (closePlay1.equals("close")) {
                            playClose1 = "close";
                            break;
                        }
                    }

                    //line2 = cs2.getLineFromClient();
                    if (closePlay2.equals("play")) {System.out.println("closePlay2 " + closePlay2);}
                    //System.out.println("line2 " + line2);

                    if (!closePlay2.isEmpty()) {
                        if (closePlay2.equals("play")) {
                            playClose2 = "play";
                            //cs2.setLineToClient("i play");
                        }
                        if (closePlay2.equals("close")) {
                            playClose2 = "close";
                            break;
                        }
                    }

                    //System.out.println("playClose1 " + playClose1);
                   // System.out.println("playClose2 " + playClose2);
                    if ((playClose1.equals("play")) && ((playClose2.equals("play")))) {
                        //запускаем все с начала !!!
                        countL = 0;
                        countR = 0;
                        xBallNew = fieldXMin + (fieldXMax - fieldXMin) / 2;
                        yBallNew = fieldYMin + (fieldYMax - fieldYMin) / 2;
                        status = 3;
                        alpha = 0.0;
                        playClose1 = "";
                        playClose2 = "";
                    }

                    closePlay1 = "";//обнулим, чтоб не путались с координатами
                    closePlay2 = "";
                continue;
                }// end if status == 4








                if ((status == 3)) {//проверяем счет
                    timer.stop();


                    if ((Math.abs(countL - countR) >= 2) && (Math.max(countL, countR) >= ballance)) { // кто-то победил!
                        status = 4;
                        //отправим счет
                        cs1.sendLineToClient("i count " + countL + " " + countR);
                        cs2.sendLineToClient("i count " + countL + " " + countR);
                        //join(500);

                        //Определим победителя
                        if (countL > countR) {
                            cs1.sendLineToClient("i winer");
                            cs2.sendLineToClient("i loser");
                            //join(500);
                        } else {
                            cs1.sendLineToClient("i loser");
                            cs2.sendLineToClient("i winer");
                            //join(500);
                        }
                        continue;//
                    }// end if кто то победил
                }// end if status == 4

                if ((status > 0) && (status < 4)) {//отправляем статус 3, 2, 1
                    status0sended = false;
                    //отправим счет
                    cs1.sendLineToClient("i count " + countL + " " + countR);
                    cs2.sendLineToClient("i count " + countL + " " + countR);
                  //  join(500);
                    // отправим статус
                    cs1.sendLineToClient("i status " + status);
                    cs2.sendLineToClient("i status " + status);
                  //  join(500);
                    status = status - 1;
                    continue;
                } else if (status0sended == false){//отправляем статус 0
                    status0sended = true;
                    cs1.sendLineToClient("i status " + status);
                    cs2.sendLineToClient("i status " + status);
                    //join(500);
                    timer.start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }









        }
    }// конец бороды

    private void calcilateCoordinationsBall() {
        xBallNew = (int) (xBall + speedBall * Math.cos(alpha));
        yBallNew = (int) (yBall + speedBall * Math.sin(alpha));
        if ((xBallNew < fieldXMin) || (xBallNew > fieldXMax) || (yBallNew < fieldYMin) ||(yBallNew > fieldYMax) ) {
            System.out.println("bred xBall " + xBall + " yBall " + yBall + " xBallNew " + xBallNew + "   yBallNew " + yBallNew + "   alpha " + alpha);
        }
    }

    private void rightNewAlpha() {
        double newAlpha = (random.nextInt(150) - 85);
        if (newAlpha > 0) newAlpha = newAlpha + 90;
        if (newAlpha == 0) newAlpha = newAlpha + 180;
        if (newAlpha < 0) newAlpha = newAlpha - 90;
        alpha = Math.toRadians(newAlpha);
    }

    private void leftNewAlpha() {
        double newAlpha = (random.nextInt(150) - 85);
        alpha = Math.toRadians(newAlpha);
    }


}
