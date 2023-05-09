package life.freebao.devops.brokereye.repository;

import life.freebao.devops.brokereye.domain.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, Long> {
    Optional<Captcha> findByMd5Digest(String md5Digest);
    Optional<Captcha> findByOcrResult(String ocrResult);
}
