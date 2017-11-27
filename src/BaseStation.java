import javafx.scene.control.Tab;
import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;


public class BaseStation extends Node{

    private ArrayList<Node> listNode1 = new ArrayList<>();
    private ArrayList<Node> listNode2 = new ArrayList<>();
    private ArrayList<Node> listNode_ = new ArrayList<>();

    private ArrayList[] Tab_list = {listNode1,listNode2};

    Boolean ret = true;
    int nb_robot_detect = 0;

    @Override
    public void onStart() {
        setIcon("src/server.png"); // to be adapted
        setSize(12);

        // Initiates tree construction with an empty message
        sendAll(new Message(null, "INIT"));
    }

    @Override
    public void onMessage(Message message) {
        if(message.getFlag().equals("BAT")){
            if(!listNode_.contains(message.getContent()))
                listNode_.add((Node) message.getContent());
        }
        if(message.getFlag().equals("SEND_LIST")){
            if(nb_robot_detect <= 2) {
                nb_robot_detect++;
                ArrayList<Node> list = new ArrayList<>();
                list.addAll(tri());
                if(listNode1.size() !=0 || listNode2.size() != 0)
                    send(message.getSender(), new Message(list, "LIST"));
            }
        }

    }

    private ArrayList tri(){
        int size = listNode_.size()/2;
        int start = 0;

        if(nb_robot_detect >= 2 ) {
            start = size;
            size = listNode_.size();
        }
        for (int i = start; i < size; i++) {
            Tab_list[nb_robot_detect - 1].add(listNode_.get(i));
        }

        for (int j = 0; j < Tab_list.length - 1; j++) {
            int nb_node = Tab_list[j].size();
            int max = ((Node)Tab_list[j].get(0)).getID();
            for (int i = 0; i < nb_node; i++) {
                System.out.println("color === "+ ((Node) Tab_list[j].get(i)).getID()+"              id ===  "+i);
                if(max < ((Node)Tab_list[j].get(i)).getID()){
                    max = ((Node)Tab_list[j].get(i)).getID();
                    Tab_list[j].add(Tab_list[j].remove(i));
                }
            }
        }

        return Tab_list[nb_robot_detect - 1];
    }
}