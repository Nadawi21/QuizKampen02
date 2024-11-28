import java.net.Socket;

//Skapar en ClientThread när Client körs och skickar in socket
public class Client {

    public static void main(String[] args) {

        try (Socket socket = new Socket("127.0.0.1", 8091)) {
            ClientThread thread = new ClientThread(socket);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem med att skapa Client socket och starta en ny Client");
        }

    }
}