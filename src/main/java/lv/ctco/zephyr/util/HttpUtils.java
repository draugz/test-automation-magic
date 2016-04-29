package lv.ctco.zephyr.util;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import lv.ctco.zephyr.Config;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.InputStream;

public class HttpUtils {

    public static HttpResponse get(String url) throws Exception {
        CloseableHttpClient httpClient = Config.getHttpClient();
        HttpGet request = new HttpGet(Config.JIRA_BASE_URL + url);
        request.setHeader("Accept", "application/json");
        HttpResponse response = httpClient.execute(request);
        httpClient.close();
        return response;
    }

    public static String getAndReturnBody(String url) throws Exception {
        HttpResponse response = get(url);
        InputStream content = response.getEntity().getContent();
        return Utils.readInputStream(content);
    }

    public static HttpResponse post(String url, Object content) throws Exception {
        String json = new Gson().toJson(content);

        CloseableHttpClient httpClient = Config.getHttpClient();
        HttpPost request = new HttpPost(Config.JIRA_BASE_URL + url);
        request.setHeader("Accept", "application/json");
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