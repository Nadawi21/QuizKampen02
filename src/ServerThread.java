/*
ServerThread hämtar ClientsThreads socket från Server.
Hämtar svar på fråga från ClientThread och skickar meddelanden dit
Game klassen tas in för att invänta spelstart och vänta på andra spelare
Kollar om enskilt svar är rätt men jämför inte poäng
 */

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {

    private Socket clientSocket;
    private Server server;
    private Game game;
    private String userName;

    private int rightAnswer = 1; //Rätt svar är alltid 1

    //Tar inputStream från Client för att läsa från Client
    BufferedReader bufferedReader;
    //Tar outputStream från Client för att läsa från Client
    PrintWriter printWriter;

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
        try{
            printWriter.println("Välj ett användarnamn: ");
            userName = bufferedReader.readLine();

            System.out.println(userName + " har anslutit.");

            //Registrera klienten på servern
            server.registerClient(bufferedReader, printWriter, this); //denna inputstream, denna outputstream, denna instans

            game = new Game(server);
            game.start();


        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    /*
    Startar spelet för en klient
    Här svarar klienten på frågorna
    Klienten får reda på om svaret är rätt eller fel
    */
    public void startGame(Game game) {

        try {
            String clientInput;
            String outputMessage;

            printWriter.println(game.getQUESTION() + ", " + game.getRIGHT_ANSWER() + ", " +
                    game.getWRONG_ANSWER1() + ", " + game.getWRONG_ANSWER2() + ", " +
                    game.getWRONG_ANSWER3());
            System.out.println(game.getQUESTION());

            while((clientInput = bufferedReader.readLine()) != null) {
                // GJorde om detta till en boolean för att kunna skicka in det i updatePoints konstruktor
                boolean rightAwnserBoolean = clientInput.equals(String.valueOf(rightAnswer));
                game.updatePoint(this,rightAwnserBoolean);

                if (rightAwnserBoolean) {
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
    public void sendMessage(String message) {
        printWriter.println(message);
    }
}