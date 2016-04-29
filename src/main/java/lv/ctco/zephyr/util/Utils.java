package lv.ctco.zephyr.util;

import lv.ctco.zephyr.beans.ResultTestCase;
import lv.ctco.zephyr.beans.ResultTestSuite;

import javax.xml.bind.JAXBContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class Utils {

    public static void log(String msg) {
        System.out.println("##### " + msg);
    }

    public static ResultTestSuite readCucumberReport(String path) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(ResultTestSuite.class);
        return (ResultTestSuite) jaxbContext.createUnmarshaller().unmarshal(resolveFile(path));
    }

    public static String readInputStream(InputStream is) throws IOException {
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();
        return response.toString();
    }

    public static File resolveFile(String path) throws IOException, URISyntaxException {
        URL resource = Utils.class.getProtectionDomain().getCodeSource().getLocation();
        return Paths.get(resource.toURI().resolve(path)).toFile();
    }

    public static String generateJiraKey(ResultTestCase testCase) {
        return String.join("-", normalizeKey(testCase.getClassname()), normalizeKey(testCase.getName()));
    }

    private static String normalizeKey(String input) {
        if (input != null) {
            String[] tokens = input.split(" ");
            input = "";
            for (String token : tokens) {
                input += token.substring(0, 1).toUpperCase();
            }
        }
        return input;
    }
}
