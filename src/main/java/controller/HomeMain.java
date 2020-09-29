package controller;

import common.MybatisSessionUtil;
import dao.IAreaDao;
import dao.ICarModelDao;
import dao.IOrderInformationDao;
import org.apache.ibatis.session.SqlSession;
import pojo.Area;
import pojo.HVRPData;
import pojo.OrderInformation;
import pojo.Vehicle;
import service.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/8/12.
 */
public class HomeMain {


    public static void main(String[] args) {
        ReadDataInterface readDataInterface = new TXTReadData();
        HVRPData data = readDataInterface.readFile("F:\\论文文档\\成本分车\\Code\\vfmpv03.txt");
        ArrayList<Vehicle> vehicleList = new ArrayList<>();

        HVRPMainInterface hvrpMainInterface = new GuriboVRP();
        try {
            hvrpMainInterface.HVRPSolution(data.getClients(),data.getVehicles(), data.getDis());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }






}
