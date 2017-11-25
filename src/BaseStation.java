import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class BaseStation extends Node{

    private ArrayList<BatteryState> listNode1 = new ArrayList<>();
    private ArrayList<BatteryState> listNode2 = new ArrayList<>();

    Robot[] listRobot = null;

    @Override
    public void onStart() {
        setIcon("src/server.png"); // to be adapted
        setSize(12);

        // Initiates tree construction with an empty message
        System.out.println("START");
        sendAll(new Message(null, "INIT"));
    }

    @Override
    public void onMessage(Message message) {
        if(message.getFlag().equals("BAT")){
            BatteryState batMSG = (BatteryState) message.getContent();
            Node node = new Node();
            node.setLocation(batMSG.getX(), batMSG.getY());
            if(listRobot[0].distance(node) < listRobot[1].distance(node)) {
                listNode1.add(batMSG);
            }
            else if(listRobot[0].distance(node) >= listRobot[1].distance(node)) {
                listNode2.add(batMSG);
            }
        }
    }

    public void setListRobot(Robot[] robots){
        listRobot = robots;
    }

    public ArrayList<BatteryState> getList(int id){
        if(id == 0)
            return listNode1;
        else
            return listNode2;
    }

    @Override
    public void onSensingIn(Node robot) {
        if (robot instanceof Robot) {
            ArrayList<BatteryState> list = getList(((Robot) robot).getIdRobot());
            Collections.sort(list, new Comparator<BatteryState>() {
                @Override
                public int compare(BatteryState b1, BatteryState b2) {
                    Node node1 = new Node();
                    node1.setLocation(b1.getX(), b1.getY());
                    Node node2 = new Node();
                    node2.setLocation(b2.getX(), b2.getY());
                    return Double.compare(robot.distance(node1), robot.distance(node2));
                }
            });
            while (!list.isEmpty()) {
                BatteryState b = list.remove(0);
                ((Robot)robot).addDestination(b.getX(), b.getY());
            }
        }
    }
}