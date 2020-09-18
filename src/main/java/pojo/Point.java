package pojo;

/**
 * Created by Administrator on 2020/8/13.
 */
public class Point {

    public double x;//longitude
    public double y;//latitude
    public Point() {

    }
    public Point(double x,double y) {
        this.x = x;
        this.y = y;
    }
    public Point(String  x,String y) {
        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
    }
}
