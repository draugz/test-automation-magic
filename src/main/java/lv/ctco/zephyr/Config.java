package lv.ctco.zephyr;

import lv.ctco.zephyr.enums.ConfigProperty;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Properties;

public class Config {

    private static Properties properties = loadConfigPropertiesFile("src/main/resources/config.properties");

    private static Properties loadConfigPropertiesFile(String path) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(new File(path))));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load properties file: " + path);
        }
        return properties;
    }

    public static String getValue(ConfigProperty property) throws IOException, URISyntaxException {
        Object value = properties.get(property.getName());
        if (value != null) {
            return String.valueOf(value).trim();
        }
        throw new RuntimeException("Property " + property.name() + " is not found in the config file!");
    }

    public static CloseableHttpClient getHttpClient() throws Exception {
        return HttpClients.createDefault();
    }
}