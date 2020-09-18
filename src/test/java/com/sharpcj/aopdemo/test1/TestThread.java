package com.sharpcj.aopdemo.test1;

/**
 * Created by Administrator on 2020/8/24.
 */
public class TestThread {

    public static void main(String[] args) {
        System.out.println(129 & 128);
        int h ;
        System.out.println((h=1256)^(h>>>16));

    }


}

class SellThread implements Runnable {
    private int i = 100;
    String data = "OJK";


    @Override
    public void run() {

        while (true) {
            synchronized (this){
                if (i > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }
                    System.out.println(Thread.currentThread().getName() + "sell" + i--);
                }
            }

        }
    }
}
