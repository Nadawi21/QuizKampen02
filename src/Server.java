import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private int port;
    private List<ServerThread> listOfClients;
    private boolean activeServer;


    public Server() {
        port = 8091;
        this.listOfClients = new ArrayList<>();
        activeServer = true;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (activeServer) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("En ny klient har anslutit");

                ServerThread serverThread = new ServerThread(clientSocket, this);//This för att koppla till denna instans av server
                listOfClients.add(serverThread); //En lista för att hålla koll på vilka klienter som har anslutit
                new Thread(serverThread).start(); //Skapar upp en ny tråd för varje ansluten klient
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void registerClient(BufferedReader inputStream, PrintWriter outputStream, ServerThread clientThread) {
        listOfClients.add(clientThread);
        System.out.println("En till klient har blivit tillagd till listan över klienter." + clientThread.getClientUsername());
    }

    //TODO: Få servern att skicka ASK_QUESTION till båda klienterna

    // Skickar output till Client och väntar på continue message.
    public static void sendOutput(OutputStream outputStream, InputStream inputStream, String output) throws IOException {
        byte[] buffer = new byte[2739];
        outputStream.write(output.getBytes());
        inputStream.read(buffer);
    }

    // Main metod för att starta servern
    public static void main(String[] args) {
        Server server = new Server();
    }

    public List<ServerThread> getQueue() {
        return listOfClients;
    }

    public void resetQueue() {
        listOfClients.clear();
    }
}