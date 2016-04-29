package lv.ctco.zephyr.enums;

public enum TestLevel {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4);

    private int id;

    TestLevel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TestLevel findById(int id) {
        for (TestLevel status : values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        return null;
    }
}