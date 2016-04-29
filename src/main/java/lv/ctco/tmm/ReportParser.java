package lv.ctco.tmm;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by S7T4M5 on 2016.04.22..
 */
public class ReportParser {

    public List<TestCase> testCaseList = new ArrayList<TestCase>();

    public void createTC(JSONObject json){
        List<String> tcNames=JsonPath.read(json.toString(), "ns2:test-suite.test-cases.test-case[*].name");
        List<String> tcStatus=JsonPath.read(json.toString(), "ns2:test-suite.test-cases.test-case[*].status");
        List<String> tcJiraKey=JsonPath.read(json.toString(), "ns2:test-suite.test-cases.test-case[*].labels.label[?(@.name=='story')].value");


        for (int i=0; i<tcNames.size(); i++){
            testCaseList.add(new TestCase(ifDoesNotExistsSetEmptyValue(tcNames, i), ifDoesNotExistsSetEmptyValue(tcStatus, i), ifDoesNotExistsSetEmptyValue(tcJiraKey, i)));
        }
    }

    public void convertXmlToJsonAndCreateTC(String reportDirectory){
        JSONObject xmlJSONObj = null;
        File folder = new File(reportDirectory);
        File[] listOfFiles = folder.listFiles();
        for(int i = 0; i < listOfFiles.length; i++) {
            String filename = listOfFiles[i].getName();
            if (filename.endsWith(".xml") || filename.endsWith(".XML")) {
                xmlJSONObj = XML.toJSONObject(readFile(reportDirectory+filename));
                createTC(xmlJSONObj);
            }
        }
    }

    public static String readFile(String fileName) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, Charset.defaultCharset());
    }

    public String ifDoesNotExistsSetEmptyValue(List<String> array, int number){
        try {
            return array.get(number);
        }catch (IndexOutOfBoundsException e){
            return "";
        }
    }

    public List<TestCase> getTCList(){
        return testCaseList;
    }
}
