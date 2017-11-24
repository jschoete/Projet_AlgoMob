import jbotsim.Message;
import jbotsim.Node;
import jbotsim.event.ClockListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BaseStation extends Node implements ClockListener {
    int minBattery = 70;
    int default_clock = 200;
    int top_clock;
    Boolean setHorizontal = true;
    ArrayList<BatteryState> sensorLowBattery;
    ArrayList<Node> listNode1;
    ArrayList<Node> listNode2;

    Robot[] listRobot = null;

    @Override
    public void onStart() {
        setIcon("src/server.png"); // to be adapted
        setSize(12);

        // Initiates tree construction with an empty message
        sendAll(new Message(null, "INIT"));
        top_clock = 0;
        sensorLowBattery = new ArrayList<>();
        robotToBase(default_clock);
    }

    @Override
    public void onClock() {
        if(++top_clock == default_clock || top_clock == 0) {
            top_clock = default_clock;

            System.out.println("Bat init");
            sendAll(new Message(null, "BAT_INIT"));

            System.out.println("detection type");
            detectLocationType();

            System.out.println("Sort destination");
            sortDependingLocation();

            System.out.println("Send destination");
            sendLocationToRobot();


            top_clock = 1;
        }
    }

    public void addRobot(Robot[] robots) {
        listRobot = robots;
    }

    @Override
    public void onMessage(Message message) {
        if(message.getFlag().equals("BAT")){
            BatteryState batMSG = (BatteryState) message.getContent();
            if(batMSG.getBattery() <= minBattery)
                sensorLowBattery.add(batMSG);
        }
    }

    public void detectLocationType(){
        for (int i = 0; i < sensorLowBattery.size(); i++) {
            double x = sensorLowBattery.get(i).getX();
            double y = sensorLowBattery.get(i).getY();
            if(x - y >= 0)
                setHorizontal = false;
            else{
                setHorizontal = true;
            }
        }
    }

    public void sortDependingLocation(){
        if(setHorizontal){
            Collections.sort(sensorLowBattery, new Comparator<BatteryState>() {
                @Override
                public int compare(BatteryState b1, BatteryState b2) {
                    return Double.compare(b1.getY(), b2.getY());
                }
            });
        }
        else{
            Collections.sort(sensorLowBattery, new Comparator<BatteryState>() {
                @Override
                public int compare(BatteryState b1, BatteryState b2) {
                    return Double.compare(b1.getX(), b2.getX());
                }
            });
        }
    }


    public void robotToBase(int default_clock){
        send(listRobot[0], new Message(default_clock,"BASE"));
        send(listRobot[1], new Message(default_clock,"BASE"));
    }

    public void sendLocationToRobot(){
        int size = sensorLowBattery.size();
        Node node = new Node();
        for (int i = 0; i < size; i++) {
            node.setLocation(sensorLowBattery.get(i).getX(),sensorLowBattery.get(i).getY());
            if(i <= size/2)
                listNode1.add(node);
            else
                listNode2.add(node);
        }
        send(listRobot[0], new Message(listNode1, "GOTO"));
        send(listRobot[0], new Message(listNode2, "GOTO"));
        sensorLowBattery.clear();
    }
}