import java.util.ArrayList;
import java.util.List;

public class Game extends Thread{
    private final String QUESTION = "Choose an answer";
    private final int RIGHT_ANSWER = 1;
    private final int WRONG_ANSWER1 = 2;
    private final int WRONG_ANSWER2 = 3;
    private final int WRONG_ANSWER3 = 4;

    public String getQUESTION() {
        return QUESTION;
    }

    public int getRIGHT_ANSWER() {
        return RIGHT_ANSWER;
    }

    public int getWRONG_ANSWER1() {
        return WRONG_ANSWER1;
    }

    public int getWRONG_ANSWER2() {
        return WRONG_ANSWER2;
    }

    public int getWRONG_ANSWER3() {
        return WRONG_ANSWER3;
    }

    //Anger att det får vara max 2 användare per spel
    private final int MAX_CLIENTS = 1;

    private Server server;
    private int mainPlayerPoints  = 0;
    private int opponentsPoints  = 0;


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

            if (server.getQueue().size() >= MAX_CLIENTS){

            }
            else {
                System.out.println("Inte tillräckligt med användare anslutna");
                return;
            }
        }
    }

    public void updatePoint(ServerThread player ,boolean isRightAwnser){
        if (server.getQueue().size() < 2){
            System.out.println("Inte tillräckligt med spelare anslutna");
            return;
        }


        if (isRightAwnser){
            if (player.equals(server.getQueue().get(0))){
                mainPlayerPoints++;
            }
            else if (player.equals(server.getQueue().get(1))) {
                opponentsPoints++;
            }
        }
    }

    /*public void declareWinner(){

        if (serverThreads.size() < MAX_CLIENTS) {
            return;
        }

        ServerThread mainPlayer = server.getQueue().get(0);
        ServerThread opponents = server.getQueue().get(1);

        if (mainPlayerPoints > opponentsPoints){
            mainPlayer.sendMessage("Du vann!");
            opponents.sendMessage("Du förlorade!");
        } else if (mainPlayerPoints < opponentsPoints) {
            mainPlayer.sendMessage("Du förlorade!");
            opponents.sendMessage("Du vann!");
        }
        else {
            mainPlayer.sendMessage("Det blev oavgjort!");
            opponents.sendMessage("Det blev oavgjort!");
        }
    }

     */
}