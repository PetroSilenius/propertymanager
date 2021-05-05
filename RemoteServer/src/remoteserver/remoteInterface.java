package remoteserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface remoteInterface extends Remote {
     void executeTask(int id) throws RemoteException;
     void temperatureTask(int temperature) throws RemoteException;
}