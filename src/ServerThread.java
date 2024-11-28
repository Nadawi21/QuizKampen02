//ServerThread hämtar ClientsThreads socket från Server.
//Hämtar svar på fråga från ClientThread och skickar meddelanden dit
//Game klassen tas in för att invänta spelstart och vänta på andra spelare

//Kollar om enskilt svar är rätt men jämför inte poäng

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {

    private Socket clientSocket;
    private String clientUsername;
    // ev ha med counter för antal rätt
    private Server server;
    private Game game;

    private int rightAnswer = 1; //Rätt svar är alltid 1

    //Tar inputStream från Client för att läsa från Client
    private InputStream inputStream;
    //Tar outputStream från Client för att läsa från Client
    private OutputStream outputStream;

    private boolean hold; //Väntar på andra spelaren
    private final int BUFFER = 2739;

    //Användaren kan välja att spela eller avsluta spelet
    private final String PLAY = "p";
    private final String EXIT = "e";

    //Konstruktor startas upp av servern och tar Clients socket och servern
    public ServerThread(Socket ClientSocket, Server server) {
        this.clientSocket = ClientSocket;
        this.server = server;

        //Skapar upp klientens input- och outputstream
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lyckades inte skapa upp klientens input- och outputstream");
        }
    }

    //Kör spelets flöde för denna klient (kör servertråden)
    public void run() {
        System.out.println("Ny spelare har anslutit.");

        //Registrera klienten på servern
        server.registerClient(inputStream, outputStream, this); //denna inputstream, denna outputstream, denna instans

        while (true) {

            //Startar spelet
            startGame(game);

            //Väntar på att den andra klienten ska avsluta spelet
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Misslyckades med att vänta på den andra spelaren");
            }

        }
    }

    //Startar spelet för en klient
// Här svarar kliennten på frågorna
//Klienten får reda på om svaret är rätt eller fel
    public void startGame(Game game) {

        try {
            String clientInput;
            String outputMessage;

            clientInput = String.valueOf(inputStream.read());

            if (clientInput.equals(rightAnswer)) {
                outputMessage = "Korrekt svar!";
            } else {
                outputMessage = "Fel svar!";

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fel när klientens svar kontrollerades");
        }

    }

    // Skickar nuvarande state till Client och får svar från Client
    private String getInput(OutputStream outputStream, InputStream inputStream, String state) {
        byte[] buffer = new byte[BUFFER];
        String input_from_client = null;

        //Skickar state till Client och får svar
        try {
            outputStream.write(state.getBytes());
            inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem vid läsning från client om state");
        }

        //Konvertera till String
        input_from_client = new String(buffer);
        //input_from_client = new String(buffer).replace("\0","");
        return input_from_client;
    }

    //  Sets client name
    public void setClientUsername(String ClientUsername) {
        this.clientUsername = ClientUsername;
    }

    //  Gets client name
    public String getClientUsername() {
        return this.clientUsername;
    }

    //  Sets game
    public void setGame(Game game) {
        this.game = game;
    }
}