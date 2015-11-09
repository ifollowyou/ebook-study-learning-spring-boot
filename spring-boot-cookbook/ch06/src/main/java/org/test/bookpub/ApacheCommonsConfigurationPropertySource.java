package org.test.bookpub;

import com.google.common.collect.Lists;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.ArrayList;

public class ApacheCommonsConfigurationPropertySource extends EnumerablePropertySource<XMLConfiguration> {
    private static final Log logger = LogFactory.getLog(ApacheCommonsConfigurationPropertySource.class);

    public static final String COMMONS_CONFIG_PROPERTY_SOURCE_NAME = "commonsConfig";

    public ApacheCommonsConfigurationPropertySource(String name, XMLConfiguration source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        ArrayList<String> keys = Lists.newArrayList(this.source.getKeys());
        return keys.toArray(new String[keys.size()]);
    }

    @Override
    public Object getProperty(String name) {
        return this.source.getString(name);
    }

    public static void addToEnvironment(ConfigurableEnvironment environment,
                                        XMLConfiguration xmlConfiguration) {
        environment.getPropertySources().addAfter(
                StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                new ApacheCommonsConfigurationPropertySource(
                        COMMONS_CONFIG_PROPERTY_SOURCE_NAME, xmlConfiguration));
        logger.trace("ApacheCommonsConfigurationPropertySource add to Environment");
    }
}
