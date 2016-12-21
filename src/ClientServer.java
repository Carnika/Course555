import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Sorka on 09.12.2016.
 */
public class ClientServer extends Thread {
    private Socket socket;
    private String lineFromClient, closePlay;
    private Boolean bLineFromClient, bClosePlay;
    //private String lineToClient;
    private String myName;
    public DataInputStream in;
    public DataOutputStream out;
    //private Integer secondtRacket;
    //private Integer firstRacket;
    public ClientServer(Socket s) {
        socket = s;
        //lineToClient = "";
        lineFromClient = "";
        setBClosePlay(false);
        setBLineFromClient(false);
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try {//сначала проверить логин
            while (true) {
                myName = in.readUTF();
                break;
            }
            while (true) {
                //получаем от клиента строку
                //if (!bLineFromClient) {
                    lineFromClient = in.readUTF();
                //}



                if (lineFromClient.equals("play")) {
                    closePlay = "play";
                    setBClosePlay(true);
                    lineFromClient = "";
                    System.out.println("lineFromClient " + closePlay);
                }
                if (lineFromClient.equals("close")) {
                    closePlay = "close";
                    setBClosePlay(true);
                    lineFromClient = "";
                    System.out.println("lineFromClient " + closePlay);
                }


                if (!(lineFromClient.isEmpty())) {
                    setBLineFromClient(true);
                }

                //String[] lineArr = lineFromClient.trim().split("\\s+");
               // secondtRacket = Integer.parseInt(lineArr[0]);
               // firstRacket = Integer.parseInt(lineArr[0]);
                System.out.println("lineFromC" + lineFromClient);
                //System.out.println((!(lineToClient.isEmpty())));

                /*if (!(lineToClient.isEmpty())){
                    out.writeUTF(lineToClient);
                    out.flush();
                    //System.out.println(lineToClient);
                    lineToClient = "";
                }*/
            }
        } catch (IOException e) {//socket closed
        }
    }
    /*
    public Integer getLeftRacket(){
        return firstRacket;
    }
    public Integer getRightRacket(){
        return secondtRacket;
    }*/
    public void sendLineToClient(String lineToClient) {
        try {
            out.writeUTF(lineToClient);
            out.flush();
        } catch (IOException e) {//socket closed
        }
    }


    public Boolean getBLineFromClient(){
        return bLineFromClient;
    }
    public Boolean getBClosePlay(){
        return bClosePlay;
    }
    public void setBLineFromClient(Boolean bLineFromClient){
        this.bLineFromClient = bLineFromClient;
    }

    public void setBClosePlay(Boolean bClosePlay){
        this.bClosePlay = bClosePlay;
    }
    /*public void setLineToClient(String lineToClient){
        this.lineToClient = lineToClient;
    }*/
    public String getLineFromClient(){
        return lineFromClient;
    }

    public String getClosePlay(){
        return closePlay;
    }
}



