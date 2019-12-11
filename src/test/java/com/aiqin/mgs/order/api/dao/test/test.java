package com.aiqin.mgs.order.api.dao.test;

import com.aiqin.mgs.order.api.OrderApiBootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertNull;


//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = OrderApiBootApplication.class)
public class test {

    @Test
    public void testDate(){
        Date date = new Date();
        assertNull(date);
        System.out.println(date);
    }
}
