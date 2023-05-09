package life.freebao.devops.brokereye.repository;

import life.freebao.devops.brokereye.domain.Broker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, Long> {
    List<Broker> findAllByName(String name);
    List<Broker> findAllByIdNumber(String idNumber);
    List<Broker> findAllByNameAndIdNumber(String name, String idNumber);
    Optional<Broker> findFirstByNameAndIdNumberOrderByCreatedDateDesc(String name, String idNumber);
    Optional<Broker> findFirstByNameAndIdNumberAndBrokerDetailVerifiedOrderByLastModifiedDateDesc(String name, String idNumber, boolean verified);
    Page<Broker> findAllByIdNotNullAndPracticeNumberNotNull(Pageable pageable);
    Page<Broker> findAllByIdNotNullAndPracticeNumberIsNull(Pageable pageable);
    @Query(value = "SELECT * FROM t_broker GROUP BY name, id_number HAVING MAX(last_modified_date) < ?1", nativeQuery = true)
    List<Broker> findAllByGroupByNameAndIdNumberHavingLastModifiedDateLessThan(Instant dateTime);
}
