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
        return range;
    }

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
    }

}