package controller;

import com.graphhopper.jsprit.analysis.toolbox.GraphStreamViewer;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;

/**
 * Created by Administrator on 2020/9/1.
 */
public class JSPRitVRP {


    //ATSP
/*    public static void main(String[] args) {

        VehicleType type = VehicleTypeImpl.Builder.newInstance("type").addCapacityDimension(0, 2).setCostPerDistance(1).build();
        VehicleImpl vehicle = VehicleImpl.Builder.newInstance("vehicle")
                .setStartLocation(Location.newInstance("0")).setType(type).build();

        Service s1 = Service.Builder.newInstance("1").addSizeDimension(0, 1).setLocation(Location.newInstance("1")).build();
        Service s2 = Service.Builder.newInstance("2").addSizeDimension(0, 1).setLocation(Location.newInstance("2")).build();
        Service s3 = Service.Builder.newInstance("3").addSizeDimension(0, 1).setLocation(Location.newInstance("3")).build();


		*//*
         * Assume the following symmetric distance-matrix
		 * from,to,distance
		 * 0,1,10.0
		 * 0,2,20.0
		 * 0,3,5.0
		 * 1,2,4.0
		 * 1,3,1.0
		 * 2,3,2.0
		 *
		 * and this time-matrix
		 * 0,1,5.0
		 * 0,2,10.0
		 * 0,3,2.5
		 * 1,2,2.0
		 * 1,3,0.5
		 * 2,3,1.0
		 *//*
        //define a matrix-builder building a symmetric matrix
        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);
        costMatrixBuilder.addTransportDistance("0", "1", 10.0);
        costMatrixBuilder.addTransportDistance("0", "2", 20.0);
        costMatrixBuilder.addTransportDistance("0", "3", 5.0);
        costMatrixBuilder.addTransportDistance("1", "2", 4.0);
        costMatrixBuilder.addTransportDistance("1", "3", 1.0);
        costMatrixBuilder.addTransportDistance("2", "3", 2.0);

        costMatrixBuilder.addTransportTime("0", "1", 10.0);
        costMatrixBuilder.addTransportTime("0", "2", 20.0);
        costMatrixBuilder.addTransportTime("0", "3", 5.0);
        costMatrixBuilder.addTransportTime("1", "2", 4.0);
        costMatrixBuilder.addTransportTime("1", "3", 1.0);
        costMatrixBuilder.addTransportTime("2", "3", 2.0);

        VehicleRoutingTransportCosts costMatrix = costMatrixBuilder.build();

        VehicleRoutingProblem vrp = VehicleRoutingProblem.Builder.newInstance().setFleetSize(VehicleRoutingProblem.FleetSize.INFINITE).setRoutingCost(costMatrix)
                .addVehicle(vehicle).addJob(s1).addJob(s2).addJob(s3).build();

        VehicleRoutingAlgorithm vra = Jsprit.createAlgorithm(vrp);

        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();
        VehicleRoutingProblemSolution best = Solutions.bestOf(solutions);
        SolutionPrinter.print(vrp, best, SolutionPrinter.Print.VERBOSE);
        new GraphStreamViewer(vrp, best).setRenderDelay(100).display();
    }*/


    /**
     * 通过FileChannel从指定的文件中读取数据(按行读取)
     *
     * @param filePath 文件路径
     */
    public static void  readFileByIO(String filePath,VehicleRoutingProblem.Builder builder) {
        FileChannel fcIn = null;
        FileInputStream fInputStream = null;


        try {
            fInputStream = new FileInputStream(new File(filePath));
            fcIn = fInputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int length = -1;

            boolean carsFlag = false;
            int carTypeNum = 1;

            while ((length = fcIn.read(buffer)) != -1) {
                buffer.clear();
                byte[] bytes = buffer.array();
                String str = new String(bytes, 0, length);
                String[] results = str.trim().split("\r\n");
                for (String result : results) {

                    if (carsFlag == false) {
                        if (!result.equals("vehicles")) {
                            String[] strs = result.trim().split(" ");
                            String Id = strs[0];
                            int demand = Integer.valueOf(strs[3]);
                            builder.addJob(Service.Builder.newInstance(Id).addSizeDimension(0, demand)
                                    .setLocation(Location.newInstance(Double.valueOf(strs[1]), Double.valueOf(strs[2]))).build());
                        } else {
                            carsFlag = true;
                            continue;
                        }

                    }else{
                        String[] strs = result.trim().split(" ");
                        int volume = Integer.valueOf(strs[0]);
                        double fixedCost = Double.valueOf(strs[1]);
                        double variableCost = Double.valueOf(strs[2]);
                        int number = Integer.valueOf(strs[3]);
                        VehicleType type1 = VehicleTypeImpl.Builder.newInstance("type_" + carTypeNum)
                                .addCapacityDimension(0, volume).setFixedCost(fixedCost).setCostPerDistance(variableCost).build();
                        for (int i = 0; i < number; i++) {
                            builder.addVehicle(VehicleImpl.Builder.newInstance(carTypeNum + "_" + (i + 1)).
                                    setStartLocation(Location.newInstance(40, 40)).setType(type1).build());
                        }
                        carTypeNum++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fcIn != null){
                try {
                    fcIn.close();
                    fInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }


    }


    public static void main(String[] args) {

        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        readFileByIO("E:\\Paper\\成本分车\\代码\\dataSet\\c50_13.txt", vrpBuilder);
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();


        //add penaltyVehicles to allow invalid solutions temporarily
//		vrpBuilder.addPenaltyVehicles(5, 1000);

        //set fleetsize finite
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.INFINITE);

        //build problem
        VehicleRoutingProblem vrp = vrpBuilder.build();
        long time1 = threadMXBean.getCurrentThreadCpuTime();
        VehicleRoutingAlgorithm vra = Jsprit.createAlgorithm(vrp);


        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();

        VehicleRoutingProblemSolution best = Solutions.bestOf(solutions);
        long time2 = threadMXBean.getCurrentThreadCpuTime();
        System.out.println("CPU:" + ((time2 - time1) /1000000.0));
        SolutionPrinter.print(vrp, best, SolutionPrinter.Print.VERBOSE);

        new GraphStreamViewer(vrp, best).setRenderDelay(100).display();


    }


}
