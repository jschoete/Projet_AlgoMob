import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;

public class Robot extends WaypointNode {
    private Boolean onStart = true;
    private ArrayList<Node> listNode = new ArrayList<>();
    private ArrayList<Node> listPrio = new ArrayList<>();
    private ArrayList<Node> listNotPrio = new ArrayList<>();

    int state1;
    int size;
    int min = Integer.MAX_VALUE;
    int max = 0;

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
            getMinMax();
            tri();
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
            addNode();
            if(--state1 == 0) {
                state1 = listPrio.size() - listPrio.size() / 3;
                addPrio();
                addNotPrio();
                addNode();
                addPrio();
            }

        }
    }


    private void getMinMax(){
        for (int i = 0; i < listNode.size(); i++) {
            if(min > listNode.get(i).getID())
                min = listNode.get(i).getID();
            if(max < listNode.get(i).getID())
                max = listNode.get(i).getID();
        }
    }

    private void addPrio(){
        for (int i = 0; i < listPrio.size(); i++)
            addDestination(listPrio.get(i).getX(), listPrio.get(i).getY());
    }
    private void addNotPrio(){
        for (int i = 0; i < listNotPrio.size(); i++)
            addDestination(listNotPrio.get(i).getX(), listNotPrio.get(i).getY());
    }

    private void addNode(){
        for (int i = 0; i < listNode.size(); i++)
            addDestination(listNode.get(i).getX(), listNode.get(i).getY());
    }

    private void tri(){
        int size = listNode.size();
        ArrayList<Node> list = new ArrayList<>();
        //list.addAll(listNode);
        for (int i = 0; i < size; i++) {
            if (listNode.get(i).getID() >= (max /2)) {
                System.out.println((max /2));
                listPrio.add(listNode.get(i));
            }
            else if(listNode.get(i).getID() > min) {
                list.add(listNode.get(i));
            }
            else{
                listNotPrio.add(listNode.get(i));
            }
        }
        listNode.clear();
        listNode.addAll(list);

        state1 = listPrio.size() - listPrio.size() / 3;

    }
}