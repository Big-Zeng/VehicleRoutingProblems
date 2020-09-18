package com.sharpcj.aopdemo.test1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2020/9/15.
 */
 class shootCar{
    private int adrId;
    private  String carNum;
    private Date shootTime;

    public int getAdrId() {
        return adrId;
    }

    public void setAdrId(int adrId) {
        this.adrId = adrId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public Date getShootTime() {
        return shootTime;
    }

    public void setShootTime(Date shootTime) {
        this.shootTime = shootTime;
    }
}

public class test323 {
    public float getAvgTravel(int beginAddress, int endAddress, List<shootCar> shootCarList) {

        Date startTime = null;
        Date endTime = null;

        for (shootCar shootCar : shootCarList) {
            if (String.valueOf(shootCar.getAdrId()).equals(beginAddress)) {
                startTime = shootCar.getShootTime();
            }
            if (String.valueOf(shootCar.getAdrId()).equals(endAddress)) {
                endTime = shootCar.getShootTime();
            }
        }
        BigDecimal data = new BigDecimal(((endTime.getTime() - startTime.getTime()) / 1000) % 60);
        data = data.setScale(2, RoundingMode.HALF_UP);

        return data.floatValue();

    }



}
