package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
     * 传入一个数列x计算标准差
     * 标准差σ=sqrt(s^2)，即标准差=方差的平方根
     * @param x 要计算的数列
     * @return 标准差
     */
    public static double standardDiviation(List<Double> x) {
        return  Math.sqrt(variance(x));
    }

}
