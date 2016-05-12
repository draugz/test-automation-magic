package lv.ctco.zephyr.util;

import lv.ctco.zephyr.beans.testresult.junit.JUnitResult;
import lv.ctco.zephyr.beans.testresult.junit.JUnitResultTestSuite;
import ru.yandex.qatools.allure.commons.AllureFileUtils;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXBContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

public class Utils {

    public static void log(String msg) {
        System.out.println("##### " + msg);
    }

    public static JUnitResultTestSuite readJUnitReport(String path) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(JUnitResultTestSuite.class);
        return (JUnitResultTestSuite) jaxbContext.createUnmarshaller().unmarshal(new File(path));
    }

    public static List<TestSuiteResult> readAllureReport(String path) throws IOException, URISyntaxException {
        return AllureFileUtils.unmarshalSuites(new File(path));
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

    public static String generateJiraKey(JUnitResult testCase) {
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
