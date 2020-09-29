package service;

import pojo.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/9/26.
 */
public class JarvisMarch {



    private List<Client> points;
    private List<Client> hull;
    private static int MAX_ANGLE = 4;
    private double currentMinAngle = 0;

    public JarvisMarch(List<Client> points)  {
        this.points = points;
        this.hull = new ArrayList<Client>();

        this.calculate();
    }

    private void calculate()  {
        int firstIndex = getFirstPointIndex(this.points);
        this.hull.clear();
        this.hull.add(this.points.get(firstIndex));//向list(hull)中添加第一个点
        currentMinAngle = 0;
        for (int i = nextIndex(firstIndex, this.points); i != firstIndex; i = nextIndex(
                i, this.points)) {
            this.hull.add(this.points.get(i));
        }//向list(hull)中添加其他的点，这些点将构成一个convex hull
    }



    public void add(Client item) {
        points.add(item);

        List<Client> tmplist = new ArrayList<Client>();

        tmplist.addAll(hull);
        tmplist.add(item);

        List<Client> tmphull = new ArrayList<Client>();
        int firstIndex = getFirstPointIndex(tmplist);
        tmphull.add(tmplist.get(firstIndex));
        currentMinAngle = 0;
        for (int i = nextIndex(firstIndex, tmplist); i != firstIndex; i = nextIndex(
                i, tmplist)) {
            tmphull.add(tmplist.get(i));
        }

        this.hull = tmphull;
    }

    public void add(List<Client> items) {
        points.addAll(items);
        List<Client> tmplist = new ArrayList<Client>();

        tmplist.addAll(hull);
        tmplist.addAll(items);

        List<Client> tmphull = new ArrayList<Client>();
        int firstIndex = getFirstPointIndex(tmplist);
        tmphull.add(tmplist.get(firstIndex));
        currentMinAngle = 0;
        for (int i = nextIndex(firstIndex, tmplist); i != firstIndex; i = nextIndex(
                i, tmplist)) {
            tmphull.add(tmplist.get(i));
        }

        this.hull = tmphull;
    }

    public List<Client> getHull() {
        return this.hull;
    }

    private int nextIndex(int currentIndex, List<Client> points) {
        double minAngle = MAX_ANGLE;
        double pseudoAngle;
        int minIndex = 0;
        for (int i = 0; i < points.size(); i++) {
            if (i != currentIndex) {
                pseudoAngle = getPseudoAngle(
                        points.get(i).getX()- points.get(currentIndex).getX(),
                        points.get(i).getY() - points.get(currentIndex).getY());
                if (pseudoAngle >= currentMinAngle && pseudoAngle < minAngle) {
                    minAngle = pseudoAngle;
                    minIndex = i;
                } else if (pseudoAngle == minAngle) {
                    if ((Math.abs(points.get(i).getX()
                            - points.get(currentIndex).getX()) > Math.abs(points
                            .get(minIndex).getX() - points.get(currentIndex).getX()))
                            || (Math.abs(points.get(i).getY()
                            - points.get(currentIndex).getY()) > Math
                            .abs(points.get(minIndex).getY()
                                    - points.get(currentIndex).getY()))) {
                        minIndex = i;
                    }
                }
            }

        }
        currentMinAngle = minAngle;
        return minIndex;
    }

    //获得起始点
    private int getFirstPointIndex(List<Client> points) {
        int minIndex = 0;
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).getY()< points.get(minIndex).getY()) {
                minIndex = i;
            } else if ((points.get(i).getY() == points.get(minIndex).getY())
                    && (points.get(i).getX() < points.get(minIndex).getX())) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    private double getPseudoAngle(double dx, double dy) {
        if (dx > 0 && dy >= 0)
            return dy / (dx + dy);
        if (dx <= 0 && dy > 0)
            return 1 + (Math.abs(dx) / (Math.abs(dx) + dy));
        if (dx < 0 && dy <= 0)
            return 2 + (dy / (dx + dy));
        if (dx >= 0 && dy < 0)
            return 3 + (dx / (dx + Math.abs(dy)));
        throw new Error("Impossible");
    }


}
