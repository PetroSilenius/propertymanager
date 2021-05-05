package controlserver;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;



public class RMIServer extends UnicastRemoteObject implements remoteserver.remoteInterface {

    controlserver.ControlServer controlServer;

    public RMIServer(int RMIPort, controlserver.ControlServer controlServer) throws RemoteException {

        super(RMIPort);
        this.controlServer = controlServer;

        // Constructor
        // You will need Security manager to make RMI work
        // Remember to add security.policy to your run time VM options
        // -Djava.security.policy=[YOUR PATH HERE]\server.policy
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

    }

        public void startServer() throws RemoteException {
            LocateRegistry.createRegistry(1099);

            try {
                Naming.bind("rmiserver", this);
            }
            catch (MalformedURLException | AlreadyBoundException exception) {
                exception.printStackTrace();
            }
        }


        // Toggle lights status
        public void executeTask(int input) {
            controlServer.toggleLightstatus(input);
        }

        // Set temperature
        public void temperatureTask(int temperature) {
            System.out.println("TemperatureTask: " + temperature);
            controlServer.setTemperature(temperature);
        }
}
