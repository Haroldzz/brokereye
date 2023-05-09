package life.freebao.devops.brokereye.web.error;

import java.net.URI;

public final class ErrorConstant {
    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.lhd18.com/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_ID_NUMBER_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-id-number");
    private ErrorConstant() {}

}
