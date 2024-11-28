import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {

    public ClientThread() {
        start();
    }

    //Denna metod kör klienttrådarna parallellt
    public void run() {
        //Tar serverns input- och outputstream och läser från servern
        try (Socket socket = new Socket("127.0.0.1", 8093);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            Scanner scanner = new Scanner(System.in);

            String serverInput;
            String clientOutput = "";
           //loopen tar in input från servertråden och driver spelet framåt utifrån states
            while ((serverInput = bufferedReader.readLine()) != null) {
                System.out.println(serverInput);

                clientOutput = scanner.nextLine();

                //Skickar antingen användarnamn eller svar till servern
                out.println(clientOutput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}