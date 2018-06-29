package org.jretty.spconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ResourceLoader;

/**
 * wrap of SpringApplication
 * @see SpringApplication
 * 
 * @author zollty
 * @since 2018-3-2
 */
public class ApplicationStarter extends SpringApplication {

    public ApplicationStarter(Object... sources) {
        super(sources);
    }

    public ApplicationStarter(ResourceLoader resourceLoader, Object... sources) {
        super(resourceLoader, sources);
    }
    
    
    /**
     * Called to run a fully configured {@link SpringApplication}.
     */
    public static void configure(SpringApplication application) {
        
    }
    
    public static SpringApplicationBuilder builder() {
        SpringApplicationBuilder builder = new SpringApplicationBuilderOverride();
        return builder;
    }
    
    /**
     * Static helper that can be used to run a {@link SpringApplication} from the
     * specified source using default settings.
     * @param source the source to load
     * @param args the application arguments (usually passed from a Java main method)
     * @return the running {@link ApplicationContext}
     */
    public static ConfigurableApplicationContext run(Object source, String... args) {
        return run(new Object[] { source }, args);
    }
    
    /**
     * Run the Spring application, creating and refreshing a new
     * {@link ApplicationContext}.
     * @param args the application arguments (usually passed from a Java main method)
     * @return a running {@link ApplicationContext}
     */
    public ConfigurableApplicationContext run(String... args) {
        ApplicationStarter.configure(this);
        this.addListeners(new EnvironmentPropertyPostProcessor());
        return super.run(args);
    }

    /**
     * Static helper that can be used to run a {@link SpringApplication} from the
     * specified sources using default settings and user supplied arguments.
     * @param sources the sources to load
     * @param args the application arguments (usually passed from a Java main method)
     * @return the running {@link ApplicationContext}
     */
    public static ConfigurableApplicationContext run(Object[] sources, String[] args) {
        return new ApplicationStarter(sources).run(args);
    }

    /**
     * A basic main that can be used to launch an application. This method is useful when
     * application sources are defined via a {@literal --spring.main.sources} command line
     * argument.
     * <p>
     * Most developers will want to define their own main method and call the
     * {@link #run(Object, String...) run} method instead.
     * @param args command line arguments
     * @throws Exception if the application cannot be started
     * @see SpringApplication#run(Object[], String[])
     * @see SpringApplication#run(Object, String...)
     */
    public static void main(String[] args) throws Exception {
        ApplicationStarter.run(new Object[0], args);
    }
    
    private static class SpringApplicationBuilderOverride extends SpringApplicationBuilder {
        
        @Override
        protected SpringApplication createSpringApplication(Object... sources) {
            return new ApplicationStarter(sources);
        }

    }

}
