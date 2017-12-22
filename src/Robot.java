import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;

public class Robot extends WaypointNode {
    private Boolean onStart = true;
    private ArrayList<Node> listNode = new ArrayList<>();

    private Destinations destFct = new Destinations();

    @Override
    public void onStart() {
        setIcon("src/robot.png"); // to be adapted
        setSensingRange(30);
        addDestination(base_x, base_y);
    }
    @Override
    public void onMessage(Message message) {
        if (message.getFlag().equals("LIST") && onStart) {
            listNode.addAll((ArrayList<Node>)message.getContent());
            this.onStart = false;
            destFct.setOptions(listNode);
        }
    }
    @Override
    public void onSensingIn(Node node) {
        if (node instanceof Sensor) {
            ((Sensor) node).battery = 255;
        }
        if(node instanceof BaseStation && onStart){
            send(node, new Message(null, "SEND_LIST"));
        }
    }
    @Override
    public void onArrival() {
        if(!destinations.isEmpty() && !onStart)
            destinations.poll();
        if(destinations.isEmpty() && !onStart) {
            addPrio();
            addNotPrio();
            addNode();
        }
    }
    public void addPrio(){
        for (Node aListPrio : destFct.getListPrio()) addDestination(aListPrio.getX(), aListPrio.getY());
    }
    public void addNotPrio(){
        for (Node aListNotPrio : destFct.getListNotPrio()) addDestination(aListNotPrio.getX(), aListNotPrio.getY());
    }
    public void addNode(){
        for (Node aListNode : destFct.getListNode()) addDestination(aListNode.getX(), aListNode.getY());
    }
}