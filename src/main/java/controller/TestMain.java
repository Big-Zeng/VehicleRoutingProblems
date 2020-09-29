package controller;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import common.CommonMethod;
import common.MybatisSessionUtil;
import common.RedisClient;
import dao.IAreaDao;
import dao.ICarModelDao;
import dao.IOrderInformationDao;
import org.apache.ibatis.session.SqlSession;
import pojo.*;
import service.BasedCostVehiclePlanning;
import service.CaculateVehicle;
import service.GuriboVRP;
import service.HVRPMainInterface;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static common.CommonMethod.getClients;
import static common.CommonMethod.getVehicles;

/**
 * Created by Administrator on 2020/9/3.
 */
public class TestMain {
    private static RedisClient redisClient = new RedisClient();
    public static final SqlSession session = MybatisSessionUtil.getSession();

    public static void main(String[] args) {
        ICarModelDao iCarModelDao = session.getMapper(ICarModelDao.class);
        IOrderInformationDao iOrderInformationDao = session.getMapper(IOrderInformationDao.class);
        IAreaDao iAreaDao = session.getMapper(IAreaDao.class);
        //430103 天心区
        //430181 浏阳县
        String waveId = "201702050034";
        String areaId = "430421";


        List<OrderInformation> orderInformations
                = iOrderInformationDao.selectByAreaIdAndWaveId(areaId,waveId);
        List<CarModel> carModels
                = iCarModelDao.selectAllRemandCars();


       // ArrayList<Client> clients = getClients(orderInformations);
       // ArrayList<Vehicle> vehicles = getVehicles(carModels);

       // Client subDepot = getSubAreaDepot(area);
       // Client depot = getDeopt();

        //CaculateVehicle caculateVehicle = new BasedCostVehiclePlanning();
        //caculateVehicle.getCaculatedVehicle(clients, vehicles, subDepot, depot);


      cacluateHVRP(orderInformations, carModels);

      //  CaculateVehicle caculateVehicle = new BasedCostVehiclePlanning();
     //   caculateVehicle.getCaculatedVehicle(orderInformations, carModels);
       /* int[][] dis = redisClient.getPathByAreaAndOrders(orderInformations);

        orderInformations.add(new OrderInformation(BigDecimal.valueOf(0.0), "27.902447", "113.003699", 0));

        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();

        setVRPClients(orderInformations, vrpBuilder);
        VehicleRoutingTransportCostsMatrix.Builder matrix = setDis(dis);
        VehicleRoutingTransportCosts costMatrix = matrix.build();

        setYuHuPaperVehicle(carModels, vrpBuilder, orderInformations.size() - 1);
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);
        vrpBuilder.setRoutingCost(costMatrix);
        VehicleRoutingProblem vrp = vrpBuilder.build();

        VehicleRoutingAlgorithm vra = Jsprit.createAlgorithm(vrp);

        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();
        VehicleRoutingProblemSolution best = Solutions.bestOf(solutions);
        SolutionPrinter.print(vrp, best, SolutionPrinter.Print.VERBOSE);*/
        //  new GraphStreamViewer(vrp, best).setRenderDelay(100).display();


        /*************************not redis**********************/



    }




    public static void cacluateHVRP(List<OrderInformation> orderInformations, List<CarModel> carModels) {
        ArrayList<Client> clients = new ArrayList<>();
        clients.add(CommonMethod. getDeopt());
        clients.addAll(getClients(orderInformations));

        ArrayList<Vehicle> vehicles = getVehicles(carModels);

       // double[][] dis = redisClient.getPathByAreaAndOrders(clients, orderInformations.get(0).getAreaId());

        double[][] dis = CommonMethod.getDis(clients);
        HVRPMainInterface hvrpMainInterface = new GuriboVRP();
        try {
            hvrpMainInterface.HVRPSolution(clients,vehicles,dis);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    private static ArrayList<Vehicle> getPaperVehicles(ArrayList<Vehicle> vehicles) {
        ArrayList<Vehicle> vehicleList = new ArrayList<>();



        Vehicle vehicle1 = vehicles.get(2);
        vehicle1.setSize(5);

       // vehicleList.add(vehicle);
        vehicleList.add(vehicle1);
        return vehicleList;
    }



    public static void setVRPClients(List<OrderInformation> orderInformations, VehicleRoutingProblem.Builder vrpBuilder) {
        for (int i = 0; i < orderInformations.size(); i++) {
            OrderInformation orderInformation = orderInformations.get(i);
            vrpBuilder.addJob(Service.Builder.newInstance(String.valueOf(i))
                    .addSizeDimension(0, (int) (orderInformation.getTurnoverVol()))
                    .setLocation(Location.newInstance(String.valueOf(i))).build());
        }
    }


    public static VehicleRoutingTransportCostsMatrix.Builder setDis(int[][] dis) {
        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);
        for (int i = 0; i < dis.length; i++) {
            for (int i1 = 0; i1 < dis[i].length; i1++) {
                costMatrixBuilder.addTransportDistance(String.valueOf(i), String.valueOf(i1), dis[i][i1]);
            }
        }
        return costMatrixBuilder;
    }


    public static void setVehicles(List<CarModel> carModels, VehicleRoutingProblem.Builder vrpBuilder, int orderLength) {
        for (CarModel carModel : carModels) {
            VehicleType type = VehicleTypeImpl.Builder
                    .newInstance(carModel.getCarTypeId()).addCapacityDimension(0, (int) carModel.getVol() * 1000)
                    .setCostPerDistance(Double.valueOf(carModel.getkCost())).build();
            VehicleImpl vehicle = VehicleImpl.Builder.newInstance(carModel.getCarTypeId() + "_")
                    .setStartLocation(Location.newInstance(orderLength)).setType(type).build();
            vrpBuilder.addVehicle(vehicle);
        }
    }


    public static void setPaperVehicle(List<CarModel> carModels, VehicleRoutingProblem.Builder vrpBuilder, int orderLength) {

        VehicleType type3 = VehicleTypeImpl.Builder
                .newInstance("2车").addCapacityDimension(0, (int) 10 * 1000)
                .setCostPerDistance(Double.valueOf(0.1067)).build();

        VehicleType type = VehicleTypeImpl.Builder
                .newInstance("4车").addCapacityDimension(0, (int) 26 * 1000)
                .setCostPerDistance(Double.valueOf(0.1692)).build();
        VehicleType type2 = VehicleTypeImpl.Builder
                .newInstance("5车").addCapacityDimension(0, (int) 35 * 1000)
                .setCostPerDistance(Double.valueOf(0.2115)).build();
        VehicleImpl vehicle = VehicleImpl.Builder.newInstance("2车_1")
                .setStartLocation(Location.newInstance(orderLength)).setType(type3).build();
     /*   VehicleImpl vehicle2 = VehicleImpl.Builder.newInstance("4车_2")
                .setStartLocation(Location.newInstance(orderLength)).setType(type).build();*/


        VehicleImpl vehicle3 = VehicleImpl.Builder.newInstance("5车_1")
                .setStartLocation(Location.newInstance(orderLength)).setType(type2).build();
        VehicleImpl vehicle4 = VehicleImpl.Builder.newInstance("5车_2")
                .setStartLocation(Location.newInstance(orderLength)).setType(type2).build();
        VehicleImpl vehicle5 = VehicleImpl.Builder.newInstance("5车_3")
                .setStartLocation(Location.newInstance(orderLength)).setType(type2).build();
        VehicleImpl vehicle6 = VehicleImpl.Builder.newInstance("5车_4")
                .setStartLocation(Location.newInstance(orderLength)).setType(type2).build();
        VehicleImpl vehicle7 = VehicleImpl.Builder.newInstance("5车_5")
                .setStartLocation(Location.newInstance(orderLength)).setType(type2).build();
        VehicleImpl vehicle8 = VehicleImpl.Builder.newInstance("5车_6")
                .setStartLocation(Location.newInstance(orderLength)).setType(type2).build();

        vrpBuilder.addVehicle(vehicle);
        // vrpBuilder.addVehicle(vehicle2);
        vrpBuilder.addVehicle(vehicle3);
        vrpBuilder.addVehicle(vehicle4);
        vrpBuilder.addVehicle(vehicle5);
        vrpBuilder.addVehicle(vehicle6);
        vrpBuilder.addVehicle(vehicle7);
        vrpBuilder.addVehicle(vehicle8);
    }


    public static void setYuHuPaperVehicle(List<CarModel> carModels, VehicleRoutingProblem.Builder vrpBuilder, int orderLength) {

        VehicleType type3 = VehicleTypeImpl.Builder
                .newInstance("1车").addCapacityDimension(0, (int) 6 * 1000)
                .setCostPerDistance(Double.valueOf(0.0850)).build();
        VehicleType type = VehicleTypeImpl.Builder
                .newInstance("4车").addCapacityDimension(0, (int) 26 * 1000)
                .setCostPerDistance(Double.valueOf(0.1692)).build();

        VehicleImpl vehicle = VehicleImpl.Builder.newInstance("1车_1")
                .setStartLocation(Location.newInstance(orderLength)).setType(type3).build();
     /*   VehicleImpl vehicle2 = VehicleImpl.Builder.newInstance("4车_2")
                .setStartLocation(Location.newInstance(orderLength)).setType(type).build();*/


        VehicleImpl vehicle3 = VehicleImpl.Builder.newInstance("4车_1")
                .setStartLocation(Location.newInstance(orderLength)).setType(type).build();
        VehicleImpl vehicle4 = VehicleImpl.Builder.newInstance("4车_2")
                .setStartLocation(Location.newInstance(orderLength)).setType(type).build();
        VehicleImpl vehicle5 = VehicleImpl.Builder.newInstance("4车_3")
                .setStartLocation(Location.newInstance(orderLength)).setType(type).build();
        VehicleImpl vehicle6 = VehicleImpl.Builder.newInstance("4车_4")
                .setStartLocation(Location.newInstance(orderLength)).setType(type).build();
        VehicleImpl vehicle7 = VehicleImpl.Builder.newInstance("4车_5")
                .setStartLocation(Location.newInstance(orderLength)).setType(type).build();
        VehicleImpl vehicle8 = VehicleImpl.Builder.newInstance("4车_6")
                .setStartLocation(Location.newInstance(orderLength)).setType(type).build();

        vrpBuilder.addVehicle(vehicle);
        // vrpBuilder.addVehicle(vehicle2);
        vrpBuilder.addVehicle(vehicle3);
        vrpBuilder.addVehicle(vehicle4);
        vrpBuilder.addVehicle(vehicle5);
        vrpBuilder.addVehicle(vehicle6);
        vrpBuilder.addVehicle(vehicle7);
        vrpBuilder.addVehicle(vehicle8);
    }


}
