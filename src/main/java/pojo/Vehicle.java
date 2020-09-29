package pojo;

/**
 * Created by Administrator on 2020/9/18.
 */
public class Vehicle implements Comparable<Vehicle> {
    private int Id;
    private double capacity;
    private double mCost; //per mile Cost
    private int size;
    private double fixedCost;

    private String vehicleName;




    public String getVehicleName() {
        return vehicleName;
    }

    public double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(double fixedCost) {
        this.fixedCost = fixedCost;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Vehicle(int id, double capacity, double mCost, int size) {
        this.size = size;
        Id = id;
        this.capacity = capacity;
        this.mCost = mCost;
    }

    public Vehicle(int id, double capacity, double mCost, int size, double fixedCost) {
        Id = id;
        this.capacity = capacity;
        this.mCost = mCost;
        this.size = size;
        this.fixedCost = fixedCost;
    }

    public Vehicle(int id, double capacity, double mCost, int size, String vehicleName) {
        Id = id;
        this.capacity = capacity;
        this.mCost = mCost;
        this.size = size;

        this.vehicleName = vehicleName;
    }

    public double getmCost() {
        return mCost;
    }

    public void setmCost(double mCost) {
        this.mCost = mCost;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public int compareTo(Vehicle o) {
        if (this.capacity > o.capacity) {
            return 1;
        } else if (this.capacity < o.capacity) {
            return -1;
        }else
            return 0;
    }
}
