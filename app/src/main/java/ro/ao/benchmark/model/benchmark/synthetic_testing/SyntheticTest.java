package ro.ao.benchmark.model.benchmark.synthetic_testing;

public enum SyntheticTest {
    IO_TEST("IO Test"),
    DATABASE_TEST("Database Test"),
    FLOATING_POINT_TEST("Floating Point Test"),
    NETWORK_TEST("Network Test");

    private String details;

    private SyntheticTest(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
