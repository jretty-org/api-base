package org.jretty.spconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * override the SpringApplicationBuilder
 * 
 * @author zollty
 * @since 2018-3-2
 */
public class SpringApplicationBuilderExt extends SpringApplicationBuilder {
    
    protected SpringApplication createSpringApplication(Object... sources) {
        return new ApplicationStarter(sources);
    }

}
