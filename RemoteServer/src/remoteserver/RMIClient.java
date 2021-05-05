package remoteserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class RMIClient {

    remoteInterface remoteInterface;


    public RMIClient() {
        // Security manager is needed. Remember policy file and VM parameter again.
        // Set VM parameter -Djava.security.policy=controlserver/src/server.policy to Control server and RemoteServer


        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // TODO: RMI client connection
    }


    // TODO: Create needed requests to control server

    public void startClient() throws NotBoundException, MalformedURLException, RemoteException {
        remoteInterface = (remoteInterface) Naming.lookup("rmi://localhost/rmiserver");

    }


    public void sendId(int id) {

        try {
            remoteInterface.executeTask(id);
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }


    public void sendTemperature(int temperature){
        try {
            System.out.print("Send temperature: " + temperature);
            remoteInterface.temperatureTask(temperature);
        }
        catch(RemoteException exception) {
            exception.printStackTrace();
        }
    }

}
