package lv.ctco.tmm;

import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;

/**
 * Created by S7T4M5 on 2016.05.10..
 */
public interface ITestCase {

    public TestStatus getStatus();

    public String getTestCaseKey();

    public Object getSteps();

    public TestLevel getSeverity();

    public String getName();

}
