package com.chrisyo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogTest {
    //define Logger Object for logging
    private final Logger logger = LoggerFactory.getLogger(LogTest.class);

//    @Test
    public void testLog(){
        //System.out.println("开始计算...");
        logger.info("Calculating starts");
        int sum = 0;
        try {
            int[] nums = {1, 5, 3, 2, 1, 4, 5, 4, 6, 7, 4, 34, 2, 23};
            for (int i = 0; i < nums.length; i++) {
                sum += nums[i];
            }
        } catch (Exception e) {
            //System.out.println("程序运行出错...");
            logger.error("Program running error", e);
        }
        //System.out.println("计算结果为: "+sum);
        logger.info("Result is: {}", sum);
        //System.out.println("结束计算...");
        logger.info("Calculating ends");

    }

}
