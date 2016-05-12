package lv.ctco.zephyr.beans;

import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;

public interface ITestCase {

    TestStatus getStatus();

    String getTestCaseKey();

    TestLevel getSeverity();

    String getName();
}