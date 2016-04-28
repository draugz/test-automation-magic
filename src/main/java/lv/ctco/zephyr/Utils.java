package lv.ctco.zephyr;

import lv.ctco.zephyr.beans.ResultTestSuite;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class Utils {

    public static ResultTestSuite readJunitXML(String path) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(ResultTestSuite.class);
        URL resource = Utils.class.getProtectionDomain().getCodeSource().getLocation();
        File file = Paths.get(resource.toURI().resolve(path)).toFile();
        return (ResultTestSuite) jaxbContext.createUnmarshaller().unmarshal(file);
    }
}
