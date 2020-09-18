package pojo;

import java.util.List;

/**
 * Created by Administrator on 2020/8/13.
 */
public class Car implements Cloneable {


    private String carType;

    private int carGrade;
    private double carVolume;
    private double carWeight;//车辆载重
    private double upVolLimit = 0.85;//车的体积上线
    private double downVolLimit = 0.7;//体积下限

    private List<OrderInformation> orderInformations;//订单信息
    private double alreadyLoadVol;//已装的体积
    private double alreadyLoadWeight;//已装的重量


    public List<OrderInformation> getOrderInformations() {
        return orderInformations;
    }

    public void setOrderInformations(List<OrderInformation> orderInformations) {
        this.orderInformations = orderInformations;
    }

    public double getAlreadyLoadVol() {
        return alreadyLoadVol;
    }

    public void setAlreadyLoadVol(double alreadyLoadVol) {
        this.alreadyLoadVol = alreadyLoadVol;
    }

    public double getAlreadyLoadWeight() {
        return alreadyLoadWeight;
    }

    public void setAlreadyLoadWeight(double alreadyLoadWeight) {
        this.alreadyLoadWeight = alreadyLoadWeight;
    }

    public double getCarWeight() {
        return carWeight;
    }

    public void setCarWeight(double carWeight) {
        this.carWeight = carWeight;
    }

    public double getUpVolLimit() {
        return upVolLimit;
    }

    public void setUpVolLimit(double upVolLimit) {
        this.upVolLimit = upVolLimit;
    }

    public double getDownVolLimit() {
        return downVolLimit;
    }

    public void setDownVolLimit(double downVolLimit) {
        this.downVolLimit = downVolLimit;
    }


    public String getCarType(){
        return carType;

    }
    public void setCarType(String carType){
        this.carType=carType;
    }


    public int  getCarGrade(){
        return carGrade;
    }
    public void setCarGrade(int carGrade){
        this.carGrade=carGrade;
    }
    public double  getCarVolume(){
        return carVolume;
    }
    public void setCarVolume(double carVolume){
        this.carVolume=carVolume;
    }

    public Car() {
        super();
    }

    public Car(CarModel carModel) {
        this.carGrade = Integer.parseInt(carModel.getDegree());
        this.carType = carModel.getCarTypeId();
        this.carVolume = carModel.getVol();
        this.upVolLimit = carModel.getToploadlimit();
        this.downVolLimit = carModel.getDownloadlimit();

       // this.carCount =
    }

    public Car clone() {
        try {
            return (Car)super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

}
