package lv.ctco.zephyr.beans;

import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;

public interface TestCase {

    Integer getId();

    void setId(Integer id);

    TestStatus getStatus();

    void setStatus(TestStatus status);

    String getUniqueId();

    void setUniqueId(String uniqueId);

    String getKey();

    void setKey(String key);

    TestLevel getSeverity();

    void setSeverity(TestLevel severity);

    String getName();

    void setName(String name);
}