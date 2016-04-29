package lv.ctco.zephyr;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Config {

    public static CloseableHttpClient httpClient = HttpClients.createDefault();
    public static String JIRA_BASE_URL;
    public static String USER_NAME;
    public static String PASSWORD;

    public static CloseableHttpClient getHttpClient() throws Exception {
        return httpClient;
    }
}