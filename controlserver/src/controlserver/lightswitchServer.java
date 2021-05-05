package controlserver;

import java.io.*;
import java.net.Socket;



public class lightswitchServer extends Thread {

    private Socket socket;
    ControlServer controlServer;
    InputStream inputStream;
    ObjectInputStream objectInputStream;
    OutputStream outputStream;
    ObjectOutputStream objectOutputStream;
    int id;



    public lightswitchServer(Socket socket, ControlServer controlServer, int id) {
        // constructor, create server here and bind it to IP & port

        this.socket = socket;
        this.controlServer = controlServer;
        this.id = id;
    }


    public void run() {
        // And here you should listen server socket

        while(true) {

            try {
                inputStream = socket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);

                int id = (int) objectInputStream.readObject();

                controlServer.toggleLightstatus(id);
            }
            catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }

    }


    // Send data to lightswitch
    public void sendData(ControlServer.Mode mode){

        try {
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println(mode.name());

            objectOutputStream.writeObject(mode.name());
            objectOutputStream.flush();
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }
    }
}
