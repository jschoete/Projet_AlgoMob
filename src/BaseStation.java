import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;
import java.util.List;

public class BaseStation extends Node{
    private ArrayList<Node> listNode_ = new ArrayList<>();
    private ArrayList<Node> listRobot =  new ArrayList<>();
    private ArrayList[] tabList = new ArrayList[50];

    @Override
    public void onStart() {
        setIcon("src/server.png"); // to be adapted
        setSize(12);

        // Initiates tree construction with an empty message
        sendAll(new Message(null, "INIT"));
    }

    @Override
    public void onMessage(Message message) {
        switch (message.getFlag()) {
            case "PARENT":
                if (!listNode_.contains(message.getContent()))
                    listNode_.add((Node) message.getContent());
                break;
            case "SEND_LIST":
                if(!listRobot.contains(message.getSender())) {
                    listRobot.add(message.getSender());
                    tri();
                    if(tabList[listRobot.size() - 1].size() !=0)
                        send(message.getSender(), new Message(tabList[listRobot.size() - 1], "LIST"));
                }
                break;
        }
    }

    private void tri(){
        tabList[listRobot.size() - 1] = new ArrayList();
        nearestNeighbour();
        int start = 0;
        int size = listNode_.size() / 4;

        if (listRobot.size() > 1){
            start = size - 1;
            size = listNode_.size();
        }
        for (int j = start; j < size; j++) {
            tabList[listRobot.size() - 1].add(listNode_.get(j));
        }
    }

    public void nearestNeighbour() {
        List<Node> solution = new ArrayList<>();
        if (!listNode_.isEmpty()) {
            solution.add(listNode_.remove(0));
            while (!listNode_.isEmpty()) {
                double min = Double.MAX_VALUE;
                int minIndex = 0;
                for (int i = 0; i < listNode_.size(); i++) {
                    double dist = (listNode_.get(i)).distance(solution.get(solution.size() - 1));
                    if (min > dist) {
                        min = dist;
                        minIndex = i;
                    }
                }
                solution.add(listNode_.remove(minIndex));
            }
        }
        listNode_.addAll(solution);
    }
}