package io.github.rothschil;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 * @author <a>https://github.com/rothschil</a>
 * @date 20/11/18 11:03
 * @since  1.0.0
*/
@MapperScan(basePackages = {"io.github.rothschil.**.mapper"})
@SpringBootApplication
public class MoonApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoonApplication.class,args);
    }

}

