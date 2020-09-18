package service;

import gurobi.*;
import pojo.Car;
import pojo.CarModel;
import pojo.OrderInformation;

import java.util.List;

/**
 * Created by Administrator on 2020/9/3.
 */
public class BasedCostVehiclePlanning implements CaculateVehicle {


    @Override
    public List<Car> getCaculatedVehicle(List<OrderInformation> orderInformations, List<CarModel> carModels) {
        double pointVols = getOrdersVol(orderInformations);


        cuculateBasedCost(218.663, carModels, 31260, 82199.9961585463, 99);

        return null;
    }

    /**
     *
     * @param orderInformations
     * @return
     */
    private double getOrdersVol(List<OrderInformation> orderInformations) {
        double vol = 0;
        for (OrderInformation orderInformation : orderInformations) {
            vol += orderInformation.getVol();
        }
        return vol;
    }


    /*private double getT(List<OrderInformation> orderInformations) {
        List<Double> lats = new ArrayList<>();
        List<Double> lons = new ArrayList<>();
        List<Double> stdValueLon = new ArrayList<>();
        List<Double> stdValueLat = new ArrayList<>();
        double midPointlon = 0;
        double minPointlat = 0;

        for (OrderInformation orderInformation : orderInformations) {
            midPointlon += Double.valueOf(orderInformation.getLon());
            minPointlat += Double.valueOf(orderInformation.getLat());
            lats.add(Double.valueOf(orderInformation.getLon()));
            lons.add(Double.valueOf(orderInformation.getLat()));
        }
        midPointlon = midPointlon / orderInformations.size();
        minPointlat = minPointlat / orderInformations.size();

        for (int i = 0; i < lats.size(); i++) {
            stdValueLat.set(i, lats.get(i) - minPointlat);
            stdValueLon.set(i, lons.get(i) - midPointlon);
        }

        double stdDevY = CommonMethod.standardDiviation(lats);
        double stdDevX = CommonMethod.standardDiviation(lons);


       double CstdDevX = CommonMethod.standardDiviation(stdValueLon);
        double CstdDevY = CommonMethod.standardDiviation(stdValueLat);

    }*/






    /**
     *  main
     * @param pointVols
     * @param carModels
     * @param L1
     * @param T
     * @param pointN
     */
    public void cuculateBasedCost(double pointVols, List<CarModel> carModels, double L1, double T, int pointN) {
        GRBEnv env = null;
        carModels = sortCars(carModels);
        //可以在这里增加装载率
        try {
            env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            GRBVar car1 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.INTEGER, "1车");
            GRBVar car2 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.INTEGER, "2车");
            GRBVar car3 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.INTEGER, "3车");
            GRBVar car4 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.INTEGER, "4车");
            GRBVar car5 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.INTEGER, "5车");
            GRBVar car6 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.INTEGER, "6车");
            GRBVar car7 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.INTEGER, "7车");

            //add constraints

            GRBLinExpr expr = new GRBLinExpr();
            expr.addTerm(Double.valueOf(carModels.get(0).getVol()), car1);
            expr.addTerm(Double.valueOf(carModels.get(1).getVol()), car2);
            expr.addTerm(Double.valueOf(carModels.get(2).getVol()), car3);
            expr.addTerm(Double.valueOf(carModels.get(3).getVol()), car4);
            expr.addTerm(Double.valueOf(carModels.get(4).getVol()), car5);
            expr.addTerm(Double.valueOf(carModels.get(5).getVol()), car6);
            expr.addTerm(Double.valueOf(carModels.get(6).getVol()), car7);

            model.addConstr(expr, GRB.GREATER_EQUAL, pointVols, "c0"); //可以考虑需要加上冗余量


            expr = new GRBLinExpr();

            double value1 = Double.valueOf(carModels.get(0).getkCost()) * (2 * L1 + Math.floor(carModels.get(0).getVol() * pointN / pointVols) * (T / pointN));
            double value2 = Double.valueOf(carModels.get(1).getkCost()) * ((2 * L1 + Math.floor(carModels.get(1).getVol() * pointN / pointVols) * (T / pointN)));
            double value3 = Double.valueOf(carModels.get(2).getkCost()) * ((2 * L1 + Math.floor(carModels.get(2).getVol() * pointN / pointVols) * (T / pointN)));
            double value4 = Double.valueOf(carModels.get(3).getkCost()) * ((2 * L1 + Math.floor(carModels.get(3).getVol() * pointN / pointVols) * (T / pointN)));
            double value5 = Double.valueOf(carModels.get(4).getkCost()) * ((2 * L1 + Math.floor(carModels.get(4).getVol() * pointN / pointVols) * (T / pointN)));
            double value6 = Double.valueOf(carModels.get(5).getkCost()) * ((2 * L1 + Math.floor(carModels.get(5).getVol() * pointN / pointVols) * (T / pointN)));
            double value7 = Double.valueOf(carModels.get(6).getkCost()) * ((2 * L1 + Math.floor(carModels.get(6).getVol() * pointN / pointVols) * (T / pointN)));


            expr.addTerm(value1, car1);
            expr.addTerm(value2, car2);
            expr.addTerm(value3, car3);
            expr.addTerm(value4, car4);
            expr.addTerm(value5, car5);
            expr.addTerm(value6, car6);
            expr.addTerm(value7, car7);

            model.setObjective(expr, GRB.MINIMIZE);
            model.optimize();

            System.out.println(car1.get(GRB.StringAttr.VarName)
                    + " " + car1.get(GRB.DoubleAttr.X));
            System.out.println(car2.get(GRB.StringAttr.VarName)
                    + " " + car2.get(GRB.DoubleAttr.X));
            System.out.println(car3.get(GRB.StringAttr.VarName)
                    + " " + car3.get(GRB.DoubleAttr.X));
            System.out.println(car4.get(GRB.StringAttr.VarName)
                    + " " + car4.get(GRB.DoubleAttr.X));
            System.out.println(car5.get(GRB.StringAttr.VarName)
                    + " " + car5.get(GRB.DoubleAttr.X));
            System.out.println(car6.get(GRB.StringAttr.VarName)
                    + " " + car6.get(GRB.DoubleAttr.X));
            System.out.println(car7.get(GRB.StringAttr.VarName)
                    + " " + car7.get(GRB.DoubleAttr.X));


        } catch (GRBException e) {
            e.printStackTrace();
        }


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
