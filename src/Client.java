/**
 * Created by Sorka on 09.12.2016.
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Адреса и порты задаются через командную строку:
 * клиенту --- куда соединяться, серверу --- на каком порту слушать.
 * Написать текстовый многопользовательский чат.
 * Пользователь управляет клиентом. На сервере пользователя нет.
 * Сервер занимается пересылкой сообщений между клиентами
 * По умолчанию сообщение посылается всем участникам чата
 * Есть команда послать сообщение конкретное пользователи (@senduser Vasya)
 * Программа работает по протоколу TCP.
 * <p>
 */
//java Client port(0) ipAddr(1)
public class Client {

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);// порт, к которому привязывается сервер
        InetAddress ipAddress = InetAddress.getByName(args[1]); // создаем объект который отображает вышеописанный IP-адрес
        Socket socket = new Socket(ipAddress, port);

        Window window = new Window();

        ToServer toServer = new ToServer(socket, window);
        toServer.start();

        FromServer fromServer = new FromServer(socket, window);
        fromServer.start();



        //
    }





}

