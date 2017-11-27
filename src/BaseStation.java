import com.sun.org.apache.xpath.internal.operations.Bool;
import jbotsim.Message;
import jbotsim.Node;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BaseStation extends Node{

    private ArrayList<Node> listNode1 = new ArrayList<>();
    private ArrayList<Node> listNode2 = new ArrayList<>();
    private ArrayList<Node> listNode_ = new ArrayList<>();

    private ArrayList[] Tab_list = {listNode1,listNode2};

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
        int start = 0;
        int size = listNode_.size()/4;

        if(nb_robot_detect >= 2 ) {
            start = size;
            size = listNode_.size();
            for (int i = start; i < size; i++) {
                Tab_list[nb_robot_detect - 1].add(listNode_.get(i));
            }
        }

        for (int i = start; i < size; i++) {
            Tab_list[nb_robot_detect - 1].add(listNode_.get(i));
        }

        Tab_list[nb_robot_detect - 1] = nearestNeighbour(Tab_list[nb_robot_detect - 1]);

        if(nb_robot_detect >= 2) {
            ArrayList<Node> list = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                for (int i = start; i < size - size/3; i++) {
                    list.add(listNode_.get(i));
                }
                list = nearestNeighbour(list);
                //Collections.reverse(list);
                Tab_list[nb_robot_detect - 1].addAll(list);
            }


        }


        return Tab_list[nb_robot_detect - 1];
    }



    public ArrayList nearestNeighbour(ArrayList<Node> list) {
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