package service;

import pojo.Client;
import pojo.Vehicle;

import java.util.ArrayList;

/**
 * Created by Administrator on 2020/8/22.
 */
public class Teest {

    public static void main(String[] args) {



        ArrayList<Client> n = new ArrayList<>();
        n.add(new Client(0, 0));
        ArrayList<Client> clients = new ArrayList<>();
        clients.add(new Client(0, 0));
        clients.add(new Client(1, 1200));
        clients.add(new Client(2, 1700));
        clients.add(new Client(3, 1500));
        clients.add(new Client(4, 1400));
        clients.add(new Client(5, 1700));
        clients.add(new Client(6, 1400));
        clients.add(new Client(7, 1200));
        clients.add(new Client(8, 1900));
        clients.add(new Client(9, 1800));
        clients.add(new Client(10, 1600));
        clients.add(new Client(11, 1700));
        clients.add(new Client(12, 1100));

        double[][] dis = new double[][]{
                {0, 10, 20, 25, 25, 20, 10,},
                {10, 0, 12, 20, 25, 30, 20},
                {20, 12, 0, 10, 11, 22, 30},
                {25, 20, 10, 0, 2, 11, 25},
                {25, 25, 11, 2, 0, 10, 20},
                {20, 30, 22, 11, 10, 0, 12},
                {10, 20, 30, 25, 20, 12, 0}
        };

        double[][] dis2 = new double[][]{
                {0, 9, 14, 21, 23, 22, 25, 32, 36, 38, 42, 50, 52},
                {9, 0, 5, 12, 22, 21, 24, 31, 35, 37, 41, 49, 51},
                {14, 5, 0, 7, 17, 16, 23, 26, 30, 36, 36, 44, 46},
                {21, 12, 7, 0, 10, 21, 30, 27, 37, 43, 31, 37, 39,},
                {23, 22, 17, 10, 0, 19, 28, 25, 35, 41, 29, 31, 29,},
                {22, 21, 16, 21, 19, 0, 9, 10, 16, 22, 20, 28, 30,},
                {25, 24, 23, 30, 28, 9, 0, 7, 11, 13, 17, 25, 27},
                {32, 31, 26, 27, 25, 10, 7, 0, 10, 16, 10, 18, 20},
                {36, 35, 30, 37, 35, 16, 11, 10, 0, 6, 6, 14, 16},
                {38, 37, 36, 43, 41, 22, 13, 16, 6, 0, 12, 12, 20,},
                {42, 41, 36, 31, 29, 20, 17, 10, 6, 12, 0, 8, 10,},
                {50, 49, 44, 37, 31, 28, 25, 18, 14, 12, 8, 0, 10,},
                {52, 51, 46, 39, 29, 30, 27, 20, 16, 20, 10, 10, 0,},
        };

        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle(1, 6000, 1, 60)); //set carType

        HVRPMainInterface hvrpMainInterface = new GuriboVRP();
        try {
            hvrpMainInterface.HVRPSolution(clients,vehicles,dis2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

   public static class order{

        public order(int id, int v, int w) {
            this.id = id;
            V = v;
            W = w;
            cost = V + w * 2;
        }

        public int id;
        public int V;
       public int W;
       public  int cost;
    }

}
