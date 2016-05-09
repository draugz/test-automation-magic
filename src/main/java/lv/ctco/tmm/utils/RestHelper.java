package lv.ctco.tmm.utils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.preemptive;

/**
 * Created by S7T4M5 on 2016.05.05..
 */
public class RestHelper {

    private static String PROJECT_NAME = ConfigReader.getValueByKey("projectName");
    private static String TEST_CYCLE_NAME = ConfigReader.getValueByKey("testCycleName");
    private static String PROJECT_VERSION_NAME = ConfigReader.getValueByKey("projectVersionName");

    private static String PASSWORD = ConfigReader.getValueByKey("password");
    private static String USER_NAME = ConfigReader.getValueByKey("userName");
    private static String BASE_URI = ConfigReader.getValueByKey("base_url");

    private static String CREATE_NEW_TEST_JSON = "CreateNewTest.json";
    private static String LINK_TO_TEST_CYCLE_JSON = "LinkTcToCycle.json";
    private static String UPDATE_EXECUTION_STATUS_JSON = "UpdateExecutionStatus.json";

    private String projectId = null;
    private String projectKey = null;
    private String versionId = null;
    private String cycleId = null;

    public RestHelper() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.authentication = preemptive().basic(USER_NAME, StringUtils.newStringUtf8(Base64.decodeBase64(PASSWORD)));

        projectId = getProjectId();
        projectKey = getProjectKey();
        versionId = getVersionId();
        cycleId = getCycleId();
    }

    public String createTestCase(String testCaseName, int serenity) {
        return given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .body(String.format(ConfigReader.readFile(CREATE_NEW_TEST_JSON), getProjectId(), testCaseName, USER_NAME, USER_NAME, serenity))
                .when()
                .post("rest/api/2/issue")
                .then().log().everything().extract().response().jsonPath().get("id");
    }

    public void addToTestCycle(String issueId) {
        given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .body(String.format(ConfigReader.readFile(LINK_TO_TEST_CYCLE_JSON), issueId, versionId, cycleId, projectId))
                .when()
                .post("rest/zapi/latest/execution")
                .then().log().everything();
    }

    public String getProjectId() {
        List<String> projectId = JsonPath.parse(given().header("Content-Type", "application/json")
                .when()
                .get("rest/api/2/project")
                .then().statusCode(200).extract().body().jsonPath().get()).read(String.format("$..[?(@.name=='%s')].id", PROJECT_NAME));
        return projectId.get(0);
    }

    public String getProjectKey() {
        List<String> projectKey = JsonPath.parse(given().header("Content-Type", "application/json")
                .when()
                .get("rest/api/2/project")
                .then().statusCode(200).extract().body().jsonPath().get()).read(String.format("$..[?(@.name=='%s')].key", PROJECT_NAME));
        return projectKey.get(0);
    }

    public String getVersionId() {
        List<String> projectId = JsonPath.parse(given().header("Content-Type", "application/json")
                .when()
                .get(String.format("/rest/api/2/project/%s/versions", projectKey))
                .then().statusCode(200).log().everything().extract().body().jsonPath().get()).read(String.format("$.[?(@.name==\"%s\")].id", PROJECT_VERSION_NAME));
        return projectId.get(0);
    }

    public String getCycleId() {
        String cycleId = null;
        HashMap<String, HashMap<String, String>> response = given().header("Content-Type", "application/json")
                .when()
                .get(String.format("/rest/zapi/latest/cycle?projectId=%s&versionId=%s", projectId, versionId))
                .then().statusCode(200).log().everything().extract().body().jsonPath().get("$..");
        response.remove("recordsCount");

        for (Map.Entry<String, HashMap<String, String>> c : response.entrySet()) {
            if (c.getValue().containsValue(TEST_CYCLE_NAME)) {
                cycleId = c.getKey();
            }
        }
        return cycleId;
    }

    public String getIssueId(String jiraKey) {
        return given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .when()
                .get(String.format("/rest/api/2/issue/%s", jiraKey))
                .then().log().everything().extract().response().body().jsonPath().getString("id");
    }

    public int getExecutionId(String testCaseId) {
        List<Integer> executionId = JsonPath.parse(given().header("Content-Type", "application/json")
                .when()
                .get(String.format("rest/zapi/latest/execution?issueId=%s", testCaseId))
                .then().statusCode(200).log().everything().extract().body().jsonPath().get()).read(String.format("$..[?(@.cycleId=='%s')].id", cycleId));
        return executionId.get(0);
    }

    public void updateExecutionStatus(int executionStatus, int executionId) {
        given().header("X-Atlassian-Token", "nocheck")
                .header("Content-Type", "application/json")
                .body(String.format(ConfigReader.readFile(UPDATE_EXECUTION_STATUS_JSON), executionStatus)) //PASSED=1;Failed=2;
                .when()
                .put(String.format("rest/zapi/latest/execution/%s/execute", executionId))
                .then().log().everything();
    }
}
