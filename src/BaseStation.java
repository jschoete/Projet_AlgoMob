import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;
import java.util.List;

public class BaseStation extends Node {
    private ArrayList<Node> listNode1 = new ArrayList<>();
    private ArrayList<Node> listNode2 = new ArrayList<>();
    private ArrayList<Node> listNode_ = new ArrayList<>();
    private ArrayList<Robot> listRobot = new ArrayList<>();
    private ArrayList[] tabList = {listNode1, listNode2};
    private Boolean send = true;


    @Override
    public void onClock() {
        if (listNode_.size() == 32 && send) {
            send = false;
            tri();
            for (int i = 0; i < listRobot.size(); i++) {
                send(listRobot.get(i), new Message(tabList[i], "LIST"));
            }
        }
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
            if (!listNode_.contains(message.getContent()))
                listNode_.add((Node) message.getContent());
        if (message.getFlag().equals("SEND_LIST"))
            listRobot.add((Robot) message.getSender());
    }

    private void tri() {
        for (Node aListNode_ : listNode_) {
            if (aListNode_.getID() > 4)
                tabList[0].add(aListNode_);
            else
                tabList[1].add(aListNode_);
        }
    }
}
