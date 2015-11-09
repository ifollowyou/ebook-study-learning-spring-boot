package org.test.bookpub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.test.bookpubstarter.dbcount.EnableDbCounting;

@Configuration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = @ComponentScan.Filter(UsedForTesting.class))
@EnableScheduling
@EnableDbCounting
public class BookPubApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookPubApplication.class, args);
    }

    @Bean
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
}

@interface UsedForTesting {

}