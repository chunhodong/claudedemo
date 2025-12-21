package com.example.claudedemo;

import com.example.claudedemo.coupon.CouponApplication;
import com.example.claudedemo.product.ProductApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
    basePackages = "com.example.claudedemo",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {ProductApplication.class, CouponApplication.class}
    )
)
public class ClaudedemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClaudedemoApplication.class, args);
    }

}
