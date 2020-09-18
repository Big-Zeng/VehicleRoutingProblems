package service;

import common.CommonMethod;
import pojo.Area;
import pojo.Car;
import pojo.OrderInformation;
import pojo.Point;

import java.util.*;

/**
 * Created by Administrator on 2020/8/13.
 */
public class ScanAlgorithm {
    double R = 10;

    public void start(List<Car> cars, List<OrderInformation> orderInformations, Area area) {
        sortCars(cars);
        OrderInformation mostLeftOrder = getLeftPoint(orderInformations, area.getVirHouseDir());
        Point depot = new Point(area.getVirHouseLon(), area.getVirHouseLat());
        Point leftPoint = new Point(mostLeftOrder.getLon(), mostLeftOrder.getLat());
        for (Car car : cars) {
            leftPoint = addSiteRightSector(leftPoint, car, depot, orderInformations);
        }
    }

    /**
     * count car load rate
     *
     * @param car
     * @return
     */
    public double countCarRate(Car car) {
        double good = 0;
        for (OrderInformation orderInformation : car.getOrderInformations()) {
            good += orderInformation.getVol();
        }
        double mark = good / car.getCarVolume();
        car.setAlreadyLoadVol(mark);
        return mark;
    }


    /**
     * scan algorithm
     *
     * @param leftPoint
     * @param car
     * @return
     */
    public Point addSiteRightSector(Point leftPoint, Car car, Point depot, List<OrderInformation> orders) {
        boolean flag = false;
        double alreadyVol = priClientVolume(car.getOrderInformations());
        Point nextPoint = null;
        double baseSita = 0.001;

        for (double sita = baseSita; alreadyVol / car.getCarVolume() < car.getDownVolLimit(); sita = sita + sita) {
            nextPoint = sureNewPoint(leftPoint, depot, sita, 0, 0);
            double angle = getTwoSideAngle(leftPoint, nextPoint, depot);
            for (int i = orders.size() - 1; i >= 0; i--) {
                OrderInformation order = orders.get(i);
                if (isInArea(leftPoint, nextPoint, angle, depot,
                        new Point(Double.valueOf(order.getLon()), Double.valueOf(order.getLat())), R)) {
                    alreadyVol += order.getVol();
                    if (alreadyVol / car.getCarVolume() <= car.getUpVolLimit()) {
                        car.getOrderInformations().add(order);
                        orders.remove(i);
                    } else {
                        flag = true;
                        break;
                    }
                }

            }
            if (flag) {
                break;
            }

        }

        return  sureNewPoint(nextPoint, depot, baseSita, 1, 0); //get former point

    }

    public double getTwoSideAngle(Point onePoint, Point twoPoint, Point centerCirclepPoint) {
        //余弦定理
        double length1 = Math.sqrt((onePoint.x - twoPoint.x) * (onePoint.x - twoPoint.x) + (onePoint.y - twoPoint.y) * (onePoint.y - twoPoint.y));
        double length2 = Math.sqrt((centerCirclepPoint.x - twoPoint.x) * (centerCirclepPoint.x - twoPoint.x) + (centerCirclepPoint.y - twoPoint.y) * (centerCirclepPoint.y - twoPoint.y));
        double length3 = Math.sqrt((onePoint.x - centerCirclepPoint.x) * (onePoint.x - centerCirclepPoint.x) + (onePoint.y - centerCirclepPoint.y) * (onePoint.y - centerCirclepPoint.y));
        double cosA = ((length2 * length2 + length3 * length3) - length1 * length1) / (2 * length2 * length3);
        double angle = Math.acos(cosA);
        return angle;
    }

    public static boolean isInArea(Point left, Point right, double sita,
                                   Point wareHouse, Point clientPoint, double R) {

        if (getTwoPointMathDis(wareHouse, clientPoint) <= R)
            if (countTwoLinesAngle(left, clientPoint, wareHouse) <= sita)
                if (countTwoLinesAngle(right, clientPoint, wareHouse) <= sita)
                    return true;
        return false;
    }

    public static double countTwoLinesAngle(Point left, Point right,
                                            Point wareHouse) {
        double L1, L2, L3;
        double x, y;

        x = Math.abs(left.x - wareHouse.x);
        y = Math.abs(left.y - wareHouse.y);
        L1 = Math.sqrt(x * x + y * y);

        x = Math.abs(right.x - wareHouse.x);
        y = Math.abs(right.y - wareHouse.y);
        L2 = Math.sqrt(x * x + y * y);

        x = Math.abs(right.x - left.x);
        y = Math.abs(right.y - left.y);
        L3 = Math.sqrt(x * x + y * y);

        double cosSita = (L1 * L1 + L2 * L2 - L3 * L3) / (2 * L1 * L2);
        double sita = Math.acos(cosSita);
        return sita;
    }

    private static double getTwoPointMathDis(Point a, Point b) {
        double x = a.x - b.x;
        double y = a.y - b.y;
        return Math.sqrt(x * x + y * y);
    }


    /**
     * 角度变化不论方位，的获取新的点
     *
     * @param keyPoint       关键点
     * @param warehousePoint 仓库点 为圆心
     * @param sita           角度
     * @param falg           1为向左 0为向右
     * @param mark           这个为自己加入的转动半径长度。可以加可以不加，不加默认为0，默认length为关键点和仓库点连线长
     * @return
     * @author zxf2015年8月16日
     */

    public Point sureNewPoint(Point keyPoint, Point warehousePoint, double sita, int falg, double mark) {
        double k1 = (keyPoint.y - warehousePoint.y) / (keyPoint.x - warehousePoint.x);
        double sita2 = Math.abs(Math.atan(k1));//r锐角
        Point point = new Point();
        double length = 0;
        if (mark == 0) {
            length = Math.sqrt((keyPoint.x - warehousePoint.x) * (keyPoint.x - warehousePoint.x) + (keyPoint.y - warehousePoint.y) * (keyPoint.y - warehousePoint.y));
        } else {
            length = mark;//可以自己加入一个变量加入半径长
        }
        double pi = Math.PI;
        //****************************************
        if (keyPoint.x == warehousePoint.x && keyPoint.y > warehousePoint.y) {//关键点和仓库原点一条线且在其上方
            if (falg == 1) {//向左
                double angle = Math.PI / 2 - sita;
                double x = warehousePoint.x - length * Math.cos(angle);
                double y = warehousePoint.y + length * Math.sin(angle);
                point = new Point(x, y);
                return point;
            }
            if (falg == 0) {//向右
                double angle = Math.PI / 2 - sita;
                double x = warehousePoint.x + length * Math.cos(angle);
                double y = warehousePoint.y + length * Math.sin(angle);
                point = new Point(x, y);
                return point;
            }
        }
        if (keyPoint.x == warehousePoint.x && keyPoint.y < warehousePoint.y) {
            if (falg == 1) {//向左为逆时针
                double angle = Math.PI / 2 - sita;
                double x = warehousePoint.x + length * Math.cos(angle);
                double y = warehousePoint.y - length * Math.sin(angle);
                point = new Point(x, y);
                return point;
            }
            if (falg == 0) {//向右即顺时针
                double angle = Math.PI / 2 - sita;
                double x = warehousePoint.x - length * Math.cos(angle);
                double y = warehousePoint.y - length * Math.sin(angle);
                point = new Point(x, y);
                return point;
            }
        }

        if (keyPoint.y == warehousePoint.y && keyPoint.x > warehousePoint.x) {
            if (falg == 1) {
                double x = warehousePoint.x + length * Math.cos(sita);
                double y = warehousePoint.y + length * Math.sin(sita);
                point = new Point(x, y);
                return point;
            }
            if (falg == 0) {
                double x = warehousePoint.x + length * Math.cos(sita);
                double y = warehousePoint.x - length * Math.sin(sita);
                point = new Point(x, y);
                return point;
            }

        }

        //上方为垂直部分
        //*********************************

        if (keyPoint.x > warehousePoint.x && keyPoint.y > warehousePoint.y) {//第一象限
            if (falg == 1) {//逆时针
                if (sita + sita2 > pi / 2) {
                    double angle = pi - sita - sita2;
                    double x = warehousePoint.x - length * Math.cos(angle);
                    double y = warehousePoint.y + length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                } else {
                    double angle = sita + sita2;
                    double y = warehousePoint.y + length * Math.cos(angle);
                    double x = warehousePoint.x + length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                }
            }
            if (falg == 0) {
                if (sita > sita2) {//sita 传入的要变化的角度
                    double angle = sita - sita2;
                    double x = warehousePoint.x + length * Math.cos(angle);
                    double y = warehousePoint.y - length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                } else {
                    double angle = sita2 - sita;
                    double x = warehousePoint.x + length * Math.cos(angle);
                    double y = warehousePoint.y + length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                }
            }
        }

        //********第一象限end*******//
        if (keyPoint.x < warehousePoint.x && keyPoint.y > warehousePoint.y) {
            if (falg == 1) {//向zuo逆时针
                if (sita > sita2) {
                    double angle = sita - sita2;
                    double x = warehousePoint.x - length * Math.cos(angle);
                    double y = warehousePoint.y - length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                } else {
                    double angle = sita2 - sita;
                    double x = warehousePoint.x - length * Math.cos(angle);
                    double y = warehousePoint.y + length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;

                }

            }
            if (falg == 0) {
                if (sita + sita2 >= pi / 2) {
                    double angle = pi - sita - sita2;
                    double x = warehousePoint.x + length * Math.cos(angle);
                    double y = warehousePoint.y + length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                } else {
                    double angle = sita + sita2;
                    double x = warehousePoint.x - length * Math.cos(angle);
                    double y = warehousePoint.y + length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                }
            }
        }
        //**************第二象限end*********************//

        if (keyPoint.x < warehousePoint.x && keyPoint.y < warehousePoint.y) {
            if (falg == 0) {//向右
                //TODO
                if (sita > sita2) {
                    double angle = (sita - sita2);
                    double y = warehousePoint.y + length * Math.sin(angle);
                    double x = warehousePoint.x - length * Math.cos(angle);
                    point = new Point(x, y);
                    return point;
                } else {
                    double angle = (sita2 - sita);
                    double y = warehousePoint.y - length * Math.sin(angle);
                    double x = warehousePoint.x - length * Math.cos(angle);
                    point = new Point(x, y);
                    return point;
                }

            }
            if (falg == 1) {
                if (sita + sita2 > Math.PI / 2) {
                    double angle = Math.PI - sita2 - sita;
                    double y = warehousePoint.y - length * Math.sin(angle);
                    double x = warehousePoint.x + length * Math.cos(angle);
                    point = new Point(x, y);
                    return point;
                } else {
                    double angle = sita2 + sita;
                    double y = warehousePoint.y - length * Math.sin(angle);
                    double x = warehousePoint.x - length * Math.cos(angle);
                    point = new Point(x, y);
                    return point;
                }
            }


        }
        //第三象限end****************************************//
        if (keyPoint.x > warehousePoint.x && keyPoint.y < warehousePoint.y) {
            if (falg == 1) {//zuo
                if (sita >= Math.PI / 2 - sita2) {
                    double angle = sita - (Math.PI / 2 - sita2);
                    double x = warehousePoint.x + length * Math.cos(angle);
                    double y = warehousePoint.y + length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                } else {
                    double angle = Math.PI / 2 - sita2 - sita;
                    double x = warehousePoint.x + length * Math.cos(angle);
                    double y = warehousePoint.y - length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                }
            }
            if (falg == 0) {
                if (sita + sita2 > (pi / 2)) {
                    double angle = pi - (sita + sita2);
                    double x = warehousePoint.x - length * Math.cos(angle);
                    double y = warehousePoint.y - length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                } else {
                    double angle = (sita2 + sita);
                    double x = warehousePoint.x + length * Math.cos(angle);
                    double y = warehousePoint.y - length * Math.sin(angle);
                    point = new Point(x, y);
                    return point;
                }

            }
        }
        //******************第四象限end********************//
        return point;
    }


    /**
     * get most left  point comparing to virtual depot
     *
     * @param orderInformations
     * @param areaDirection
     * @return
     */
    public OrderInformation getLeftPoint(List<OrderInformation> orderInformations, String areaDirection) {
        OrderInformation orderInformation = orderInformations.get(0);

        switch (areaDirection) {
            case "N":
                for (int i = 1; i < orderInformations.size(); i++) {
                    double lon = Double.valueOf(orderInformations.get(i).getLon());
                    if (lon < Double.valueOf(orderInformation.getLon())) {
                        orderInformation = orderInformations.get(i);
                    }
                }
                break;
            case "S":
                for (int i = 1; i < orderInformations.size(); i++) {
                    double lon = Double.valueOf(orderInformations.get(i).getLon());
                    if (lon > Double.valueOf(orderInformation.getLon())) {
                        orderInformation = orderInformations.get(i);
                    }
                }
                break;
            case "E":
                for (int i = 1; i < orderInformations.size(); i++) {
                    double lat = Double.valueOf(orderInformations.get(i).getLat());
                    if (lat > Double.valueOf(orderInformation.getLat())) {
                        orderInformation = orderInformations.get(i);
                    }
                }
                break;
            case "W":
                for (int i = 1; i < orderInformations.size(); i++) {
                    double lat = Double.valueOf(orderInformations.get(i).getLat());
                    if (lat < Double.valueOf(orderInformation.getLat())) {
                        orderInformation = orderInformations.get(i);
                    }
                }
                break;
        }
        return orderInformation;

    }


    /**
     * 按距离从大到小比较
     *
     * @author zxf
     */
    class sortClientByDis implements Comparator<OrderInformation> {

        @Override
        public int compare(OrderInformation o1, OrderInformation o2) {
            if (o1.getDistanceToMidline() > o2.getDistanceToMidline()) {
                return -1;
            } else if (o1.getDistanceToMidline() < o2.getDistanceToMidline()) {
                return 1;
            } else {
                return 0;
            }

        }

    }


    public void moveClientsFromSpindle(Car car, List<OrderInformation> clients) {
        List<OrderInformation> clientAllocations = new ArrayList<>();
        List<OrderInformation> otherClients = new ArrayList<>();
        int falg = 0;//用于标记另外一个客户集合的索引
        int mark = 0;//用于标记是否达到装载率
        for (int i = clients.size() - 1; i >= 0; i--) {
            clientAllocations.add(clients.get(i));
            double vol = (priClientVolume(clientAllocations) + priClientVolume(car.getOrderInformations())) / car.getCarVolume();
            if ((vol) > car.getDownVolLimit() && vol < car.getUpVolLimit()) {
                falg = i;
                mark = 1;
                continue;
            } else if (vol > car.getUpVolLimit()) {
                clientAllocations.remove(clientAllocations.size() - 1);
                otherClients.add(clients.get(i));
            }
        }
        if (mark == 0) { //说明循环无法达到装载率
            for (int i = falg - 1; i >= 0; i--) {
                otherClients.add(clients.get(i));//另外一个客户集合
            }
            double vol1 = (priClientVolume(clientAllocations) + priClientVolume(car.getOrderInformations())) / car.getCarVolume();

            if (vol1 < car.getDownVolLimit() && otherClients.size() != 0) {//进行交换点处理
                int falg1 = 0;
                for (int j = clientAllocations.size() - 1; j >= 0; j--) {
                    for (int i = 0; i < otherClients.size(); i++) {
                        double load = (priClientVolume(car.getOrderInformations()) + getVolByChangeTwoClientsPoint(clientAllocations, clientAllocations.get(j), otherClients.get(i))) / car.getCarVolume();
                        if (load > car.getDownVolLimit() && load < car.getUpVolLimit()) {
                            clientAllocations.remove(j);
                            clientAllocations.add(otherClients.get(i));
                            falg1 = 1;
                            break;
                        }
                    }
                    if (falg1 == 1) {
                        break;
                    }
                }

            }
            if (vol1 < car.getDownVolLimit()) {
                car.getOrderInformations().addAll(clientAllocations);
            }

        } else {
            car.getOrderInformations().addAll(clientAllocations);
        }

        for (int i = 0; i < clientAllocations.size(); i++) {
            clients.remove(clientAllocations.get(i));
        }
    }


    public double getVolByChangeTwoClientsPoint(List<OrderInformation> clients1, OrderInformation removeClients, OrderInformation client) {
        double vol = priClientVolume(clients1) - removeClients.getVol() + client.getVol();
        return vol;
    }

    /**
     * insert algorithm
     * from big to small
     *
     * @param cars
     * @return
     */
    public void sortCars(List<Car> cars) {

        for (int i = 1; i < cars.size(); i++) {
            Car car = cars.get(i);
            int j = i;
            while (j > 0 && cars.get(i).getCarGrade() > cars.get(j - 1).getCarGrade()) {
                j--;
            }
            cars.remove(i);
            cars.add(j, car);
        }
    }

    public double priClientVolume(List<OrderInformation> clients) {
        double clientVolume = 0;
        for (int i = 0; i < clients.size(); i++) {
            clientVolume += clients.get(i).getVol();
        }
        return clientVolume;
    }
}
