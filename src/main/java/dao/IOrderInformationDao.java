package dao;

import org.apache.ibatis.annotations.Param;
import pojo.OrderInformation;

import java.util.List;
import java.util.Map;

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

    List<OrderInformation> selectByAreaIdAndWaveId(@Param("areaId") String areaId, @Param("waveId") String waveId);


    int updateByAreaId(@Param("waveId")String waveId, @Param("areaId")String areaId);


    int updateRandomWaveId(@Param("Ids") List<Integer> ids,@Param("waveId")String waveId);

}