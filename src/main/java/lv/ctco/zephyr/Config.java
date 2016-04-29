package lv.ctco.zephyr;

import lv.ctco.zephyr.enums.ConfigProperty;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Properties;

import static lv.ctco.zephyr.util.Utils.resolveFile;

public class Config {

    private static Properties properties = loadConfigPropertiesFile("config.properties");

    public static CloseableHttpClient httpClient = HttpClients.createDefault();

    private static Properties loadConfigPropertiesFile(String path) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(resolveFile(path))));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load properties file: " + path);
        }
        return properties;
    }

    public static String getValue(ConfigProperty property) throws IOException, URISyntaxException {
        Object value = properties.get(property.getName());
        return value != null ? String.valueOf(value) : null;
    }

    public static CloseableHttpClient getHttpClient() throws Exception {
        return httpClient;
    }
}