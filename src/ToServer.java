/**
 * Created by Sorka on 09.12.2016.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 */
public class ToServer extends Thread {
    private String line, lineSend;
    private Window window;
    private DataOutputStream out;
    private Socket socket;

    public ToServer(Socket socket, Window window) {
        this.line = "";
        this.lineSend = "";
        this.window = window;
        this.socket = socket;
        try {
            out = new DataOutputStream(socket.getOutputStream()

            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void run() {//отправляет на сервер
            try {
                while (true) {
                    //System.out.println("enter your name: ");
                    String line = "Connection";//keyboard.readLine();
                    out.writeUTF(line);
                    out.flush();

                    break;
                }
            } catch (IOException e) {
                System.out.println("crash");
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            try {
                while (true) {//-------------    Основной цикл
                    if (socket.isClosed()) {
                        break;
                    }
                    //if (!flagLine) continue;
                    //flagLine = false;
                   // System.out.println(window.getTablePanel().getLineToServer());
                   // out.writeUTF(window.getTablePanel().getLineToServer()); // отсылаем серверу
                    //line = window.getTablePanel().firstRacket.getYString();//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
                    line = window.getTablePanel().getLineToServer();
                    if (!(line.equals(lineSend))) {//
                        //System.out.println("line " + line);
                        if (line.equals("play")) {System.out.println("line " + line);}
                        out.writeUTF(line); // отсылаем серверу
                        //System.out.println("line " + line);
                        // System.out.println(window.getTablePanel().firstRacket.getYString());
                        out.flush();
                        lineSend = line;
                        //window.getTablePanel().setLineToServer(""); // обнуляем строку
                    }
                }
            } catch (IOException e) {
                System.out.println("crash");
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

        }
    public void setLine(String line){
        this.line = line;
    }
    /*public void setFlagLine(boolean flagLine){
        this.flagLine = flagLine;
    }*/
}

