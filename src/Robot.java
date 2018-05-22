import jbotsim.Message;
import jbotsim.Node;
import java.util.ArrayList;

public class Robot extends WaypointNode {
    Boolean onStart = true;
    ArrayList<Node> listNode_ = new ArrayList<>();

    @Override
    public void onStart() {
        setIcon("src/robot.png"); // to be adapted
        setSensingRange(getRange());
        addDestination(base_x, base_y);
        onArrival();
    }

    @Override
    public void onSensingIn(Node node) {
        /**
         * Si le robot détecte dans a range (actuellement de 30) un objet de type Node - Recharge sa batterie
         *
         * Si de type BaseStation, envoie une demande d'envoie de la liste de noeud à parcourir.
         *
         */
        if (node instanceof Sensor)
            ((Sensor) node).battery = 255;
        if(node instanceof BaseStation && onStart){
            send(node, new Message(null, "SEND_LIST"));
            onStart = false;
        }
    }

    @Override
    public void onMessage(Message message) {
        /**
         *
         * Réception de la liste de noeud à parcourir et la sauvegarde dans la variable listNode_
         */
        if (message.getFlag().equals("LIST"))
            listNode_.addAll((ArrayList<Node>)message.getContent());
    }
    @Override
    public void onArrival() {
        /**
         * Le robot considère être arrivé à destination:
         * Si la liste n'est pas vide, il retire le noeud qu'il vient de visiter de la liste destinations.
         * Sinon, recharge la liste sauvegardé listNode_
         */
        if(!destinations.isEmpty() && !onStart)
            destinations.poll();
        if(destinations.isEmpty() && !onStart)
            for (Node aList_node : listNode_)
                this.addDestination(aList_node.getX(), aList_node.getY());
    }

	//TODO hey supprimes moi pls
}
