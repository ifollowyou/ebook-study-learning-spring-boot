package org.test.bookpub;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.test.bookpubstarter.dbcount.EnableDbCounting;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@Configuration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = @ComponentScan.Filter(UsedForTesting.class))
@EnableScheduling
@EnableDbCounting
@SuppressWarnings({"restriction", "UnusedDeclaration"})
public class BookPubApplication {

    public static void main(String[] args) {
        Reloader reloader = new Reloader();
        Signal.handle(new Signal("HUP"), reloader);
        SpringApplication.run(BookPubApplication.class, args);
    }

    static class Reloader implements SignalHandler {
        @Override
        public void handle(Signal sig) {
            System.out.println("Reloading data...!!!");
        }
    }

    @Bean
    @Profile("logger")
    public StartupRunner schedulerRunner() {
        return new StartupRunner();
    }

//    protected final Log logger = LogFactory.getLog(getClass());
//    @Bean
//    public DbCountRunner dbCountRunner(Collection<CrudRepository> repositories) {
//        return new DbCountRunner(repositories) {
//            @Override
//            public void run(String... args) throws Exception {
//                logger.info("Manually Declared DbCountRunner");
//            }
//        };
//    }

    @Bean
    public CommandLineRunner configValuePrinter(
            @Value("${my.config.value:}") String configValue) {
        return args ->
                LogFactory.getLog(getClass()).
                        info("Value of my.config.value property is: " + configValue);
    }
}

@interface UsedForTesting {

}