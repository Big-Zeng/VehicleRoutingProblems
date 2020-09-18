package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import common.CommonMethod;
import common.RedisClient;
import pojo.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2020/8/12.
 */
public class VRPMain {

    //this path can revise, which saves matlab result.
    //TODO
    private  static  String carStrPath = "E:\\Paper\\成本分车\\派车顺序\\carss.txt";
    private static RedisClient redisClient = new RedisClient();


    /**
     * Paper main Method
     *
     * @param carModels
     */
    public void testPaper(List<CarModel> carModels, List<OrderInformation> orders, Area area) throws Exception {

        String carStr = CommonMethod.readFile(carStrPath);
        List<Car> cars = getCarGroup(carStr, carModels);

        int[][] dis = redisClient.getPathByAreaAndOrders(orders);



    }



    /**
     * get cars from Matlab results
     * @param carStr  outputs from matlab
     * @param carModels database cars
     * @return
     * @throws Exception not found carData from database
     */
    public List<Car>  getCarGroup(String carStr, List<CarModel> carModels) throws Exception {
        String[] results = carStr.split(" ");
        System.out.println("divide carLists for scanning algorithm");
        List<Car> carGroups = new ArrayList<Car>();
        for (String result : results) {
            String[] cars = result.split("-");
            int size = Integer.valueOf(cars[0]);    //数量
            CarModel carModel = CarModel.getCarModel(carModels, cars[1]);
            for (int i = 0; i < size; i++) {
                carGroups.add(new Car(carModel));
            }
        }
        return carGroups;

    }


    ;

}
