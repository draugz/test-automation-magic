package lv.ctco.zephyr.util;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import lv.ctco.zephyr.Config;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import static lv.ctco.zephyr.Config.getValue;
import static lv.ctco.zephyr.enums.ConfigProperty.JIRA_URL;
import static lv.ctco.zephyr.enums.ConfigProperty.PASSWORD;
import static lv.ctco.zephyr.enums.ConfigProperty.USERNAME;

public class HttpUtils {

    public static HttpResponse get(String url) throws Exception {
        CloseableHttpClient httpClient = Config.getHttpClient();
        HttpGet request = new HttpGet(getValue(JIRA_URL) + url);
        setCommonHeaders(request);
        return httpClient.execute(request);
    }

    private static void setCommonHeaders(HttpRequestBase request) throws IOException, URISyntaxException {
        request.setHeader("Authorization", String.format("Basic %s", getAuthString()));
        request.setHeader("Accept", "application/json");
    }

    private static String getAuthString() throws IOException, URISyntaxException {
        String auth = String.format("%s:%s", getValue(USERNAME), getValue(PASSWORD));
        byte[] bytesEncoded = Base64.encodeBase64(auth.getBytes());
        return new String(bytesEncoded);
    }

    public static String getAndReturnBody(String url) throws Exception {
        HttpResponse response = get(url);
        InputStream content = response.getEntity().getContent();
        return Utils.readInputStream(content);
    }

    public static HttpResponse post(String url, Object content) throws Exception {
        String json = new Gson().toJson(content);

        CloseableHttpClient httpClient = Config.getHttpClient();
        HttpPost request = new HttpPost(getValue(JIRA_URL) + url);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));

        HttpResponse response = httpClient.execute(request);
        httpClient.close();
        return response;
    }

    public static Object jsonValue(String body, String path) {
        return JsonPath.parse(body).read(path);
    }

    public static String getStringValue(String body, String path) {
        return String.valueOf(jsonValue(body, path));
    }

    public static Long getLongValue(String body, String path) {
        Object obj = jsonValue(body, path);
        if (obj instanceof Integer) {
            return Long.valueOf((Integer) obj);
        } else if (obj instanceof String) {
            return Long.valueOf((String) obj);
        } else {
            return (Long) obj;
        }
    }
}