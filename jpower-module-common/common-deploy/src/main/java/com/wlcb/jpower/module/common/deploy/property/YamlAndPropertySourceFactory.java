package com.wlcb.jpower.module.common.deploy.property;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author mr.g
 * @date 2021-06-07 16:44
 */
public class YamlAndPropertySourceFactory extends DefaultPropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null) {
            return super.createPropertySource(name, resource);
        }
        Resource resourceResource = resource.getResource();
        if (!resourceResource.exists()) {
            return new PropertiesPropertySource(null, new Properties());
        } else if (resourceResource.getFilename().endsWith(".yml") || resourceResource.getFilename().endsWith(".yaml")) {
            return loadYaml(name,resource);
        }
        return super.createPropertySource(name, resource);
    }

    private PropertySource<?> loadYaml(String name, EncodedResource resource) throws IOException {
        Resource res = resource.getResource();
        List<PropertySource<?>> sources = new YamlPropertySourceLoader()
                .load(res.getFilename(), res);
        return sources.get(0);
    }
}
