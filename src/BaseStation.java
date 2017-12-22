import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;

public class BaseStation extends Node {
    private ArrayList<Node> listNode_ = new ArrayList<>();
    private ArrayList<Robot> listRobot = new ArrayList<>();

    private int send = 0;
    private int timer;
    private Tri tri = new Tri();


    @Override
    public void onClock() {
        if (listNode_.size() != 0 && timer > 1 && listRobot.size() != 0 && listRobot.size() > send) {
            if(send == 0) {
                tri.setOption(listRobot.size(), listNode_, this);
            }
            System.out.println(listRobot.size()+ " size "+listNode_.size());
            send(listRobot.get(send), new Message(tri.getListNode(send), "LIST"));
            send++;
        }
        timer++;
    }
    @Override
    public void onStart() {
        setIcon("src/server.png"); // to be adapted
        setSize(12);
        // Initiates tree construction with an empty message
        sendAll(new Message(null, "INIT"));
    }
    @Override
    public void onMessage(Message message) {
        if (message.getFlag().equals("CHILD"))
            if (!listNode_.contains(message.getContent())){
                listNode_.add((Node) message.getContent());
                timer = 0;
            }
        if (message.getFlag().equals("SEND_LIST")) {
            if (!listNode_.contains(message.getContent())) {
                listRobot.add((Robot) message.getSender());
                timer = 0;
            }
        }
    }

}
