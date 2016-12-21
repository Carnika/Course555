/**
 * Created by Sorka on 09.12.2016.
 */

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 */
public class FromServer extends Thread {//принимает сообщения
    private String line;
    private DataInputStream in;
    private Socket socket;
    private Coordinator coordinator;
    Window window;
    public FromServer (Socket socket, Window window) {
        this.window = window;
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream()

            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        coordinator = new Coordinator();
    }
        public void run() {
            try {
                while (true) {

                    line = in.readUTF(); // ждем пока сервер отошлет строку текста


                    coordinator.coordinator(line, window);
                    //System.out.println(line);

                }
            } catch (IOException e) {
                System.out.println("crash");
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } finally {
                System.out.println("crash f");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


}

