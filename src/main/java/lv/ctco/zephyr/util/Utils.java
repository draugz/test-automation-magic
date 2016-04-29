package lv.ctco.zephyr.util;

import lv.ctco.zephyr.beans.ResultTestSuite;

import javax.xml.bind.JAXBContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;

public class Utils {

    public static ResultTestSuite readJunitXML(String path) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(ResultTestSuite.class);
        URL resource = Utils.class.getProtectionDomain().getCodeSource().getLocation();
        File file = Paths.get(resource.toURI().resolve(path)).toFile();
        return (ResultTestSuite) jaxbContext.createUnmarshaller().unmarshal(file);
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
}
