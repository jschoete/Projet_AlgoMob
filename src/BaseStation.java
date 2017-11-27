import jbotsim.Message;
import jbotsim.Node;

import java.awt.geom.Point2D;
import java.util.ArrayList;
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
        int size = listNode_.size()/3;

        if(nb_robot_detect >= 2 ) {
            start = size;
            size = listNode_.size();
            for (int j = 0; j < 3; j++) {
                for (int i = start; i < size - size/5; i++) {
                    Tab_list[nb_robot_detect - 1].add(listNode_.get(i));
                }
            }
        }

        for (int i = start; i < size; i++) {
            Tab_list[nb_robot_detect - 1].add(listNode_.get(i));
        }


        return Tab_list[nb_robot_detect - 1];
    }


/*
    public List<Point2D> getItinerary() {
        List<Point2D> solution = new ArrayList<>();
        List<Point2D> stack = new ArrayList<>(this.points);
        if (!stack.isEmpty()) {
            // set first destination
            solution.add(stack.remove(0));

            // search nearest neighbors and add them to list
            while (!stack.isEmpty()) {
                double min = Double.MAX_VALUE;
                int minIndex = 0;
                for (int i = 0; i < stack.size(); i++) {
                    double dist = stack.get(i).distance(solution.get(solution.size() - 1));
                    if (min > dist) {
                        min = dist;
                        minIndex = i;
                    }
                }
                solution.add(stack.remove(minIndex));
            }
        }
        return solution;
    }
*/

}