package controlserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;



// Handler for listening clients
// Keeps score on the amount of clients
// Accepts connection requests
public class clientController extends Thread{

    ControlServer controlServer;
    ServerSocket serverSocket;
    ArrayList<lightswitchServer> lights = new ArrayList<>();


    public clientController(ServerSocket serverSocket, ControlServer controlServer) {
        this.controlServer = controlServer;
        this.serverSocket = serverSocket;
    }


    public void listen() {

        int id;

        while(true) {

            try {
                id = lights.size()+1;
                lights.add(new lightswitchServer(serverSocket.accept(), controlServer, id));
                Thread thread = lights.get(id-1);
                thread.start();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    public void sendData(ControlServer.Mode mode, int id) {

        System.out.println("Lightswitch's id: " + id);

        // When attempt fails, delete it from list
        lights.get(id-1).sendData(mode);
    }

    public void run() {
        listen();
    }

}
