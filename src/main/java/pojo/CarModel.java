package pojo;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2020/8/12.
 */
public class CarModel {
    /**
     * @return the carTypeId
     */
    public String getCarTypeId() {
        return carTypeId;
    }

    /**
     * @param carTypeId the carTypeId to set
     */
    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    /**
     * @return the restrictedArea
     */
    public String getRestrictedArea() {
        return restrictedArea;
    }

    /**
     * @param restrictedArea the restrictedArea to set
     */
    public void setRestrictedArea(String restrictedArea) {
        this.restrictedArea = restrictedArea;
    }

    /**
     * @return the goodsClass
     */
    public String getGoodsClass() {
        return goodsClass;
    }

    /**
     * @param goodsClass the goodsClass to set
     */
    public void setGoodsClass(String goodsClass) {
        this.goodsClass = goodsClass;
    }

    private String carTypeId;
    private String name;

    private String degree;

    private float ton;

    private float vol;

    private String restrictedArea;
    /**
     * @return the addTime
     */
    public String getAddTime() {
        return addTime;
    }

    /**
     * @param addTime the addTime to set
     */
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    private String addTime;

    private String goodsClass;

    private String hCost;

    private String kCost;

    private String remark;

    private String state;

    private String editor;

    private Date edtime;

    private float length;

    private float wideth;

    private float high;

    private float normaltempervol;

    private float coldstoragevol;

    private String frozenvol;

    private float downloadlimit;

    private float toploadlimit;

    private String sendpriority;

    private int count;
    //是否已选择
    private Boolean field=false;

    public Boolean getField() {
        return field;
    }

    public void setField(Boolean field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree == null ? null : degree.trim();
    }

    public float getTon() {
        return ton;
    }

    public void setTon(float ton) {
        this.ton = ton;
    }

    public float getVol() {
        return vol;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }

    public String gethCost() {
        return hCost;
    }

    public void sethCost(String hCost) {
        this.hCost = hCost == null ? null : hCost.trim();
    }

    public String getkCost() {
        return kCost;
    }

    public void setkCost(String kCost) {
        this.kCost = kCost == null ? null : kCost.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor == null ? null : editor.trim();
    }

    public Date getEdtime() {
        return edtime;
    }

    public void setEdtime(Date edtime) {
        this.edtime = edtime;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWideth() {
        return wideth;
    }

    public void setWideth(float wideth) {
        this.wideth = wideth;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getNormaltempervol() {
        return normaltempervol;
    }

    public void setNormaltempervol(float normaltempervol) {
        this.normaltempervol = normaltempervol;
    }

    public float getColdstoragevol() {
        return coldstoragevol;
    }

    public void setColdstoragevol(float coldstoragevol) {
        this.coldstoragevol = coldstoragevol;
    }

    public String getFrozenvol() {
        return frozenvol;
    }

    public void setFrozenvol(String frozenvol) {
        this.frozenvol = frozenvol == null ? null : frozenvol.trim();
    }

    public float getDownloadlimit() {
        return downloadlimit;
    }

    public void setDownloadlimit(float downloadlimit) {
        this.downloadlimit = downloadlimit;
    }

    public float getToploadlimit() {
        return toploadlimit;
    }

    public void setToploadlimit(float toploadlimit) {
        this.toploadlimit = toploadlimit;
    }

    public String getSendpriority() {
        return sendpriority;
    }

    public void setSendpriority(String sendpriority) {
        this.sendpriority = sendpriority == null ? null : sendpriority.trim();

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {

        this.count = count;
    }

    public static CarModel getCarModel(List<CarModel> carModels, String carGrade) throws Exception{
        for (CarModel carModel : carModels) {
            if(carModel.getDegree().equals(carGrade))
                return  carModel;
        }

        throw new Exception("car not found,please insert carData to database");
    }

}