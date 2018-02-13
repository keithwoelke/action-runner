package com.github.keithwoelke.actions.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SuppressWarnings("WeakerAccess")
@ComponentScan(basePackages = {"com.github.keithwoelke.actions.core"})
@Import(com.github.keithwoelke.assertion.config.SpringConfiguration.class)
@Configuration
public class SpringConfiguration {

}
