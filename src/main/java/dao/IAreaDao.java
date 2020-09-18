package dao;

import pojo.Area;

/**
 * Created by Administrator on 2020/8/14.
 */
public interface IAreaDao {


    Area selectByPrimaryKey(String areaId);

}
