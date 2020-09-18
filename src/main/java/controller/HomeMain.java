package controller;

import common.MybatisSessionUtil;
import dao.IAreaDao;
import dao.ICarModelDao;
import dao.IOrderInformationDao;
import org.apache.ibatis.session.SqlSession;
import pojo.Area;
import pojo.OrderInformation;
import service.VRPMain;

import java.util.List;

/**
 * Created by Administrator on 2020/8/12.
 */
public class HomeMain {

    public static final SqlSession session = MybatisSessionUtil.getSession();


    public static void main(String[] args) {


        ICarModelDao iCarModelDao = session.getMapper(ICarModelDao.class);
        IOrderInformationDao iOrderInformationDao = session.getMapper(IOrderInformationDao.class);
        IAreaDao iAreaDao = session.getMapper(IAreaDao.class);

        List<OrderInformation> orderInformations
                = iOrderInformationDao.selectByAreaAndIsReexamine("430103");

        Area area = iAreaDao.selectByPrimaryKey("430103");
        VRPMain vrp = new VRPMain();



    }






}
