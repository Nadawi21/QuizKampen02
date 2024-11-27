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
     inputStream = socket.getInputStream();

     outputStream = socket.getOutputStream();

     scanner = new Scanner(System.in);

     String serverInput, clientOutput;

     while(true) {
    }
}
