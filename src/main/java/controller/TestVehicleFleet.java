package controller;

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
import service.ReadDataInterface;
import service.TXTReadData;

import java.util.ArrayList;
import java.util.List;

import static common.CommonMethod.getVehicles;

/**
 * Created by Administrator on 2020/9/29.
 */
public class TestVehicleFleet {
    private static RedisClient redisClient = new RedisClient();
    public static final SqlSession session = MybatisSessionUtil.getSession();


    public static void main(String[] args) {
        //TestStandardInstance();
        ICarModelDao iCarModelDao = session.getMapper(ICarModelDao.class);
        IOrderInformationDao iOrderInformationDao = session.getMapper(IOrderInformationDao.class);
        IAreaDao iAreaDao = session.getMapper(IAreaDao.class);
        //430103 天心区
        //430181 浏阳县
        String waveId = "201702050034";
        String areaId = "430103";


        List<OrderInformation> orderInformations
                = iOrderInformationDao.selectByAreaIdAndWaveId(areaId,waveId);
        List<CarModel> carModels
                = iCarModelDao.selectAllRemandCars();
        Area area = iAreaDao.selectByPrimaryKey(areaId);

        ArrayList<Client> clients = CommonMethod. getClients(orderInformations);
        ArrayList<Vehicle> vehicles = getVehicles(carModels);

        Client subDepot = getSubAreaDepot(area);
        Client depot = CommonMethod.getDeopt();

        CaculateVehicle caculateVehicle = new BasedCostVehiclePlanning();
        caculateVehicle.getCaculatedVehicle(clients, vehicles, subDepot, depot);

    }


    public static Client getSubAreaDepot(Area area) {
        Client subAreaDepot = new Client(-1, "subAreaId", 0,
                Double.valueOf(area.getVirHouseLat()), Double.valueOf(area.getVirHouseLon()));
        return subAreaDepot;
    }



    private static void TestStandardInstance() {
        CaculateVehicle caculateVehicle = new BasedCostVehiclePlanning();
        ReadDataInterface readDataInterface = new TXTReadData();
        HVRPData data = readDataInterface.readFile("F:\\论文文档\\成本分车\\Code\\vfmpv03.txt");

        Client depot = null;
        try {
            depot = data.getClients().get(0).clone();
            Client subDepot = depot.clone();
            data.getClients().remove(0);
            depot.setId(0);
            subDepot.setId(-1);
            caculateVehicle.getCaculatedVehicle(data.getClients(), data.getVehicles(), subDepot, depot);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
