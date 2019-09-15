package com.zhitar.library.connection;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PropertiesHolder {

    private static final Logger LOG = Logger.getLogger(PropertiesHolder.class);

    private List<ResourceBundle> resources = new ArrayList<>();

    public void loadProperties(String propFile) {
        LOG.info("load properties from " + propFile + ".properties");
        if (propFile == null || propFile.trim().length() == 0) {
            LOG.error("Illegal properties file name");
            throw new IllegalArgumentException("Properties file mustn't be null or blank!");
        }
        ResourceBundle bundle = ResourceBundle.getBundle(propFile);
        LOG.trace("Load bundle with keys " + bundle.keySet());
        resources.add(bundle);
    }

    public String getValue(String key) {
        for (ResourceBundle resource : resources) {
            if (resource.containsKey(key)) {
                String value = resource.getString(key);
                LOG.debug("getValue " + value + " for key " + key);
                return value;
            }
        }
        LOG.error("Couldn't find given key: " + key);
        throw new IllegalArgumentException("Given key " + key + " does not present");
    }
}
