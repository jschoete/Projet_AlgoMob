import jbotsim.Node;
import jbotsim.event.ClockListener;
import java.awt.geom.Point2D;
import java.util.*;

public class WaypointNode extends Node implements ClockListener{
    int time = 0;
    Queue<Point2D> destinations = new LinkedList<>();
    double speed = 1;
    double base_x;
    double base_y;
    int range = 30;

    public int getRange() {
        /**
         * Retourne la distance auquel un robot peut capter une information.
         */
        return range;
    }

    public void setBase(double x, double y){
        /**
         * Enregistre la position de la base
         */
        base_x = x;
        base_y = y;
    }

    public void addDestination(double x, double y){
        /**
         * Ajout de destination, appelé au niveau de onArrival et du onStart (du robot) pour rajouter la base.
         */
       destinations.add(new Point2D.Double(x, y));
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void onClock() {
        /**
         * A chaque top d'horloge, le robot va tester sa position par rapport à sa destination.
         * Si le noeud est compris dans sa zone de détecter, il le retire de sa liste de destination et prend la suivante
         * Sinon, il avance avec un pas de speed
         */
        if (!destinations.isEmpty()) {
            Point2D dest = destinations.peek();
            if (distance(dest) - (range - 3)  > speed) {
                setDirection(dest);
                move(speed);
            } else {
                onArrival();
            }
        }
        time++;
    }


    public void onArrival(){ // to be overridden
        /**
         * Modifié dans la classe Robot
         */
    }

}