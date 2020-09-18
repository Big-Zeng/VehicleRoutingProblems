package dao;

import org.apache.ibatis.annotations.Param;
import pojo.OrderInformation;

import java.util.List;

/**
 * Created by Administrator on 2020/8/13.
 */
public interface IOrderInformationDao {
    /**
     * select all orders by areaId
     * @param areaId
     * @return
     */
    List<OrderInformation> selectByAreaAndIsReexamine(String areaId);


}