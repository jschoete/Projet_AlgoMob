import jbotsim.Node;
import java.util.ArrayList;

public class Tri {
    private ArrayList<Node> listNode_ = new ArrayList<>();
    private ArrayList[] tabList;
    private int nbRobot;
    private BaseStation base;


    public void setOption(int nbRobot, ArrayList<Node> listNode, BaseStation base){
        this.base = base;
        this.nbRobot = nbRobot;
        this.listNode_.addAll(listNode);
        this.tabList = new ArrayList[nbRobot];
        distribution();
    }
    public ArrayList getListNode(int idRobot){
        return tabList[idRobot];
    }
    private void distribution() {
        ArrayList<Node> list = new ArrayList<>();
        list.addAll(listNode_);
        listNode_.clear();
        listNode_.addAll(nearestNeighbour(list));
        listNode_.addAll(nearestNeighbour(list));
        for (int j = 0; j < nbRobot; j++) {
            tabList[j] = new ArrayList<>();
            for (int i = 0; i < listNode_.size(); i++) {
                if(i % (j +1)  == 0)
                    tabList[j].add(listNode_.get(i));
            }


        }
    }
    private ArrayList<Node> nearestNeighbour(ArrayList<Node> list) {
        ArrayList<Node> solution = new ArrayList<>();
        if (!list.isEmpty()) {
            solution.add(list.remove(0));
            while (!list.isEmpty()) {
                double min = Double.MAX_VALUE;
                int minIndex = 0;
                for (int i = 0; i < list.size(); i++) {
                    double dist = this.base.distance(list.get(i));
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
