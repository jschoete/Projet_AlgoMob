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
    BaseStation base;

    public void setBase(BaseStation b){
        base_x = b.getX();
        base_y = b.getY();
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
        time++;
    }
    public void onArrival(){ // to be overridden
    }
}