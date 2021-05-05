package remoteserver;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.parser.*;
import org.json.simple.*;


public class WWWServer {

    private HttpServer httpServer;
    RMIClient rmiClient;

    public WWWServer(InetSocketAddress address, String url) {
        //TODO: Create http-server instance and run it

        rmiClient = new RMIClient();

        try {
            System.out.println("Starting HTTP-server...");

            httpServer = HttpServer.create(address, 0);

            // Create separate paths for status and temperature
            httpServer.createContext("/status", new Handler());
            httpServer.createContext("/temperature", new TemperatureHandler());

            ExecutorService executor = Executors.newCachedThreadPool();

            httpServer.setExecutor(executor);
            httpServer.start();

        }catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }


    //TODO: Create handlers for requests

    class Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            // Format response as json
            JSONObject response = new JSONObject();

            try {
                rmiClient.startClient();
            } catch (RemoteException | MalformedURLException | NotBoundException exception) {
                exception.printStackTrace();
            }

            OutputStream outputStream = httpExchange.getResponseBody();
            InputStream inputStream = httpExchange.getRequestBody();

            // Get input from client
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String value;

            while ((value = bufferedReader.readLine()) != null) {
                System.out.println(value);

                try {
                    JSONObject jsonObject = (JSONObject) new JSONParser().parse(value);
                    int id = Math.toIntExact((long) jsonObject.get("id"));
                    response.put("id", Integer.toString(id));
                    rmiClient.sendId(id);
                }
                catch(ParseException exception){
                    exception.printStackTrace();
                }
            }

            inputStream.close();

            // Send 200 response
            httpExchange.sendResponseHeaders(200, response.toString().getBytes().length);

            outputStream.close();
        }
    }

    class TemperatureHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String response = "";

            InputStream inputStream = httpExchange.getRequestBody();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String value;

            while ((value = bufferedReader.readLine()) != null) {

                System.out.println(value);

                try {
                    JSONObject jsonObject = (JSONObject) new JSONParser().parse(value);
                    int temperature = Math.toIntExact((long) jsonObject.get("temperature"));
                    response += temperature;
                    System.out.println(temperature);
                    rmiClient.sendTemperature(temperature);
                }
                catch(ParseException exception){
                    exception.printStackTrace();
                }
            }

            // Send 200 response
            httpExchange.sendResponseHeaders(200, "Success".getBytes().length);

            inputStream.close();
        }
    }
}
