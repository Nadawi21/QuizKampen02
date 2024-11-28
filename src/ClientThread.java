import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {

    public ClientThread() {
        run();
    }

    //Denna metod kör klienttrådarna parallellt
    public void run() {
        Scanner scanner;

        //Tar serverns inputstream och läser från servern
        //Tar serverns outputstream och skriver till servern
        try (Socket socket = new Socket("127.0.0.1", 8091);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            scanner = new Scanner(System.in);

            String serverInput;
            String clientOutput = "";
           //loopen tar in input från servertråden och driver spelet framåt utifrån states
            while (true) {

                serverInput = bufferedReader.readLine();

                //State-logiken börjar här
                if (serverInput.equals("EXIT_GAME")) {
                    break;
                } else if (serverInput.equals("ENTER_NAME")) {
                    while (true) {
                        clientOutput = scanner.nextLine();
                    }
                    //TODO: Vi kanske behöver ett state som ställer en fråga till att börja med
                }
                //..Detta kommer att byggas ut

                //Skickar antingen användarnamn eller svar till servern
                out.write(clientOutput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}