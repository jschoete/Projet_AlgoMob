import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;

public class Robot extends WaypointNode {
    private Boolean onStart = true;
    private ArrayList<Node> listNode = new ArrayList<>();

    @Override
    public void onStart() {
        setIcon("src/robot.png"); // to be adapted
        setSensingRange(30);

        addDestination(base_x, base_y);

    }

    @Override
    public void onMessage(Message message) {
        if (message.getFlag().equals("LIST")) {
            listNode.addAll((ArrayList<Node>)message.getContent());
            this.onStart = false;
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
        if(!destinations.isEmpty() && !onStart){
            destinations.poll();
        }
        if(destinations.isEmpty() && !onStart) {
            for (int i = 0; i < listNode.size(); i++) {
                addDestination(listNode.get(i).getX(),listNode.get(i).getY());
            }
        }
    }

}