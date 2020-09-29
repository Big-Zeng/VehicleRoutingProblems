package service;

import pojo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/9/9.
 */
public interface CaculateVehicle {


    /**
     *
     * @param clients
     * @param vehicles
     * @return
     */
    List<Vehicle> getCaculatedVehicle(ArrayList<Client> clients, ArrayList<Vehicle> vehicles, Client subareaDepot, Client depot);

}
