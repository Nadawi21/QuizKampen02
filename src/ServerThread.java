//ServerThread hämtar ClientsThreads socket från Server.
//Hämtar svar på fråga från ClientThread och skickar meddelanden dit
//Game klassen tas in för att invänta spelstart och vänta på andra spelare

//Kollar om enskilt svar är rätt men jämför inte poäng

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {

    private Socket clientSocket;
    private String clientUsername;
    // ev ha med counter för antal rätt
    private Server server;
    private Game game;

    private int rightAnswer = 1; //Rätt svar är alltid 1

    //Tar inputStream från Client för att läsa från Client
    BufferedReader bufferedReader;
    //Tar outputStream från Client för att läsa från Client
    PrintWriter printWriter;

    private final int BUFFER = 2739;

    //Konstruktor startas upp av servern och tar Clients socket och servern
    public ServerThread(Socket ClientSocket, Server server) {
        this.clientSocket = ClientSocket;
        this.server = server;

        //Skapar upp klientens input- och outputstream
        try {
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lyckades inte skapa upp klientens input- och outputstream");
        }
    }

    //Kör spelets flöde för denna klient (kör servertråden)
    public void run() {
        System.out.println("Ny spelare har anslutit.");
        game = new Game(server);
        game.start();

        //Registrera klienten på servern
        server.registerClient(bufferedReader, printWriter, this); //denna inputstream, denna outputstream, denna instans

            //Startar spelet
            //startGame(game);

    }

    //Startar spelet för en klient
// Här svarar kliennten på frågorna
//Klienten får reda på om svaret är rätt eller fel
    public void startGame(Game game) {

        try {
            String clientInput;
            String outputMessage;

            printWriter.println(game.getQUESTION() + ", " + game.getRIGHT_ANSWER() + ", " +
                    game.getWRONG_ANSWER1() + ", " + game.getWRONG_ANSWER2() + ", " +
                    game.getWRONG_ANSWER3());
            System.out.println(game.getQUESTION());

            while((clientInput = bufferedReader.readLine()) != null) {

                if (clientInput.equals(String.valueOf(rightAnswer))) {
                    outputMessage = "Korrekt svar!";
                    printWriter.println(outputMessage);
                } else {
                    outputMessage = "Fel svar!";
                    printWriter.println(outputMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fel när klientens svar kontrollerades");
        }
    }
}