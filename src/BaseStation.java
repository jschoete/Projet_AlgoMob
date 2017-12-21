import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;

public class BaseStation extends Node {
    private ArrayList<Node> listNode1 = new ArrayList<>();
    private ArrayList<Node> listNode2 = new ArrayList<>();
    private ArrayList<Node> listNode_ = new ArrayList<>();
    private ArrayList<Robot> listRobot = new ArrayList<>();
    private ArrayList[] tabList = {listNode1, listNode2};
    private Boolean send = true;
    private int nbSensors;

    public void setNumberSensor(int sensors){
        nbSensors = sensors;
    }
    @Override
    public void onClock() {
        if (listNode_.size() == nbSensors && send) {
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
        nearestNeighbour(listNode_);
        for (int i = 0; i < listNode_.size(); i++) {
            //if (aListNode_.getID() > 4)
            if (i < listNode_.size()/4)

                tabList[0].add(listNode_.get(i));
            else
                tabList[1].add(listNode_.get(i));
        }
    }

    private ArrayList nearestNeighbour(ArrayList<Node> list) {
        //la grande liste
        ArrayList<Node> solution = new ArrayList<>();
        if (!list.isEmpty()) {
            // set first destination
            solution.add(list.remove(0));

            // search nearest neighbors and add them to list
            while (!list.isEmpty()) {
                double min = Double.MAX_VALUE;
                int minIndex = 0;
                for (int i = 0; i < list.size(); i++) {
                    double dist = list.get(i).distance(this);
                    if (min > dist) {
                        min = dist;
                        minIndex = i;
                    }
                }
                solution.add(list.remove(minIndex));
            }
        }
        list.addAll(solution);
        return list;
    }
}
