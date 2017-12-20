import jbotsim.Message;
import jbotsim.Node;
import java.awt.*;

public class Sensor extends Node {
    private Node parent = null;
    int battery = 255;
    private Boolean isbattery = false;

    private int nb_children = 0;

    private int time = 0;

    private Boolean send = true;
    private Boolean isParent = false;
    private Boolean isLeaf = false;

    @Override
    public void onMessage(Message message) {
        // "INIT" flag : construction of the spanning tree
        // "SENSING" flag : transmission of the sensed values
        // You can use other flags for your algorithms
        if (message.getFlag().equals("INIT")) {
            // if not yet in the tree
            if (parent == null) {
                // enter the tree
                parent = message.getSender();
                getCommonLinkWith(parent).setWidth(4);
                // propagate further
                sendAll(message);
                send(parent, new Message(null, "PAR"));
            }
        } else if (message.getFlag().equals("SENSING")) {
            // retransmit up the tree
            send(parent, message);
        }
        else if(message.getFlag().equals("PAR")){
            isParent = true;
            isLeaf = false;
            this.nb_children++;
            send(parent, message);
            time = 0;
        }
        else if(message.getFlag().equals("CHILD")){
            send(parent, message);
        }
    }

    @Override
    public void send(Node destination, Message message) {
        if (battery > 0) {
            super.send(destination, message);
            battery--;
            if(battery == 0){
                //System.out.println("Battery = 0 !!!!" + getTime()+" "+this.getID());
                isbattery = true;
            }
            updateColor();
        }
    }

    @Override
    public void onClock() {
        if (parent != null) { // if already in the tree
            if (Math.random() < 0.02) { // from time to time...
                double sensedValue = Math.random(); // sense a value
                send(parent, new Message(sensedValue, "SENSING")); // send it to parent
            }


            if(this.send && (this.isLeaf || (this.isParent && this.time > 1))){
                this.send = false;

                Node node = new Node();
                node.setLocation(this.getX(), this.getY());

                node.setID(this.nb_children);

                send(parent, new Message(node, "CHILD"));
                System.out.println("nb_children    "+this.nb_children+"     ID   "+this.getID());
            }
            else if (send && !this.isParent) {
                if(time != 0)
                    isLeaf = true;
            }
            time++;
        }
    }

    protected void updateColor() {
        setColor(battery == 0 ? Color.red : new Color(255 - battery, 255 - battery, 255));
    }
}