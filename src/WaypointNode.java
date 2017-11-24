import jbotsim.Message;
import jbotsim.Node;
import jbotsim.event.ClockListener;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class WaypointNode extends Node implements ClockListener {
    Queue<Point2D> destinations = new LinkedList<>();
    double speed = 1;


    int default_clock;
    int top_clock = 0;

    Node base;

    public void addDestination(double x, double y){
        destinations.add(new Point2D.Double(x, y));
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void onClock() {
        if(++top_clock == default_clock)
            addDestination(base.getX(), base.getY());
        if (!destinations.isEmpty()) {
            Point2D dest = destinations.peek();
            if (distance(dest) > speed) {
                setDirection(dest);
                move(speed);
            } else {
                setLocation(dest);
                destinations.poll();
                onArrival();
            }
        }
    }

    @Override
    public void onMessage(Message message) {
        if (message.getFlag().equals("BASE")){
            default_clock = (int) message.getContent();
            base = message.getSender();
        }
        else if (message.getFlag().equals("GOTO")){
            ArrayList<BatteryState> batteryState = (ArrayList<BatteryState>) message.getContent();
            if(!batteryState.isEmpty())
                addList(batteryState);
        }
    }

    public void addList(ArrayList<BatteryState> list){
        while(!list.isEmpty()) {
            BatteryState b = list.remove(0);
            this.addDestination(b.getX(), b.getY());
        }
    }

    public void onArrival(){ // to be overridden
    }

}