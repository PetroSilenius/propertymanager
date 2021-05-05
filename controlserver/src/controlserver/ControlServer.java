package controlserver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;


public class ControlServer{
    //GUI variables, Do not edit
    private JTextField temperature;
    private JPanel mainPanel;
    private JLabel temperatureLabel;
    private JButton light7;
    private JButton light8;
    private JButton light9;
    private JButton light4;
    private JButton light1;
    private JButton light5;
    private JButton light2;
    private JButton light6;
    private JButton light3;
    //End of GUI variables

    public enum Mode {OFF, ON, NOTCONNECTED}
    private Mode[] lightstatus = new Mode[10];
    private ConcurrentHashMap<Integer, JButton> lights;
    ServerSocket serverSocket = null;
    clientController clientController;
    Socket socket;


    public ControlServer() {
        //constructor
            light1.addActionListener(new buttonAction());
            light2.addActionListener(new buttonAction());
            light3.addActionListener(new buttonAction());
            light4.addActionListener(new buttonAction());
            light5.addActionListener(new buttonAction());
            light6.addActionListener(new buttonAction());
            light7.addActionListener(new buttonAction());
            light8.addActionListener(new buttonAction());
            light9.addActionListener(new buttonAction());

            lights = new ConcurrentHashMap();
            lights.put(1, light1);
            lights.put(2, light2);
            lights.put(3, light3);
            lights.put(4, light4);
            lights.put(5, light5);
            lights.put(6, light6);
            lights.put(7, light7);
            lights.put(8, light8);
            lights.put(9, light9);

            startServers();
    }


    private class buttonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton tempB = (JButton)e.getSource();
            int ID = Integer.parseInt(tempB.getName());
            toggleLightstatus(ID);
        }

    }


    public void toggleLightstatus(int ID) {
        int arrayid = ID;
        if(lightstatus[arrayid] == Mode.ON) {
            lights.get(ID).setText("Light "+ ID +" OFF");
            lightstatus[arrayid] = Mode.OFF;
            sendLightStatus(ID, Mode.OFF);
        }
        else if (lightstatus[arrayid] == Mode.OFF) {
            lights.get(ID).setText("Light "+ ID +" ON");
            lightstatus[arrayid] = Mode.ON;
            sendLightStatus(ID, Mode.ON);
        }
        else {
            lights.get(ID).setText("Light "+ ID +" ON");
            lightstatus[arrayid] = Mode.ON;
            sendLightStatus(ID, Mode.ON);
        }
    }


    // Send LightStatus to client
    public void sendLightStatus(int ID, Mode input) {
        clientController.sendData(input, ID);
    }

    //Getter for Lightstatus
    public Mode getLightstatus(int ID) {
        return lightstatus[ID];
    }

    //Getter for temperature
    public String getTemperature() {
        return temperature.getText();
    }

    // Setter for temperature
    public void setTemperature(int newTemperature) {
        temperature.setText(Integer.toString(newTemperature));
        System.out.println(newTemperature);
    }


    private void startServers() {

        try {
            serverSocket = new ServerSocket(3001, 9, InetAddress.getByName("127.0.0.1"));
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }

        System.out.println("Servers started...");


        try {
            RMIServer rmi = new RMIServer(1099, this);
            rmi.startServer();
        }
        catch(RemoteException exception) {
            exception.printStackTrace();
        }

        // Controller for client listening
        clientController = new clientController(serverSocket, this);

        new Thread(clientController).start();

    }


    public static void main(String[] args) {
        //No need to edit main method, start your servers in  startServers() method

        JFrame frame = new JFrame("Controlserver");
        frame.setContentPane(new ControlServer().mainPanel);
        frame.setTitle("Controlserver");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}

