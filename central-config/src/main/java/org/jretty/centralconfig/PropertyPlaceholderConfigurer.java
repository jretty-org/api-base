package org.jretty.centralconfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * 配置中心,获取各种配置
 * 
 * @author zollty
 * @since 2016-9-7
 */
public class PropertyPlaceholderConfigurer
        extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
        implements InitializingBean, DisposableBean {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyPlaceholderConfigurer.class);

    protected ConfigHolder configService;

    protected Resource[] locations;

    @Override
    protected void loadProperties(Properties props) throws IOException {
        configService.loadProperties(props);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LocalPropsService localPropsService = new LocalPropsService();

        Map<String, Properties> propsMap = new LinkedHashMap<String, Properties>();

        for (int i = 0; i < locations.length; i++) {
            Properties tmp = getProperties(locations[i]);
            propsMap.put(locations[i].getFilename(), tmp);
        }

        localPropsService.setLocalProperties(propsMap);

        ConfigSyncManager manger = new ConfigSyncManager(localPropsService)
                .setChangedListener(new ConfigChangedLogListener());

        this.configService = manger.getConfig();
    }

    @Override
    public void destroy() throws Exception {
    }

    @Override
    public void setLocation(Resource location) {
        this.locations = new Resource[] { location };
        super.setLocation(location);
    }

    @Override
    public void setLocations(Resource[] locations) {
        this.locations = locations;
        super.setLocations(locations);
    }

    /**
     * 获取Properties资源
     */
    private static Properties getProperties(Resource location) {
        InputStream io = null;
        try {
            io = location.getInputStream();
            Properties tempProps = new Properties();
            tempProps.load(io);
            return tempProps;
        } catch (IOException e) {
            LOG.warn("getProperties error due to ", e);
        } finally {
            if (io != null) {
                try {
                    io.close();
                } catch (IOException e) {
                    // ignore...
                }
            }
        }
        return null;
    }

    public ConfigHolder getConfigHolder() {
        return configService;
    }
}