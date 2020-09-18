package com.sharpcj.aopdemo.test1;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2020/8/23.
 */
@Component
public class Boy implements IBuy{


    @Override
    public String buy(double price) {

        System.out.println("男孩买了一个游戏机");
        return "游戏机";
    }
}
