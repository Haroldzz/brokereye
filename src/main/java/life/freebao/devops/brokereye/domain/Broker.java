package life.freebao.devops.brokereye.domain;


import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.util.Locale;

@Entity
@Table(name = "t_broker")
public class Broker extends AbstractAuditingEntity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(max = 50)
    @Column(name = "name",length = 50, nullable = false)
    private String name;
    @NotNull
    @Size(min = 18, max = 18)
    @Column(name = "id_number", length = 18, nullable = false)
    private String idNumber;
    @Size(min = 20, max = 50)
    @Column(name = "practice_number", length = 50)
    private String practiceNumber;
    @Size(max = 128)
    @Column(name = "practice_area", length = 128)
    private String practiceArea;
    @Size(max = 128)
    @Column(name = "insurance_institute", length = 128)
    private String insuranceInstitute;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER,optional = false)
//    @OneToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "broker_detail_id",referencedColumnName = "id")
    private BrokerDetail brokerDetail;

    @Override
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
        this.idNumber = StringUtils.upperCase(idNumber, Locale.ENGLISH);
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

    public BrokerDetail getBrokerDetail() {
        return brokerDetail;
    }

    public void setBrokerDetail(BrokerDetail brokerDetail) {
        this.brokerDetail = brokerDetail;
    }

    public Broker() {
    }

    public Broker(Long id, String name, String idNumber, String practiceNumber, String practiceArea, String insuranceInstitute, BrokerDetail brokerDetail) {
        this.id = id;
        this.name = name;
        this.idNumber = idNumber;
        this.practiceNumber = practiceNumber;
        this.practiceArea = practiceArea;
        this.insuranceInstitute = insuranceInstitute;
        this.brokerDetail = brokerDetail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Broker)) return false;
        return id != null && id.equals(((Broker) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Broker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", practiceNumber='" + practiceNumber + '\'' +
                ", practiceArea='" + practiceArea + '\'' +
                ", insuranceInstitute='" + insuranceInstitute + '\'' +
                '}';
    }
}
