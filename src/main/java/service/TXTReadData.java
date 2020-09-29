package service;

import common.CommonMethod;
import pojo.Client;
import pojo.HVRPData;
import pojo.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/9/28.
 */
public class TXTReadData implements ReadDataInterface {


    @Override
    public HVRPData readFile(String path) {
        HVRPData hvrpData = new HVRPData();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));


            List<String> VehicleStr = new ArrayList<>();
            List<String> ClientStr = new ArrayList<>();
            String firstLine = reader.readLine();
            String[] datas = removeStr(firstLine.trim().split(" "));
            int clientN = Integer.valueOf(datas[0]);
            int vehicleN = Integer.valueOf(datas[1]);
            double bestResult = Double.valueOf(datas[5]);
            hvrpData.setBestKnownSolution(bestResult);

            int line = 0;
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {//BufferedReader有readLine()，可以实现按行读取
                if (line < vehicleN) {
                    VehicleStr.add(tempString);
                } else {
                    ClientStr.add(tempString);
                }
                line++;
            }

            ArrayList<Vehicle> vehicleList = getVehicles(VehicleStr);
            ArrayList<Client> clients = getClients(ClientStr);
            double[][] dis = CommonMethod.getDis(clients);
            hvrpData.setClients(clients);
            hvrpData.setDis(dis);
            hvrpData.setVehicles(vehicleList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hvrpData;
    }


    private String[] removeStr(String[] strings) {
        List<String> strings1 = new ArrayList<>();

        for (String string : strings) {
            if (string.equals(" ") || string.equals("")) {
                continue;
            }
            strings1.add(string);
        }

        return   strings1.toArray(new String[strings1.size()]);

    }



    private ArrayList<Vehicle> getVehicles(List<String> vehicleStrs) {
        ArrayList<Vehicle> vehicleList = new ArrayList<>();
        int id = 1;
        for (String vehicleStr : vehicleStrs) {
            String[] strings = removeStr(vehicleStr.trim().split(" "));
            int size = Integer.valueOf(strings[0]);
            double capacity = Double.valueOf(strings[1]);
            double fixedCost = Double.valueOf(strings[2]);
            double cost = Double.valueOf(strings[3]);
            vehicleList.add(new Vehicle(id, capacity, cost, size, fixedCost));
            id++;
        }
        return vehicleList;
    }



    private ArrayList<Client> getClients(List<String> clientStrs) {
        ArrayList<Client> clients = new ArrayList<>();
        int id = 1;

        for (String clientStr : clientStrs) {
            if (clientStr.equals("")) {
                break;
            }
            String[] strings = removeStr(clientStr.trim().split(" "));
            double x = Double.valueOf(strings[0]);
            double y = Double.valueOf(strings[1]);
            double demands = 0;
            if (strings.length == 3)
                demands = Double.valueOf(strings[2]);
            clients.add(new Client(id, demands, y, x));
            id++;
        }
        return clients;
    }

}
