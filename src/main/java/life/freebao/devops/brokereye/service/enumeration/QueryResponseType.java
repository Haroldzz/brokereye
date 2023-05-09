package life.freebao.devops.brokereye.service.enumeration;

public enum QueryResponseType {
    SUCCESS("insurance.do?query", true),
    NONE("insurance.do?validate", false),
    CAPTCHA("insurance.do?checkcaptch", false);

    private final String response;
    private final boolean status;

    public String getResponse() {
        return response;
    }

    public boolean isStatus() {
        return status;
    }

    QueryResponseType(String response, boolean status) {
        this.response = response;
        this.status = status;
    }
}
