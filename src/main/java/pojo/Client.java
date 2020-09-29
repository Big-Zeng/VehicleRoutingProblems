package pojo;

import common.CommonMethod;

/**
 * Created by Administrator on 2020/9/18.
 */
public class Client implements Cloneable {

    private int Id;

    private String clientId;

    private double demand;

    private double y;

    private double x;

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public Client(int id, String clientId, double demand, double lat, double lon) {
        Id = id;
        this.clientId = clientId;
        this.demand = demand;
        double[] result = CommonMethod.MillierConvertion(lat, lon);
        x = result[0];
        y = result[1];
    }

    public Client(int id, double demand, double y, double x) {
        Id = id;
        this.demand = demand;
        this.y = y;
        this.x = x;
    }

    @Override
    public Client clone() throws CloneNotSupportedException {

        return (Client)super.clone();
    }

    public Client(int id, double y, double x) {
        Id = id;
        this.y = y;
        this.x = x;
    }

    public Client(int id, double demand) {
        Id = id;
        this.demand = demand;
    }

    public double getToDis(Client client) {
        return  Math.sqrt(Math.pow(client.x - this.x, 2) + Math.pow(client.y - this.y, 2));
    }


    public Client(int id, String clientId, double demand) {
        Id = id;
        this.clientId = clientId;
        this.demand = demand;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
    }
}
