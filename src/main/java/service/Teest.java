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
        clients.add(new Client(1, 1));
        clients.add(new Client(2, 1));
        clients.add(new Client(3, 1));
        clients.add(new Client(4, 1));
        clients.add(new Client(5, 1));
        clients.add(new Client(6, 1));

        double[][] dis = new double[][]{
                {0, 10, 20, 25, 25, 20, 10,},
                {10, 0, 12, 20, 25, 30, 20},
                {20, 12, 0, 10, 11, 22, 30},
                {25, 20, 10, 0, 2, 11, 25},
                {25, 25, 11, 2, 0, 10, 20},
                {20, 30, 22, 11, 10, 0, 12},
                {10, 20, 30, 25, 20, 12, 0}
        };

        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle(1, 3, 1)); //set carType




        try {
            GuriboVRP vrp = new GuriboVRP(clients, n, vehicles, dis);
            vrp.solution();
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
