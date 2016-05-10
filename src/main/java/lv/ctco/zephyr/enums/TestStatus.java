package lv.ctco.zephyr.enums;

public enum TestStatus {
    NOT_EXECUTED(0),
    FAILED(2),
    PASSED(1);

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
        throw new IllegalArgumentException();
    }
}