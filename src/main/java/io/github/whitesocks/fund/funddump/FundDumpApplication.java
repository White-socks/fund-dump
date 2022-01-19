package io.github.whitesocks.fund.funddump;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "io.github.whitesocks.fund.funddump.mapper")
@SpringBootApplication
public class FundDumpApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundDumpApplication.class, args);
    }

}
