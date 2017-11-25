import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;

public class Robot extends WaypointNode {
    int idR;
    public void setIdRobot(int id) {
        this.idR = id;
    }
    public int getIdRobot() {
        return idR;
    }

    @Override
    public void onStart() {
        setIcon("src/robot.png"); // to be adapted
        setSensingRange(30);
        addDestination(base_x, base_y);
        onArrival();
    }

    @Override
    public void onMessage(Message message) {
        if (message.getFlag().equals("LIST")) {
            ArrayList<BatteryState> list = (ArrayList<BatteryState>) message.getContent();
            if(!list.isEmpty())
                while (!list.isEmpty()) {
                    BatteryState b = list.remove(0);
                    this.addDestination(b.getX(), b.getY());
                }
            else {
                for (int i = idR; i < message.getSender().getNeighbors().size() - 1; i += 2) {
                    addDestination(message.getSender().getNeighbors().get(i).getX(),
                            message.getSender().getNeighbors().get(i).getY());
                }

            }
        }
    }

    @Override
    public void onSensingIn(Node node) {
        if (node instanceof Sensor) {
            ((Sensor) node).battery = 255;
        }
        if(node instanceof BaseStation){
            send(node, new Message(destinations, "SEND_LIST"));
        }
    }

    @Override
    public void onArrival() {
    }
}