import jbotsim.Message;
import jbotsim.Node;
import java.awt.*;

public class Sensor extends Node {
    private Node parent = null;
    int battery = 255;

    @Override
    public void onMessage(Message message) {
        /**
         * Réception des messages
         *
         * Init - demande d'initialisation de la base, transfert de cette demande à tous les noeuds détecter
         * Sensing - envoie la donnée capté par le noeud
         * Parent - signale au noeud ses coordonnées
         */
        // "INIT" flag : construction of the spanning tree
        // "SENSING" flag : transmission of the sensed values
        // You can use other flags for your algorithms
        switch (message.getFlag()) {
            case "INIT":
                // if not yet in the tree
                if (parent == null) {
                    // enter the tree
                    parent = message.getSender();
                    getCommonLinkWith(parent).setWidth(4);
                    // propagate further
                    sendAll(message);
                    Node node = new Node();
                    node.setLocation(this.getX(), this.getY());
                    send(parent, new Message(node, "PARENT"));
                }
                break;
            case "SENSING":
                // retransmit up the tree
                send(parent, message);
                break;
            case "PARENT":
                send(parent, message);
                break;
        }
    }

    @Override
    public void send(Node destination, Message message) {
        /**
         * Si le noeud à une batterie supérieux à 0, envoie un message de type "sensing", "parent" ou "init" voir onMessage
         * Sinon, l'exécution s'arrete et affiche la batterie et l'id du noeud qui s'est arreté.
         */
        if (battery > 0) {
            super.send(destination, message);
            battery--;
            if(battery == 0){
                System.out.println("Battery " + getTime()+" "+this.getID());
                System.exit(0);
            }
            updateColor();
        }
    }

    @Override
    public void onClock() {
        /**
         * A chaque top d'horloge, le noeud envoi une donnée.
         * Cette donnée n'est envoyé que si le nombre aléatoire généré est strictement inférieur à 0.02
         * Ce nombre aléatoire est compris entre 0 et 1.
         */
        if (parent != null) { // if already in the tree
            if (Math.random() < 0.02) { // from time to time...
                double sensedValue = Math.random(); // sense a value
                send(parent, new Message(sensedValue, "SENSING")); // send it to parent
            }
        }
    }

    protected void updateColor() {
        /**
         * MModification de la couleur du noeud en fonction de l'état de la batterie.
         */
        setColor(battery == 0 ? Color.red : new Color(255 - battery, 255 - battery, 255));
    }
}