package lv.ctco.zephyr.enums;

public enum TestStatus {
    NOT_EXECUTED(1),
    FAILED(2),
    PASSED(3);

    private int id;

    TestStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TestStatus findById(int id) {
        for (TestStatus status : values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        return null;
    }
}