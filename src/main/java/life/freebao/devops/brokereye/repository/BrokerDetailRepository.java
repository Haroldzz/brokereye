package life.freebao.devops.brokereye.repository;

import life.freebao.devops.brokereye.domain.BrokerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokerDetailRepository extends JpaRepository<BrokerDetail, Long> {

}
