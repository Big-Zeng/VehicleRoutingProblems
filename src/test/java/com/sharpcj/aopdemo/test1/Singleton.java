package com.sharpcj.aopdemo.test1;

/**
 * Created by Administrator on 2020/8/24.
 */
public class Singleton {
    private Singleton() {


    }



    private static Singleton singleton = new Singleton();

    public static Singleton getSingleton() {
        return singleton;
    }


}
