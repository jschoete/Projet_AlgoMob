import jbotsim.Node;
import java.awt.geom.Point2D;
import java.util.*;

public class WaypointNode extends Node{

    Queue<Point2D> destinations = new LinkedList<>();
    double speed = 1;

    double base_x;
    double base_y;

    public void setBase(double x, double y){
        base_x = x;
        base_y = y;
    }

    public void addDestination(double x, double y){
       destinations.add(new Point2D.Double(x, y));
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void onClock() {
        if (!destinations.isEmpty()) {
            Point2D dest = destinations.peek();
            if (distance(dest) > speed) {
                setDirection(dest);
                move(speed);
            } else {
                setLocation(dest);
                //destinations.poll();
                onArrival();
            }
        }

    }


    public void onArrival(){ // to be overridden
    }

}