package pojo;

/**
 * Created by Administrator on 2020/9/18.
 */
public class Vehicle {
    private int Id;
    private double capacity;
    private double mCost; //per mile Cost

    public Vehicle(int id, double capacity, double mCost) {
        Id = id;
        this.capacity = capacity;
        this.mCost = mCost;
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
}
