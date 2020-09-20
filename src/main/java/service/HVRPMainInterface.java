package service;

import pojo.Client;
import pojo.Vehicle;

import java.util.ArrayList;

/**
 * Created by Administrator on 2020/9/19.
 */
public interface HVRPMainInterface {
    /**
     *  caculate HVRP
     * @param clients
     * @param vehicles
     * @param dis
     * @throws Exception
     */
     void HVRPSolution(ArrayList<Client> clients, ArrayList<Vehicle> vehicles, double[][] dis) throws Exception;



}
