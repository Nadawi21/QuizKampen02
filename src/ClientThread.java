import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {

    private Socket socket;

    public ClientThread(Socket socket) {
        this.setDaemon(true); //prova att ta bort/ändra
        this.socket = socket;
    }

    //Denna metod kör klienttrådarna parallellt
    public void run() {
        //InputStream inputStream = null;
         //outputStream = null;
        Scanner scanner = null;

        //Tar serverns inputstream och läser från servern
        //Tar serverns outputstream och skriver till servern
        try (InputStream inputStream = new InputStream(socket.getInputStream());
             OutputStream outputStream = socket.getOutputStream();
        ) {
            scanner = new Scanner(System.in);

            String serverInput;
            String clientOutput = "";
            //Buffrar bytes från servern
            byte[] serverBuffer = new byte[2739];
           //loopen tar in input från servertråden och driver spelet framåt utifrån states
            while (true) {

                inputStream.read(serverBuffer);
                //konverterar buffer
                serverInput = new String(serverBuffer);
                //Rensar buffer
                serverBuffer = new byte[2739];

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
                outputStream.write(clientOutput.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}