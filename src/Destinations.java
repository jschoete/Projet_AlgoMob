import jbotsim.Node;
import java.util.ArrayList;

public class Destinations extends WaypointNode {
    private ArrayList<Node> listNode_ = new ArrayList<>();
    private ArrayList<Node> listPrio = new ArrayList<>();
    private ArrayList<Node> listNotPrio = new ArrayList<>();
    private int min = Integer.MAX_VALUE;
    private int max = 0;

    public void setOptions(ArrayList<Node> listNode){
        this.listNode_.addAll(listNode);
        getMinMax();
        distribution();
    }
    private void distribution(){
        int size = listNode_.size();
        ArrayList<Node> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (listNode_.get(i).getID() >= (max /2)) {
                listPrio.add(listNode_.get(i));
            }
            else if(listNode_.get(i).getID() > min) {
                list.add(listNode_.get(i));
            }
            else{
                listNotPrio.add(listNode_.get(i));
            }
        }        listNode_.clear();
        listNode_.addAll(list);
    }
    public void getMinMax(){
        for (int i = 0; i < listNode_.size(); i++) {
            if(min > listNode_.get(i).getID())
                min = listNode_.get(i).getID();
            if(max < listNode_.get(i).getID())
                max = listNode_.get(i).getID();
        }
    }
    public ArrayList<Node> getListNode(){
        return listNode_;
    }

    public ArrayList<Node> getListNotPrio() {
        return listNotPrio;
    }

    public ArrayList<Node> getListPrio() {
        return listPrio;
    }
}