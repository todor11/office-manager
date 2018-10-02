package com.officemaneger.configs;

import com.officemaneger.areas.paidHoursCalculator.PaidHoursCalculator;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@Configuration
public class ApplicationBeenConfig {

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PaidHoursCalculator getPaidHoursCalculator() {
        return new PaidHoursCalculator();
    }
}
