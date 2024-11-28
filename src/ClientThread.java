import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {

    public ClientThread() {
        start();
    }

    //Denna metod kör klienttrådarna parallellt
    public void run() {
        //Tar serverns inputstream och läser från servern
        //Tar serverns outputstream och skriver till servern
        try (Socket socket = new Socket("127.0.0.1", 8091);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            Scanner scanner = new Scanner(System.in);

            String serverInput;
            String clientOutput = "";
           //loopen tar in input från servertråden och driver spelet framåt utifrån states
            while ((serverInput = bufferedReader.readLine()) != null) {
                System.out.println(serverInput);

                //State-logiken börjar här
                /*if (serverInput.equals("EXIT_GAME")) {
                    break;
                } else if (serverInput.equals("ENTER_NAME")) {
                    while (true) {
                        clientOutput = scanner.nextLine();
                    }
                } else if (serverInput.equals("ASK_QUESTION"));*/
                clientOutput = scanner.nextLine();

                //Skickar antingen användarnamn eller svar till servern
                out.println(clientOutput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}