package remoteserver;

import java.net.InetSocketAddress;


public class RemoteServer {

    InetSocketAddress socketAddress;

    public RemoteServer() {
        // TODO: create and start your servers, make connection needed


        socketAddress = new InetSocketAddress(3002);
        WWWServer wwwServer = new WWWServer(socketAddress, "/status");


        System.out.println("HTTP Server running on port " + 3002);

    }

    public static void main(String[] args) {
        new RemoteServer();
    }
}
