package com.viewcent.data.interchange;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.viewcent.data.interchange.mapper")
public class InterchangeApplication
{
    
    public static void main(String[] args)
    {
        SpringApplication.run(InterchangeApplication.class, args);
    }
    
}
