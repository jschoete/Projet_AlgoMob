import jbotsim.LinkResolver;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

public class Main {
    public static void main(String[] args) {
        // Create topology with clock not started
        Topology tp = new Topology(false);

        // Forbid communication between robots and sensors
        tp.setLinkResolver(new LinkResolver(){
            @Override
            public boolean isHeardBy(Node n1, Node n2) {
                if ((n1 instanceof Robot && n2 instanceof Sensor) ||
                        (n1 instanceof Sensor && n2 instanceof Robot))
                    return false;
                else
                    return super.isHeardBy(n1, n2);
            }
        });

        // Add sensors
        tp.setDefaultNodeModel(Sensor.class);
        tp.fromFile("src/sensors.tp"); // to be adapted

        // Add base station
        BaseStation baseStation = new BaseStation();
        tp.addNode(100, 80, baseStation);

        // Add two robots
        Robot robot1 = new Robot();
        Robot robot2 = new Robot();

        robot1.setBase(baseStation);
        robot2.setBase(baseStation);

        tp.addNode(90, 40, robot1);
        tp.addNode(60, 80, robot2);

        new JViewer(tp);

        tp.setClockSpeed(0);
        tp.start(); // starts the clock
    }
}