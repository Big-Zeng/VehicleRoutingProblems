package controller;

import common.MybatisSessionUtil;
import dao.IOrderInformationDao;
import org.apache.ibatis.session.SqlSession;
import pojo.OrderInformation;

import java.util.*;

/**
 * Created by Administrator on 2020/9/29.
 */
public class GenerateClients {

    public static final SqlSession session = MybatisSessionUtil.getSession();
    public static void main(String[] args) {
        IOrderInformationDao iOrderInformationDao = session.getMapper(IOrderInformationDao.class);

        String areaId = "430421";
        String originWaveId = "201606140038";
        String newWaveId = "201702050034";


        iOrderInformationDao.updateByAreaId(areaId, originWaveId);

        List<OrderInformation> list = iOrderInformationDao.selectByAreaAndIsReexamine(areaId);

        Random random = new Random();
        int size = 16 + random.nextInt(5);

        Set<Integer> set = new HashSet<>();

        while (set.size() < size) {
            int id = random.nextInt(list.size() - 1);

            set.add( list.get(id).getId());
        }
        List<Integer> list1 = new ArrayList<>();
        list1.addAll(set);


        int i = iOrderInformationDao.updateRandomWaveId(list1, newWaveId);

        session.commit();
    }


}
