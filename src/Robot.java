import jbotsim.Node;

public class Robot extends WaypointNode {

    @Override
    public void onStart() {
        setIcon("src/robot.png"); // to be adapted
        setSensingRange(30);
        //addDestination(Math.random()*600, Math.random()*400);
        onArrival();
    }

    @Override
    public void onSensingIn(Node node) {
       if (node instanceof Sensor) {
            ((Sensor) node).battery = 255;
       }
    }

    @Override
    public void onArrival() {
        System.out.println("OnArrival");
    }


}