package common;

import pojo.CarModel;
import pojo.Client;
import pojo.OrderInformation;
import pojo.Vehicle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2020/8/13.
 */
public class CommonMethod {
    /**
     * 两点距离
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return double
     * @author zxf
     * 2016年7月14日 下午4:54:19
     */
    public static double lineSpace(double x1, double y1, double x2, double y2) {
        double lineLength = 0;
        lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
                * (y1 - y2));
        return lineLength;
    }

    /**
     * // 点到直线的最短距离的判断 点（x0,y0） 到由两点组成的线段（x1,y1） ,( x2,y2 )
     *@author zxf2015年8月19日
     * @param x1 （可以理解为关键点）
     * @param y1（可以理解为 关键点）
     * @param x2 （可以理解为仓库点）
     * @param y2
     * @param x0 需要判断的点
     * @param y0  需要判断的点
     * @return
     */

    @SuppressWarnings("unused")
    public static double pointToLine(double x1, double y1, double x2, double y2, double x0,

                                     double y0) {
        double space = 0;
        double a, b, c;
        a =  lineSpace(x1, y1, x2, y2);// 线段的长度
        b =  lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离
        c =  lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离
        if (c+b == a) {//点在线段上
            space = 0;
            return space;
        }
        if (a <= 0.000001) {//不是线段，是一个点
            space = b;
            return space;
        }
        if (c * c >= a * a + b * b) { //组成直角三角形或钝角三角形，(x1,y1)为直角或钝角
            space = b;
            return space;
        }
        if (b * b >= a * a + c * c) {//组成直角三角形或钝角三角形，(x2,y2)为直角或钝角
            space = c;
            return space;
        }
        //组成锐角三角形，则求三角形的高
        double p = (a + b + c) / 2;// 半周长
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
        space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
        return space;
    }

    /**
     * read File
     * @param path
     * @return
     */
    public static String readFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String temString = null;
            while ((temString = reader.readLine()) != null) {
                sb.append(temString);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e
                        ) {
                    System.out.println(e.toString());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 传入一个数列x计算平均值
     * @param x
     * @return 平均值
     */
    public static double average(List<Double> x) {
        int n = x.size();            //数列元素个数
        double sum = 0;
        for (double i : x) {        //求和
            sum+=i;
        }
        return sum/n;
    }

    /**
     * 传入一个数列x计算方差
     * 方差s^2=[（x1-x）^2+（x2-x）^2+......（xn-x）^2]/（n）（x为平均数）
     * @param x 要计算的数列
     * @return 方差
     */
    public static double variance(List<Double> x) {
        int n = x.size();            //数列元素个数
        double avg = average(x);    //求平均值
        double var = 0;
        for (double i : x) {
            var += (i-avg)*(i-avg);    //（x1-x）^2+（x2-x）^2+......（xn-x）^2
        }
        return var/n;
    }

    /**
     * 经纬度->换算米
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {

        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;


        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;


        double R = 6371;

        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return d * 1000;
    }




    /**
     * 经纬度->平面坐标系
     * @param lat
     * @param lon
     * @return
     */
    public static double[] MillierConvertion(double lat, double lon)
    {
        double L = 6381372 * Math.PI * 2;//地球周长
        double W=L;// 平面展开后，x轴等于周长
        double H=L/2;// y轴约等于周长一半
        double mill=2.3;// 米勒投影中的一个常数，范围大约在正负2.3之间
        double x = lon * Math.PI / 180;// 将经度从度数转换为弧度
        double y = lat * Math.PI / 180;// 将纬度从度数转换为弧度
        y=1.25 * Math.log( Math.tan( 0.25 * Math.PI + 0.4 * y ) );// 米勒投影的转换
        // 弧度转为实际距离
        x = ( W / 2 ) + ( W / (2 * Math.PI) ) * x;
        y = ( H / 2 ) - ( H / ( 2 * mill ) ) * y;
        double[] result=new double[2];
        result[0]=x;
        result[1]=y;
        return result;
    }
    /**
     * 获取客户间的距离
     * @param clients
     * @return
     */
    public static double[][] getClientDises(List<Client> clients) {
        double[][] dis = new double[clients.size()][clients.size()];


        for (int i = 0; i < clients.size(); i++) {
            for (int i1 = 0; i1 < clients.size(); i1++) {
                Client client1 = clients.get(i);
                Client client2 = clients.get(i1);
                dis[i][i1] = getDistance(client1.getX(), client1.getY(), client2.getX(), client2.getY());
            }
        }

        return dis;
    }



    /**
     * 获取唯一仓库点
     * @return
     */
    public static Client getDeopt() {
        return new Client(0, "depot", 0, 27.902447, 113.003699);

    }

    public static ArrayList<Vehicle> getVehicles(List<CarModel> carModels) {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        for (int i = 0; i < carModels.size(); i++) {
            vehicles.add(new Vehicle(i, carModels.get(i).getVol() * 1000, Double.valueOf(carModels.get(i).getkCost()),
                    carModels.get(i).getCount(),carModels.get(i).getCarTypeId()));
        }
        Collections.sort(vehicles);
        return vehicles;
    }

    public static ArrayList<Client> getClients(List<OrderInformation> orderInformations) {
        ArrayList<Client> clients = new ArrayList<>();
        for (int i = 0; i < orderInformations.size(); i++) {
            clients.add(new Client(i + 1, orderInformations.get(i).getClientId().toString(),
                    orderInformations.get(i).getTurnoverVol(), Double.valueOf(orderInformations.get(i).getLat())
                    , Double.valueOf(orderInformations.get(i).getLon())));
        }
        return clients;
    }



    public static double[][] getDis(List<Client> clients) {
        double[][] dis = new double[clients.size()][clients.size()];
        for (int i = 0; i < clients.size(); i++) {
            for (int i1 = 0; i1 < clients.size(); i1++) {
                double oneDis =  clients.get(i).getToDis(clients.get(i1));
                dis[i][i1] = oneDis;
                dis[i1][i] = oneDis;
            }
        }
        return dis;
    }

    /**
     * 传入一个数列x计算标准差
     * 标准差σ=sqrt(s^2)，即标准差=方差的平方根
     * @param x 要计算的数列
     * @return 标准差
     */
    public static double standardDiviation(List<Double> x) {
        return  Math.sqrt(variance(x));
    }

}
