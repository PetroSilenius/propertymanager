package controlserver.src;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;



public class lightswitchServer extends Thread {

    String IP;
    int port = 3000;
    ServerSocket serverSocket;

    public lightswitchServer(String IP, int port) {
        //constructor, create server here and bind it to IP & port

        this.IP = IP;
        this.port = port;

        while(true) {

            try {
            System.out.println("Waiting for client request");
            Socket socket = serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                // Read message from socket
                try {
                    String message = (String) objectInputStream.readObject();
                    System.out.println("Message received: " + message);
                }
                catch(ClassNotFoundException exception) {
                    exception.printStackTrace();
                }

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject("Hello client");
                objectOutputStream.close();
                objectOutputStream.close();
                socket.close();

            }
            catch(IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    public void run() {
        //And here you should listen to the server socket

        try {
            String address = "127.0.0.1";
            Socket socket = new Socket(address, 3000);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request...");

            // Read message from socket
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                String message = (String) objectInputStream.readObject();
                System.out.println("TestMessage: " + message);
            }
            catch(ClassNotFoundException exception) {
                exception.printStackTrace();
            }

        }
        catch(IOException exception) {
            exception.printStackTrace();
        }

    }
}
