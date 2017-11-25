import jbotsim.Node;

public class Robot extends WaypointNode {
    int idR;

    public void setIdRobot(int id) {
        this.idR = id;
    }
    public int getIdRobot() {
        return idR;
    }
    @Override
    public void onStart() {
        setIcon("src/robot.png"); // to be adapted
        setSensingRange(30);
        //addDestination(base_x, base_y);
        addDestination(Math.random()*600, Math.random()*400);
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

    }
}