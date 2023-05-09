package life.freebao.devops.brokereye.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serial;
import java.time.Instant;

@Entity
@Table(name = "t_broker_detail")
public class BrokerDetail extends AbstractAuditingEntity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "begin_date")
    private Instant beginDate;
    @Column(name = "end_date")
    private Instant endDate;
    @Column(name = "institution_type")
    private Integer institutionType;
    @Column(name = "apply_date")
    private Instant applyDate;
    @Column(name = "business_scope")
    private String businessScope;
    @Column(name = "online_sales")
    private boolean onlineSales = false;
    @Column(name = "activated")
    private boolean activated = false;
    @Column(name = "person_id")
    private String personId; //verified
    @Column(name = "verified")
    private boolean verified = false;
    @Min(value = 1, message = "Incorrect gender")
    @Max(value = 2, message = "Incorrect gender") //1man2woman
    @Column(name = "gender")
    private Integer gender;
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "brokerDetail")
    private Broker broker;

    public BrokerDetail() {
    }

    public BrokerDetail(Long id, Instant beginDate, Instant endDate, Integer institutionType, Instant applyDate, String businessScope, boolean onlineSales, boolean activated, String personId, boolean verified, Integer gender, Broker broker) {
        this.id = id;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.institutionType = institutionType;
        this.applyDate = applyDate;
        this.businessScope = businessScope;
        this.onlineSales = onlineSales;
        this.activated = activated;
        this.personId = personId;
        this.verified = verified;
        this.gender = gender;
        this.broker = broker;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Instant beginDate) {
        this.beginDate = beginDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(Integer institutionType) {
        this.institutionType = institutionType;
    }

    public Instant getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Instant applyDate) {
        this.applyDate = applyDate;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public boolean isOnlineSales() {
        return onlineSales;
    }

    public void setOnlineSales(boolean onlineSales) {
        this.onlineSales = onlineSales;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BrokerDetail)) return false;
        return id != null && id.equals(((BrokerDetail) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "BrokerDetail{" +
                "id=" + id +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", institutionType=" + institutionType +
                ", applyDate=" + applyDate +
                ", businessScope='" + businessScope + '\'' +
                ", onlineSales=" + onlineSales +
                ", activated=" + activated +
                ", personId='" + personId + '\'' +
                ", verified=" + verified +
                ", gender=" + gender +
                '}';
    }
}
