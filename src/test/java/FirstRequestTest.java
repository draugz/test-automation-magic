import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
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
    private static String USER_NAME = "";
    private static String BASE_URI = "";
    private static String PROJECT_NAME = "L&H Client Portal";

    private static String TEST_CYCLE_NAME = "Regression";
    //private static String PROJECT_VERSION_NAME = "UW Portal Pilot (June 2016)";
    private static String PROJECT_VERSION_NAME = "UW Portal Global (Oct 2016)";

    private static String CREATE_NEW_TEST_CYCLE_JSON = "CreateNewCycle_Name_ProjectId_VersionId.json";
    private static String CREATE_NEW_TEST_JSON = "CreateNewTest.json";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.authentication = preemptive().basic(USER_NAME, StringUtils.newStringUtf8(Base64.decodeBase64(PASSWORD)));
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
        String projectId = getProjectId();

        System.out.println(readFile(CREATE_NEW_TEST_JSON));
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
    public void getExecution() {
        given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .when()
                .get("rest/zapi/latest/execution/")
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
    public void getCycleId() {
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


    //&versionId=35430
    //&versionId=31240
}
