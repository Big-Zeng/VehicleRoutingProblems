package pojo;

/**
 * Created by Administrator on 2020/9/18.
 */
public class Client {

    private int Id;

    private String clientId;

    private double demand;

    public Client(int id, double demand) {
        Id = id;
        this.demand = demand;
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
