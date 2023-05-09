package life.freebao.devops.brokereye.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serial;

@Entity
@Table(name = "t_captcha")
public class Captcha extends AbstractAuditingEntity<Long> {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 32, max = 32)
    @Column(name = "md5_digest",length = 32,unique = true,nullable = false)
    private String md5Digest;
    @Size(min = 4, max = 4)
    @Column(name = "ocr_result",length = 4,nullable = false)
    private String ocrResult;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMd5Digest() {
        return md5Digest;
    }

    public void setMd5Digest(String md5Digest) {
        this.md5Digest = md5Digest;
    }

    public String getOcrResult() {
        return ocrResult;
    }

    public void setOcrResult(String ocrResult) {
        this.ocrResult = ocrResult;
    }

    public Captcha() {
    }

    public Captcha(Long id, String md5Digest, String ocrResult) {
        this.id = id;
        this.md5Digest = md5Digest;
        this.ocrResult = ocrResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Captcha)) return false;
        return id != null && id.equals(((Captcha) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Captcha{" +
                "id=" + id +
                ", md5Digest='" + md5Digest + '\'' +
                ", ocrResult='" + ocrResult + '\'' +
                '}';
    }
}
