package lv.ctco.zephyr.util;

import com.google.gson.Gson;
import lv.ctco.zephyr.beans.jira.Login;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import static lv.ctco.zephyr.Config.getValue;
import static lv.ctco.zephyr.enums.ConfigProperty.*;
import static lv.ctco.zephyr.util.ObjectTransformer.deserialize;

public class HttpUtils {
    private static final BasicCookieStore COOKIE_STORE = new BasicCookieStore();

    private static String jSessionId;

    static void setAuthorizationCookie(HttpRequestBase request) throws Exception{
        if(jSessionId == null){
            String json = new Gson().toJson(new Login(getValue(USERNAME), getValue(PASSWORD)));

            CloseableHttpClient httpClient = getHttpClient();
            String uri = getValue(JIRA_URL) + "auth/1/session";
            Utils.log("LOGIN: " + uri);
            HttpPost loginRequest = new HttpPost(uri);
            loginRequest.setHeader("Content-Type", "application/json");
            loginRequest.setEntity(new StringEntity(json));
            CloseableHttpResponse execute = httpClient.execute(loginRequest);
            Login.Response loginResponse = deserialize(Utils.readInputStream(execute.getEntity().getContent()), Login.Response.class);
            jSessionId = loginResponse.getSession().get("value");
        }
    }

    public static CloseableHttpClient getHttpClient() throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultCookieStore(COOKIE_STORE)
                .build();
        return client;
    }

    public static HttpResponse get(String url) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        String uri = getValue(JIRA_URL) + url;
        Utils.log("GET: " + uri);
        HttpGet request = new HttpGet(uri);
        setCommonHeaders(request);
        return httpClient.execute(request);
    }

    private static void setCommonHeaders(HttpRequestBase request) throws Exception {
        request.setHeader("Accept", "application/json");
        //request.setHeader("Authorization", "Basic " + getAuthString());
        setAuthorizationCookie(request);
    }

    public static String getAndReturnBody(String url) throws Exception {
        HttpResponse response = get(url);
        return Utils.readInputStream(response.getEntity().getContent());
    }

    public static HttpResponse post(String url, Object entity) throws Exception {
        String json = new Gson().toJson(entity);

        CloseableHttpClient httpClient = getHttpClient();
        String uri = getValue(JIRA_URL) + url;
        Utils.log("POST: " + uri);
        HttpPost request = new HttpPost(uri);
        setCommonHeaders(request);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));
        return httpClient.execute(request);
    }

    public static HttpResponse put(String url, Object entity) throws Exception {
        String json = new Gson().toJson(entity);

        CloseableHttpClient httpClient = getHttpClient();
        String uri = getValue(JIRA_URL) + url;
        Utils.log("PUT: " + uri);
        HttpPut request = new HttpPut(uri);
        setCommonHeaders(request);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));
        CloseableHttpResponse response = httpClient.execute(request);
        httpClient.close();
        return response;
    }
}