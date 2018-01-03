import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;
import java.util.List;

public class Robot extends WaypointNode {
    Boolean onStart = true;
    ArrayList<Node> listNode_ = new ArrayList<>();

    @Override
    public void onStart() {
        setIcon("src/robot.png"); // to be adapted
        setSensingRange(30);
        addDestination(base_x, base_y);
        onArrival();
    }
    @Override
    public void onMessage(Message message) {
        if (message.getFlag().equals("LIST"))
            listNode_.addAll((ArrayList<Node>)message.getContent());
    }
    @Override
    public void onSensingIn(Node node) {
        if (node instanceof Sensor)
            ((Sensor) node).battery = 255;
        if(node instanceof BaseStation && onStart){
            send(node, new Message(null, "SEND_LIST"));
            onStart = false;
        }

    }
    @Override
    public void onArrival() {
        if(!destinations.isEmpty() && !onStart)
            destinations.poll();
        if(destinations.isEmpty() && !onStart)
            for (Node aList_node : listNode_)
                this.addDestination(aList_node.getX(), aList_node.getY());
    }
}