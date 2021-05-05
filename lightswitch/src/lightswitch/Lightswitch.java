package lightswitch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Lightswitch {
    private JPanel mainPanel;
    private JButton switchbutton;
    private JLabel statuslabel;
    private Mode lightstatus;
    private enum Mode {OFF, ON, NOTCONNECTED}
    int ID;

    private Socket socket;
    InputStream inputStream;
    ObjectInputStream objectInputStream;
    OutputStream outputStream;
    ObjectOutputStream objectOutputStream;


    public Lightswitch(int ID) {
        this.ID = ID;

        switchbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChange(ID);
            }
        });

        System.out.println("Connecting to server...");


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                // Listening to server socket
                connectSwitch(ID);
                while(true) {
                    try {
                        inputStream = socket.getInputStream();
                        objectInputStream = new ObjectInputStream(inputStream);
                        String id = (String) objectInputStream.readObject();

                        Mode mode = Mode.valueOf(id);
                        System.out.println(mode);
                        setLightStatus(mode);
                    }
                    catch (IOException | ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                }
                }

        });

        thread.start();
    }


    protected void connectSwitch(int ID) {
        //TODO: Create an socket connection connection to server

        while(true) {
            try {
                socket = new Socket(InetAddress.getByName("127.0.0.1"), 3001);
                sendChange(ID);

                System.out.println("Listening to socket...");
                break;
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    protected void sendChange(int ID) {
        //TODO: Send lightswitch action pressed to server
        if (socket != null) {
            try {

                outputStream = socket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);

                objectOutputStream.writeObject(ID);
                objectOutputStream.flush();

            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    protected void receiveStatus() {
        //Setting default option to not connected
        Mode receivedMode = Mode.NOTCONNECTED;
        int id = -1;

        //TODO: receive status of the light from the server

        try {
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            while(true) {
                receivedMode = (Mode) objectInputStream.readObject();
                id = (int) objectInputStream.readObject();
                System.out.println(receivedMode);
            }
        }
        catch(IOException | ClassNotFoundException exception){
            exception.printStackTrace();
        }

        //Update view to received status state
        if(this.ID == id) {
            setLightStatus(receivedMode);
        }
    }



// Sets light status
// Different modes are ON, OFF, NOTCONNECTED
    public void setLightStatus(Mode input) {

            if (input == Mode.ON) {

                lightstatus = Mode.ON;
                statuslabel.setText("Lights on.");
                statuslabel.setBackground(Color.green);
            }
            else if (input == Mode.OFF){

                lightstatus = Mode.OFF;
                statuslabel.setText("Lights off.");
                statuslabel.setBackground(Color.red);
            }
            else if (input == Mode.NOTCONNECTED){

                lightstatus = Mode.NOTCONNECTED;
                statuslabel.setText("Not connected.");
                statuslabel.setBackground(Color.yellow);
            }
    }


    public static void main(String[] args) {
        //No need to edit main.
        //ID number is read from th first command line parameter

            // Create 9 light switches with id from 1 to 9
            for(int i = 1; i < 10; i++) {
                JFrame frame = new JFrame("Lightswitch");
                frame.setContentPane(new Lightswitch(i).mainPanel);
                frame.setTitle("Lightswitch" + i);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
    }
}
