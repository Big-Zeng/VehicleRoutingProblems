package dao;

import org.apache.ibatis.annotations.Param;
import pojo.CarModel;

import java.util.List;

/**
 * Created by Administrator on 2020/8/12.
 */
public interface ICarModelDao {


    List<CarModel> selectAllCarModel();

}
