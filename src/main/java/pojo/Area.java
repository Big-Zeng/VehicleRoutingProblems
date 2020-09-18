package pojo;

import java.util.List;

/**
 * Created by Administrator on 2020/8/13.
 */
public class Area {
    private String areaId;

    private String name;

    private String virHouseLat;

    private String virHouseLon;

    private String virHouseDir;




    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getVirHouseLat() {
        return virHouseLat;
    }

    public void setVirHouseLat(String virHouseLat) {
        this.virHouseLat = virHouseLat == null ? null : virHouseLat.trim();
    }

    public String getVirHouseLon() {
        return virHouseLon;
    }

    public void setVirHouseLon(String virHouseLon) {
        this.virHouseLon = virHouseLon == null ? null : virHouseLon.trim();
    }

    public String getVirHouseDir() {
        return virHouseDir;
    }

    public void setVirHouseDir(String virHouseDir) {
        this.virHouseDir = virHouseDir == null ? null : virHouseDir.trim();
    }

}
