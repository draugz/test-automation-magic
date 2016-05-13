package lv.ctco.zephyr.util;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URISyntaxException;

import static lv.ctco.zephyr.Config.getValue;
import static lv.ctco.zephyr.enums.ConfigProperty.JIRA_URL;
import static lv.ctco.zephyr.enums.ConfigProperty.PASSWORD;
import static lv.ctco.zephyr.enums.ConfigProperty.USERNAME;

public class HttpUtils {

    public static CloseableHttpClient getHttpClient() throws Exception {
        return HttpClients.createDefault();
    }

    public static HttpResponse get(String url) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        HttpGet request = new HttpGet(getValue(JIRA_URL) + url);
        setCommonHeaders(request);
        return httpClient.execute(request);
    }

    private static void setCommonHeaders(HttpRequestBase request) throws IOException, URISyntaxException {
        request.setHeader("Accept", "application/json");
        request.setHeader("Authorization", "Basic " + getAuthString());
    }

    private static String getAuthString() throws IOException, URISyntaxException {
        String auth = String.format("%s:%s", getValue(USERNAME), getValue(PASSWORD));
        byte[] bytesEncoded = Base64.encodeBase64(auth.getBytes());
        return new String(bytesEncoded);
    }

    public static String getAndReturnBody(String url) throws Exception {
        HttpResponse response = get(url);
        return Utils.readInputStream(response.getEntity().getContent());
    }

    public static HttpResponse post(String url, Object entity) throws Exception {
        String json = new Gson().toJson(entity);

        CloseableHttpClient httpClient = getHttpClient();
        HttpPost request = new HttpPost(getValue(JIRA_URL) + url);
        setCommonHeaders(request);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));
        return httpClient.execute(request);
    }

    public static HttpResponse put(String url, Object entity) throws Exception {
        String json = new Gson().toJson(entity);

        CloseableHttpClient httpClient = getHttpClient();
        HttpPut request = new HttpPut(getValue(JIRA_URL) + url);
        setCommonHeaders(request);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));
        CloseableHttpResponse response = httpClient.execute(request);
        httpClient.close();
        return response;
    }
}