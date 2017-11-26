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
        int size = listNode_.size()/3;
        int start = 0;

        if(nb_robot_detect >= 2 ) {
            start = size;
            size = listNode_.size();
        }
        for (int i = start; i < size; i++) {
            Tab_list[nb_robot_detect - 1].add(listNode_.get(i));
        }

        return Tab_list[nb_robot_detect - 1];
    }
}