import java.awt.*;
import java.awt.geom.Point2D;

public class BatteryState {
    double x;
    double y;
    int battery;

    public BatteryState(double x, double y, int battery){
        this.x = x;
        this.y = y;
        this.battery = battery;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getBattery(){
        return battery;
    }
}
