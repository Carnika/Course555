/**
 * Created by Sorka on 09.12.2016.
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends Thread{
    private static ConcurrentHashMap<String, Socket>  clients = new ConcurrentHashMap<>();
    private static ArrayList<String> names = new ArrayList<>();
    private ServerSocket ss;
    private ArrayList<ClientServer> clientServerArr = new ArrayList<ClientServer>();
    private GamePong gamePong;
    public Server(int port) throws IOException {
        ss = new ServerSocket(port);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("I'm working...");
        int port = Integer.parseInt(args[0]);
        new Server(port).start();
    }

    public void run(){
        while(true){
            try {
                if(clientServerArr.size() < 2) {
                    Socket socket = ss.accept();    //подсоединение
                    System.out.println("New client");
                    ClientServer client = new ClientServer(socket);
                    client.start(); //запускаем
                    clientServerArr.add(client);
                }
                if(clientServerArr.size() == 2) {
                    gamePong = new GamePong(clientServerArr.get(0), clientServerArr.get(1));
                    gamePong.start();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


}