package service;

import pojo.Car;
import pojo.CarModel;
import pojo.OrderInformation;

import java.util.List;

/**
 * Created by Administrator on 2020/9/9.
 */
public interface CaculateVehicle {

    List<Car> getCaculatedVehicle(List<OrderInformation> orderInformations, List<CarModel> carModels);





}
