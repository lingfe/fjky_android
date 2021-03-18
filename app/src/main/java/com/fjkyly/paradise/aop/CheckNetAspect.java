package com.fjkyly.paradise.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2020/01/11
 *    desc   : 网络检测切面
 */
@Aspect
public class CheckNetAspect {

    /**
     * 方法切入点
     */
    @Pointcut("execution(@com.hjq.demo.aop.CheckNet * *(..))")
    public void method() {}
}