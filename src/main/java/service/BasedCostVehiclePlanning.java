package service;

import common.CommonMethod;
import gurobi.*;
import pojo.*;
import uk.co.geolib.geolib.C2DPoint;
import uk.co.geolib.geopolygons.C2DPolygon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于成本分车
 * Created by Administrator on 2020/9/3.
 */
public class BasedCostVehiclePlanning implements CaculateVehicle {


    @Override
    public List<Vehicle> getCaculatedVehicle(ArrayList<Client> clients, ArrayList<Vehicle> vehicles,Client subareaDepot, Client depot) {
        double pointVols = getOrdersVol(clients);

       clients.add(0, subareaDepot); //将分区中心添加到客户集合中

        JarvisMarch convexHull = new JarvisMarch(clients);
        List<Client> convexHullPoints = convexHull.getHull();

        double areas = getArea(convexHullPoints);
        double[] means = getmeans(clients);
        double[] stdDev = getStd(clients, means);
        double[] CstdDev = getstdDev(clients, means);

        double t = getT(clients.size(), CstdDev[0], CstdDev[1], stdDev[1], stdDev[0], areas, means[0], means[1]);
        //cuculateBasedCost(218.663, carModels, 31260, 82199.9961585463, 99);
        double L1 = getL1(subareaDepot, depot);
        getCacualteVehicle(vehicles, clients.size(), pointVols, L1,t);

        return null;
    }


    public double getArea(List<Client> clients) {
        ArrayList<C2DPoint> c2DPoints = new ArrayList<>();

        for (Client client : clients) {
            c2DPoints.add(new C2DPoint(client.getX(), client.getY()));
        }
        C2DPolygon c2DPolygon = new C2DPolygon(c2DPoints, true);

        return c2DPolygon.GetArea();
    }


    private double getL1(Client subArea, Client depot) {
        return CommonMethod.lineSpace(depot.getX(), depot.getY(), subArea.getX(), subArea.getY());

    }


    private void getCacualteVehicle(List<Vehicle> vehicles,int n,double pointVols,double L1,double T) {
        GRBEnv env = null;
        try {
            env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            GRBVar[] x = new GRBVar[vehicles.size()];
            for (int i = 0; i < vehicles.size(); i++) {
                x[i] = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.INTEGER, String.valueOf(vehicles.get(i).getId()));
            }

            //添加约束

            GRBLinExpr constraint1 = new GRBLinExpr();
            for (int i = 0; i < vehicles.size(); i++) {
                constraint1.addTerm(vehicles.get(i).getCapacity(), x[i]);
            }
            model.addConstr(constraint1, GRB.GREATER_EQUAL, pointVols, "Constraint1");
            //constraint2

            GRBLinExpr obj2 = new GRBLinExpr();
            for (int i = 0; i < vehicles.size(); i++) {
                double termValue = vehicles.get(i).getmCost()*(2*L1+Math.floor(vehicles.get(i).getCapacity()*n/pointVols)*(T/n));
                obj2.addTerm(termValue,x[i]);

            }

            model.setObjective(obj2, GRB.MINIMIZE);
            model.update();

            model.optimize();
            System.out.println("is the solution feasible? " + model.get(GRB.IntAttr.Status));

            for (int i = 0; i < vehicles.size(); i++) {
                System.out.println(vehicles.get(i).getVehicleName() + "车数量：" + x[i].get(GRB.DoubleAttr.X));
            }

        } catch (GRBException e) {
            e.printStackTrace();
        }
    }



    /**
     * 平均值
     * @param clients
     * @return
     */
    private double[] getmeans(List<Client> clients) {

        double meanX = 0;
        double meanY = 0;

        for (Client client : clients) {
            meanX += client.getX();
            meanY += client.getY();
        }

        meanX = meanX / clients.size();
        meanY = meanY / clients.size();
        return new double[]{meanX, meanY};
    }

    private double getmeans(double[] datas) {
        double mean = 0;
        for (double data : datas) {
            mean += data;
        }
        return mean / datas.length;

    }


    private double[] getStd(List<Client> clients, double[] means) {
        double dvarX = 0;
        double dvarY = 0;
        for (Client client : clients) {
            dvarX += Math.pow(client.getX() - means[0], 2);
            dvarY += Math.pow(client.getY() - means[1], 2);
        }
        double stdX = Math.sqrt(dvarX / clients.size());
        double stdY = Math.sqrt(dvarY / clients.size());

        return new double[]{stdX, stdY};

    }

    private double getStd(double[] values,double mean){
        double std = 0;
        for (double value : values) {
            std += Math.pow(value - mean, 2);
        }
        return Math.sqrt(std / values.length);

    }

    private double[] getstdDev(List<Client> clients, double[] means) {
        double[] stdValueX = new double[clients.size()];
        double[] stdValueY = new double[clients.size()];


        for (int i = 0; i < clients.size(); i++) {
            stdValueX[i] = Math.abs(clients.get(i).getX() - means[0]);
            stdValueY[i] =Math.abs( clients.get(i).getY() - means[1]);
        }

        double stdValuemeanX = getmeans(stdValueX);
        double stdValuemeanY = getmeans(stdValueY);

        double CstdDevX = getStd(stdValueX, stdValuemeanX);
        double CstdDevY = getStd(stdValueY, stdValuemeanY);

        return new double[]{CstdDevX, CstdDevY};
    }






    public static double area(List<Client> polyPoints) {
        int i, j, n = polyPoints.size();
        double area = 0;

        for (i = 0; i < n; i++) {
            j = (i + 1) % n;
            area += polyPoints.get(i).getX() * polyPoints.get(j).getY();
            area -= polyPoints.get(j).getX() * polyPoints.get(i).getY();
        }
        area /= 2.0;
        return (area);
    }


    /**
     * A distribution-free TSP tour length estimation model for random graphs
     * @param n
     * @param CstdDevX
     * @param CstdDevY
     * @param stdDevY
     * @param stdDevX
     * @param areas
     * @param midPointlon
     * @param minPointlat
     * @return
     */
    private double getT(int n, double CstdDevX, double CstdDevY, double stdDevY,
                        double stdDevX, double areas, double midPointlon, double minPointlat) {

        return   2.791*Math.sqrt(n*(CstdDevX*CstdDevY))+0.2669*Math.sqrt(n*(stdDevX*stdDevY)*(areas/(midPointlon*minPointlat)));

    }


    /**
     *
     * @param clients
     * @return
     */
    private double getOrdersVol(List<Client> clients) {
        double vol = 0;
        for (Client client : clients) {
            vol += client.getDemand();
        }
        return vol;
    }




    /**
     * insert sort
     *
     * @param cars
     * @return
     */
    public static List<CarModel> sortCars(List<CarModel> cars) {
        for (int i = 1; i < cars.size(); i++) {
            CarModel car = cars.get(i);
            int j = i;
            while (j > 0 && cars.get(i).getVol() < cars.get(j - 1).getVol()) {
                j--;
            }
            cars.remove(i);
            cars.add(j, car);

        }
        return cars;

    }


}
