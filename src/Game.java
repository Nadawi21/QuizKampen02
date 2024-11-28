import java.util.ArrayList;
import java.util.List;

public class Game extends Thread{
    private final String QUESTION = "Choose an answer";
    private final int RIGHT_ANSWER = 1;
    private final int WRONG_ANSWER1 = 2;
    private final int WRONG_ANSWER2 = 3;
    private final int WRONG_ANSWER3 = 4;

    //Anger att det får vara max 2 användare per spel
    private final int MAX_CLIENTS = 1;

    private Server server;

    //En lista som håller koll på alla klienter/spelare
    List<ServerThread> serverThreads = new ArrayList<>();

    private int serverThreadCounter;

    Game(Server server){
        this.server = server;
    }

    public void run(){
        //Tråd sover tills två spelare är anslutna
        while(server.getQueue().size() < MAX_CLIENTS){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //Lägger till servertrådar till listan
        for(int i = 0; i < MAX_CLIENTS; i++){
            serverThreads.add(server.getQueue().get(i));
        }
        serverThreadCounter = serverThreads.size();
        //Ser till att man letar efter spelare för nästa spel
        server.resetQueue();

        for(ServerThread serverThread : serverThreads){
            serverThread.startGame(this);
            serverThread.notify();
        }

    }

}
