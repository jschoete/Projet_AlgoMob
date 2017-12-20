import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;
import java.util.List;

public class BaseStation extends Node{

    private ArrayList<Node> listNode1 = new ArrayList<>();
    private ArrayList<Node> listNode2 = new ArrayList<>();
    private ArrayList<Node> listNode_ = new ArrayList<>();

    private ArrayList[] Tab_list = {listNode1,listNode2};

    int nb_robot_detect = 0;
    int nb_sensor = 0;
    int send = 0;

    private ArrayList<Robot> listRobot = new ArrayList<>();
    @Override
    public void onStart() {
        setIcon("src/server.png"); // to be adapted
        setSize(12);

        // Initiates tree construction with an empty message
        sendAll(new Message(null, "INIT"));
    }

    @Override
    public void onMessage(Message message) {
        if(message.getFlag().equals("CHILD")){
            if(!listNode_.contains(message.getContent())) {
                listNode_.add((Node) message.getContent());
                nb_sensor++;
            }
        }

        if(message.getFlag().equals("SEND_LIST")){
            System.out.println("ok BASE");
            nb_robot_detect++;
            tri();
            send(message.getSender(), new Message(Tab_list[nb_robot_detect - 1], "LIST"));

        }
    }


    private ArrayList tri(){
        int start = 0;
        int size = listNode_.size()/4 ;


        nearestNeighbour(listNode_);
        triChildren();
        if(nb_robot_detect >= 2 ) {
            start = size - 2;
            size = listNode_.size();
            for (int i = start; i < size; i++) {
                Tab_list[nb_robot_detect - 1].add(listNode_.get(i));
            }
        }

        for (int i = start; i < size; i++) {
            Tab_list[nb_robot_detect - 1].add(listNode_.get(i));
        }



        return Tab_list[nb_robot_detect - 1];
    }

    public void triChildren(){
        List<Node> solution = new ArrayList<>();
        if (!listNode_.isEmpty()) {
            // set first destination
            int max = 0;
            int id = 0;
            for (int i = 0; i < listNode_.size(); i++) {
                if(max < listNode_.get(0).getID()){
                    max = listNode_.get(0).getID();
                    id = i;
                }
            }
            solution.add(listNode_.remove(id));

            // search nearest neighbors and add them to list
            while (!listNode_.isEmpty()) {
                max = 0;
                id = 0;
                for (int i = 0; i < listNode_.size(); i++) {
                    if (max < listNode_.get(i).getID()) {
                        max = listNode_.get(i).getID();

                        id = i;
                    }
                }

                solution.add(listNode_.remove(id));
            }
        }
        listNode_.addAll(solution);

    }


    public ArrayList nearestNeighbour(ArrayList<Node> list) {
        //la grande liste
        List<Node> solution = new ArrayList<>();
        if (!list.isEmpty()) {
            // set first destination
            solution.add(list.remove(0));

            // search nearest neighbors and add them to list
            while (!list.isEmpty()) {
                double min = Double.MAX_VALUE;
                int minIndex = 0;
                for (int i = 0; i < list.size(); i++) {
                    double dist = (list.get(i)).distance(solution.get(solution.size() - 1));
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