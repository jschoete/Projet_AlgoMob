import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;


public class BaseStation extends Node{

    private ArrayList<BatteryState> listNode1 = new ArrayList<>();
    private ArrayList<BatteryState> listNode2 = new ArrayList<>();

    ArrayList[] Tab_list = {listNode1,listNode2};

    Robot[] listRobot = null;
    int nb_message = 0;
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
                if(nb_message%2 == 0)
                    Tab_list[0].add(batMSG);
                else
                    Tab_list[1].add(batMSG);
                nb_message++;
        }
        if(message.getFlag().equals("SEND_LIST")){
            nb_message = 0;
            Robot robot = (Robot) message.getSender();
            ArrayList<BatteryState> list = getList(robot.getIdRobot());
            send(robot, new Message(list, "LIST"));
        }


    }

    public void setListRobot(Robot[] robots){
        listRobot = robots;
    }

    public ArrayList getList(int id){
        return Tab_list[id];
    }
}