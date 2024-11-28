import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private int port;
    private List<ServerThread> listOfClients;
    private boolean activeServer;

    public Server(int port){
        this.port = port;
        this.listOfClients = new ArrayList<>();
    }

    public void startServer(){

        try(ServerSocket serverSocket = new ServerSocket(port)){
            while (activeServer) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("En ny klient har ansultit");

                ServerThread cleintThread = new ServerThread(clientSocket, this);//This för att koppla till denna instans av server
                listOfClients.add(cleintThread); //En lista för att hålla koll på vilka klienter som har anslutit
                new Thread(cleintThread).start(); //Skapar upp en ny tråd för varje ansluten klient

            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public synchronized void registerClient(InputStream inputStream, OutputStream outputStream, ServerThread clientThread) {
        listOfClients.add(clientThread);
        System.out.println("En till klient har blivit till lagd." + clientThread.getClientUsername());
    }
}


