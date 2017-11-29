public class BatteryState {
    double x;
    double y;
    int nb_children;

    public BatteryState(double x, double y, int children){
        this.x = x;
        this.y = y;
        this.nb_children = children;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getBattery(){
        return nb_children;
    }

}
