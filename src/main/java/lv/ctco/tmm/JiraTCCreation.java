package lv.ctco.tmm;

import com.jayway.restassured.RestAssured;
import lv.ctco.tmm.utils.ConfigReader;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.preemptive;

/**
 * Created by S7T4M5 on 2016.04.23..
 */
public class JiraTCCreation {

    private static String PASSWORD = ConfigReader.getValueByKey("password");
    private static String USER_NAME = ConfigReader.getValueByKey("userName");
    private static String BASE_URI = ConfigReader.getValueByKey("base_url");
    private static String PROJECT_NAME = ConfigReader.getValueByKey("projectName");

    private static String TEST_CYCLE_NAME = ConfigReader.getValueByKey("testCycleName");
    private static String PROJECT_VERSION_NAME = ConfigReader.getValueByKey("projectVerionName");

    private static String PERORT_DIRECTORY = ConfigReader.getValueByKey("reportDirectory");

    private static String CREATE_NEW_TEST_CYCLE_JSON = "CreateNewCycle_Name_ProjectId_VersionId.json";
    private static String CREATE_NEW_TEST_JSON = "CreateNewTest.json";

    public void de(){
        RestAssured.baseURI = BASE_URI;
        RestAssured.authentication = preemptive().basic(USER_NAME, StringUtils.newStringUtf8(Base64.decodeBase64(PASSWORD)));

        ReportParser reportParser = new ReportParser();
        reportParser.convertXmlToJsonAndCreateTC(PERORT_DIRECTORY);

        for (TestCase currentTC:reportParser.getTCList()){
            if(!currentTC.getId().equals("")){

            }else {

            }
        }
    }

    public void createTestCaseAndLinkToTestCysle(){
        given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .body(String.format(ConfigReader.readFile(CREATE_NEW_TEST_CYCLE_JSON), "Functional", ""))
                .when()
                .post("rest/zapi/latest/cycle")
                .then().log().everything();
    }


    public void existingTestCaseLinkToTestCysle(){

    }
}
