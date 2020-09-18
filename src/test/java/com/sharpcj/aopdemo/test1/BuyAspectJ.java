package com.sharpcj.aopdemo.test1;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2020/8/23.
 */
@Aspect
@Component
public class BuyAspectJ {

    @Pointcut("execution(String com.sharpcj.aopdemo.test1.IBuy.buy(..))")
    public void haha() {

        System.out.println("男孩女孩都买自己喜欢的东西");
    }


    @Before("point()")
    public void hehe() {
        System.out.println("before ...");
    }


}
