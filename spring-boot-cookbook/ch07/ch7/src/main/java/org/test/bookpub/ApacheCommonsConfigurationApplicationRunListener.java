package org.test.bookpub;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class ApacheCommonsConfigurationApplicationRunListener implements SpringApplicationRunListener{

    public ApacheCommonsConfigurationApplicationRunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void started() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        try {
            ApacheCommonsConfigurationPropertySource.addToEnvironment(environment,
                    new XMLConfiguration("commons-config.xml"));
        } catch (ConfigurationException e) {
            throw new RuntimeException("Unable to load commons-config.xml", e);
        }
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {

    }
}
