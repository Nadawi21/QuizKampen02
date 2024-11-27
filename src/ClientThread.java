import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread{

    private Socket socket;

 public  ClientThread(Socket socket){
     this.setDaemon (true); //prova att ta bort/ändra
     this.socket = socket;
 }
 //Denna metod kör klienttrådarna parallellt
 public void run(){
     InputStream inputStream = null;
     OutputStream outputStream = null;
     Scanner scanner = null;
 try {
     //Tar serverns inputstream och läser från servern
     inputStream = socket.getInputStream();
     //Tar serverns outputstream och skriver till servern
     outputStream = socket.getOutputStream();

     scanner = new Scanner(System.in);

     String serverInput;
     String clientOutput;
     //Buffrar bytes från servern
     byte[] serverBuffer = new byte[2739];
//loopen tar in input från servertråden och driver spelet framåt utifrån states
     while(true) {

         inputStream.read(serverBuffer);
         //konverterar buffer
         serverInput = new String(serverBuffer);
         //Rensar buffer
         serverBuffer = new byte[2739];

         //State-logiken börjar här
         if (serverInput.equals("EXIT_GAME")){
             break;
         } else if (serverInput.equals("WAITING_OTHER_PLAYER")){
          System.out.println("Väntar på att spelare2 ansluter..");
          //Säger åt servern att fortsätta
          clientOutput = "Continue";
         } else if (serverInput.equals("ENTER_NAME")){
       while (true) {
       clientOutput = scanner.nextLine();
       }
         }
     //..Detta kommer att byggas ut

     //Skickar antingen användarnamn eller svar till servern
     outputStream.write(clientOutput.getBytes());
     }
// Stänger strömmarna, socketen och scanner
     }catch(IOException e){
     e.printStackTrace();
     } finally {
     try {
         if (scanner != null) {
             scanner.close();
         }
         if (inputStream != null) {
             inputStream.close();
         }
         if (outputStream != null) {
             outputStream.close();
         }
         if (socket != null) {
             socket.close();
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
     }
 }

}


