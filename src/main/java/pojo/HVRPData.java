package pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/9/29.
 */
public class HVRPData {

    private ArrayList<Client> clients;

    private ArrayList<Vehicle> vehicles;

    private double[][] dis;

    private double bestKnownSolution;

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void setDis(double[][] dis) {
        this.dis = dis;
    }

    public void setBestKnownSolution(double bestKnownSolution) {
        this.bestKnownSolution = bestKnownSolution;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public double[][] getDis() {
        return dis;
    }

    public double getBestKnownSolution() {
        return bestKnownSolution;
    }
}
