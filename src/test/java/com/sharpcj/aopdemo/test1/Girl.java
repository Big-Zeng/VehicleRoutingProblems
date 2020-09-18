package com.sharpcj.aopdemo.test1;

/**
 * Created by Administrator on 2020/8/23.
 */
public class Girl implements IBuy {

    @Override
    public String buy(double price) {
        System.out.println("女孩买了一件漂亮的衣服");
        return "衣服";
    }

}
