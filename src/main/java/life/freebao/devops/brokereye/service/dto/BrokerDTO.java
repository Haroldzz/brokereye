package life.freebao.devops.brokereye.service.dto;

import java.io.Serial;
import java.io.Serializable;

public class BrokerDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String idNumber;
    private String practiceNumber;
    private String practiceArea;
    private String insuranceInstitute;

    public BrokerDTO() {
    }

    public BrokerDTO(Long id, String name, String idNumber, String practiceNumber, String practiceArea, String insuranceInstitute) {
        this.id = id;
        this.name = name;
        this.idNumber = idNumber;
        this.practiceNumber = practiceNumber;
        this.practiceArea = practiceArea;
        this.insuranceInstitute = insuranceInstitute;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPracticeNumber() {
        return practiceNumber;
    }

    public void setPracticeNumber(String practiceNumber) {
        this.practiceNumber = practiceNumber;
    }

    public String getPracticeArea() {
        return practiceArea;
    }

    public void setPracticeArea(String practiceArea) {
        this.practiceArea = practiceArea;
    }

    public String getInsuranceInstitute() {
        return insuranceInstitute;
    }

    public void setInsuranceInstitute(String insuranceInstitute) {
        this.insuranceInstitute = insuranceInstitute;
    }

    @Override
    public String toString() {
        return "BrokerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", practiceNumber='" + practiceNumber + '\'' +
                ", practiceArea='" + practiceArea + '\'' +
                ", insuranceInstitute='" + insuranceInstitute + '\'' +
                '}';
    }
}
