package pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2020/8/13.
 */
public class OrderInformation {
    private Integer id;

    private String storgeName;

    private String assignCarState = "否";

    private String clientName;

    private String contect;

    private String contcetNum;

    private String lat;

    private String areaId;

    private float vol;
    private float standard;

    //总件数
    private float num;
    private float wholeNum;
    private float weight;

    private double money;

    private BigDecimal clientId;

    private String lon;

    private String waveId;

    private float turnoverNumber;
    private String turnoverNumberType;

    //复核后总体积
    private float turnoverVol;
    private float departVol;
    private float  totalCheckedVol;
    private float  totaUnCheckedVol;
    private Date createTime;

    private Date assignCarTime;

    private String assignCarId;

    private char checkState;

    private  Integer orderTypeAssign;
    private String turnBoxCode;
    private String yesOrno;

    private int SendSeq; //派车顺序

    //点到纺锤体中线的直线距离
    private double distanceToMidline = 0;

    public OrderInformation(BigDecimal clientId,String lat,  String lon, float turnoverVol) {
        this.lat = lat;
        this.clientId = clientId;
        this.lon = lon;
        this.turnoverVol = turnoverVol;
    }

    public  OrderInformation(){

    }

    public int getSendSeq() {
        return SendSeq;
    }

    public void setSendSeq(int sendSeq) {
        SendSeq = sendSeq;
    }

    public Integer getOrderTypeAssign() {
        return orderTypeAssign;
    }

    public void setOrderTypeAssign(Integer orderTypeAssign) {
        this.orderTypeAssign = orderTypeAssign;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStorgeName() {
        return storgeName;
    }

    public void setStorgeName(String storgeName) {
        this.storgeName = storgeName;
    }

    public String getWaveId() {
        return waveId;
    }

    public void setWaveId(String waveId) {
        this.waveId = waveId;
    }

    public String getAssignCarState() {
        return assignCarState;
    }

    public void setAssignCarState(String assignCarState) {
        this.assignCarState = assignCarState;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName == null ? null : clientName.trim();
    }

    public String getContect() {
        return contect;
    }

    public void setContect(String contect) {
        this.contect = contect == null ? null : contect.trim();
    }

    public String getContcetNum() {
        return contcetNum;
    }

    public void setContcetNum(String contcetNum) {
        this.contcetNum = contcetNum == null ? null : contcetNum.trim();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat == null ? null : lat.trim();
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId == null ? null : areaId.trim();
    }

    public float getVol() {
        return vol;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public void setClientId(BigDecimal clientId) {
        this.clientId = clientId;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon == null ? null : lon.trim();
    }



    public  float getTurnoverNumber() {
        return turnoverNumber;
    }

    public void setTurnoverNumber( float turnoverNumber) {
        this.turnoverNumber = turnoverNumber;
    }

    public  float getTurnoverVol() {
        return turnoverVol;
    }

    public void setTurnoverVol( float turnoverVol) {
        this.turnoverVol = turnoverVol;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getAssignCarTime() {
        return assignCarTime;
    }

    public void setAssignCarTime(Date assignCarTime) {
        this.assignCarTime = assignCarTime;
    }

    public String getAssignCarId() {
        return assignCarId;
    }

    public void setAssignCarId(String assignCarId) {
        this.assignCarId = assignCarId;
    }

    public char getCheckState() {
        return checkState;
    }

    public void setCheckState(char checkState) {
        this.checkState = checkState;
    }


    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return false;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        OrderInformation other = (OrderInformation) obj;
        return this.clientId.equals(other.clientId)
                &&this.waveId.equals(other.waveId)
                &&this.storgeName.equals(other.storgeName);
    }


    public String getTurnBoxCode() {
        return turnBoxCode;
    }

    public void setTurnBoxCode(String turnBoxCode) {
        this.turnBoxCode = turnBoxCode;
    }

    public String getYesOrno() {
        return yesOrno;
    }

    public void setYesOrno(String yesOrno) {
        this.yesOrno = yesOrno;
    }

    public float getDepartVol() {
        return departVol;
    }

    public void setDepartVol(float departVol) {
        this.departVol = departVol;
    }

    public float getTotalCheckedVol() {
        return totalCheckedVol;
    }

    public void setTotalCheckedVol(float totalCheckedVol) {
        this.totalCheckedVol = totalCheckedVol;
    }

    public float getTotaUnCheckedVol() {
        return totaUnCheckedVol;
    }

    public void setTotaUnCheckedVol(float totaUnCheckedVol) {
        this.totaUnCheckedVol = totaUnCheckedVol;
    }

    public String getTurnoverNumberType() {
        return turnoverNumberType;
    }

    public void setTurnoverNumberType(String turnoverNumberType) {
        this.turnoverNumberType = turnoverNumberType;
    }

    public float getWholeNum() {
        return wholeNum;
    }

    public void setWholeNum(float wholeNum) {
        this.wholeNum = wholeNum;
    }

    public float  getStandard() {
        return standard;
    }

    public void setStandard(float standard) {
        this.standard = standard;
    }


    public double getDistanceToMidline() {
        return distanceToMidline;
    }

    public void setDistanceToMidline(double distanceToMidline) {
        this.distanceToMidline = distanceToMidline;
    }
}
