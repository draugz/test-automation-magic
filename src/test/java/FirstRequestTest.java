import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
import lv.ctco.tmm.ReportParser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.preemptive;


/**
 * Created by S7T4M5 on 2016.04.20..
 */
public class FirstRequestTest {

    private static String PASSWORD = "";
    private static String USER_NAME = "s7t4m5";
    private static String BASE_URI = "http://jira..com/";
    private static String PROJECT_NAME = "L&H Client Portal";

    private static String TEST_CYCLE_NAME = "Regression";
    private static String PROJECT_VERSION_NAME = "UW Portal Pilot (June 2016)";
    //private static String PROJECT_VERSION_NAME = "UW Portal Global (Oct 2016)";

    private static String CREATE_NEW_TEST_CYCLE_JSON = "CreateNewCycle_Name_ProjectId_VersionId.json";
    private static String CREATE_NEW_TEST_JSON = "CreateNewTest.json";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.authentication = preemptive().basic(USER_NAME, StringUtils.newStringUtf8(Base64.decodeBase64(PASSWORD)));
    }
    @Test
    public void doTest(){
        ReportParser reportParser = new ReportParser();
        reportParser.convertXmlToJsonAndCreateTC("C:\\Test\\cp\\cp_uw\\sr-cp-selenium-uw\\target\\allure-results\\");
        System.out.println(reportParser.testCaseList.get(2).getName());
        System.out.println(reportParser.testCaseList.get(2).getStatus());
        System.out.println(reportParser.testCaseList.get(2).getJiraKey());
    }

    @Test
    public void createNewCycle() {
        String projectId = getProjectId();

        given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .body(String.format(readFile(CREATE_NEW_TEST_CYCLE_JSON), "Functional", projectId))
                .when()
                .post("rest/zapi/latest/cycle")
                .then().log().everything();
    }

    @Test
    public void createNewTestCase() {
        given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .body(readFile(CREATE_NEW_TEST_JSON))
                .when()
                .post("rest/api/2/issue")
                .then().log().everything();
    }

    @Test
    public void addTestToCycle() {
        given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .body(readFile("LinkTcToCycle.json"))
                .when()
                .post("rest/zapi/latest/execution")
                .then().log().everything();
    }

    @Test
    public void getExecutionIdTest() {
        System.out.println(getExecutionId());
    }

    public int getExecutionId(){
        List<Integer> executionId = JsonPath.parse(given().header("Content-Type", "application/json")
                .when()
                .get("rest/zapi/latest/execution?issueId=661910")
                .then().statusCode(200).log().everything().extract().body().jsonPath().get()).read(String.format("$..[?(@.cycleId=='%s')].id", getCycleId()));
        return executionId.get(0);
    }

    @Test
    public void updateExecutionStatus() {
        given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .body("{\"status\":\"1\"}") //PASSED=1;Failed=2;
                .when()
                .put("rest/zapi/latest/execution/" + getExecutionId() + "/execute")
                .then().log().everything();
    }

    @Test
    public void getIssueTypes() {
        System.out.println(given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .when()
                .get("/rest/api/2/issue/createmeta")
                .then().extract().response().body().print());
    }

    @Test
    public void getIssueId() {
        System.out.println(given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .when()
                .get("/rest/api/2/issue/LHCP-1381")
                .then().log().everything().extract().response().body().jsonPath().getString("id"));
    }

    @Test
    public void getProjectsIdByName() {
        System.out.println(getProjectId());
    }

    @Test
    public void getProjectsKeyByName() {
        System.out.println(getProjectKey());
    }


    @Test
    public void getTestCycleIdByName() {
        System.out.println(getTestCycleId());
    }

    public Integer getTestCycleId() {
        List<Integer> testCycleName = JsonPath.parse(given().header("Content-Type", "application/json")
                .when()
                .get("rest/zapi/latest/cycle?projectId=" + getProjectId() + "&versionId=" + getVersionId())
                .then().statusCode(200).log().everything().extract().body().jsonPath().get()).read(String.format("$..[?(@.name=='%s')].versionId", TEST_CYCLE_NAME));
        return testCycleName.get(0);
    }

    public String getProjectId() {
        List<String> projectId = JsonPath.parse(given().header("Content-Type", "application/json")
                .when()
                .get("rest/api/2/project")
                .then().statusCode(200).extract().body().jsonPath().get()).read(String.format("$..[?(@.name=='%s')].id", PROJECT_NAME));
        return projectId.get(0);
    }

    public String getProjectKey() {
        List<String> projectId = JsonPath.parse(given().header("Content-Type", "application/json")
                .when()
                .get("rest/api/2/project")
                .then().statusCode(200).extract().body().jsonPath().get()).read(String.format("$..[?(@.name=='%s')].key", PROJECT_NAME));
        return projectId.get(0);
    }

    static String readFile(String fileName) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get("src\\main\\resources\\" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, Charset.defaultCharset());
    }

    @Test
    public void getVersionIdTest() {
        System.out.println(getVersionId());
    }

    public String getVersionId() {
        List<String> projectId = JsonPath.parse(given().header("Content-Type", "application/json")
                .when()
                .get("/rest/api/2/project/" + getProjectKey() + "/versions")
                .then().statusCode(200).log().everything().extract().body().jsonPath().get()).read(String.format("$.[?(@.name==\"%s\")].id", PROJECT_VERSION_NAME));
        return projectId.get(0);
    }


    @Test
    public void getCycleIdTest() {
        String cycleId=null;
        HashMap<String, HashMap<String, String>> response=given().header("Content-Type", "application/json")
                .when()
                .get("/rest/zapi/latest/cycle?projectId="+ getProjectId()+"&versionId="+getVersionId())
                .then().statusCode(200).log().everything().extract().body().jsonPath().get("$..");
        response.remove("recordsCount");

        for (Map.Entry<String, HashMap<String, String>> c:response.entrySet()){
            if (c.getValue().containsValue(TEST_CYCLE_NAME)) {
                cycleId=c.getKey();
            }
        }
        System.out.println(cycleId);
    }

    public String getCycleId() {
        String cycleId=null;
        HashMap<String, HashMap<String, String>> response=given().header("Content-Type", "application/json")
                .when()
                .get("/rest/zapi/latest/cycle?projectId="+ getProjectId()+"&versionId="+getVersionId())
                .then().statusCode(200).log().everything().extract().body().jsonPath().get("$..");
        response.remove("recordsCount");

        for (Map.Entry<String, HashMap<String, String>> c:response.entrySet()){
            if (c.getValue().containsValue(TEST_CYCLE_NAME)) {
                cycleId=c.getKey();
            }
        }
        return cycleId;
    }

    //&versionId=35430
    //&versionId=31240
}
