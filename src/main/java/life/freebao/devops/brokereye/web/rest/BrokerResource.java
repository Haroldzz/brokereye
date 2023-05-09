package life.freebao.devops.brokereye.web.rest;

import cn.hutool.core.util.IdcardUtil;
import life.freebao.devops.brokereye.domain.Broker;
import life.freebao.devops.brokereye.repository.BrokerRepository;
import life.freebao.devops.brokereye.service.BrokerService;
import life.freebao.devops.brokereye.web.error.BadRequestAlertException;
import life.freebao.devops.brokereye.web.error.InvalidIdNumberException;
import life.freebao.devops.brokereye.service.dto.BrokerDTO;
import life.freebao.devops.brokereye.service.mapper.BrokerMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class BrokerResource {
    private final Logger log = LoggerFactory.getLogger(BrokerResource.class);
    @Value("${spring.application.name}")
    private String applicationName;
    private final BrokerService brokerService;
    private final BrokerRepository brokerRepository;
    private final BrokerMapper brokerMapper;
    public BrokerResource(BrokerService brokerService, BrokerRepository brokerRepository, BrokerMapper brokerMapper) {
        this.brokerService = brokerService;
        this.brokerRepository = brokerRepository;
        this.brokerMapper = brokerMapper;
    }
    @PostMapping("/brokers")
    public ResponseEntity<BrokerDTO> createBroker(@Valid @RequestBody BrokerDTO brokerDTO) throws IOException, InterruptedException, URISyntaxException {
        log.info("REST request for Broker: {}", brokerDTO);
        Broker newBroker = new Broker();
        if (!IdcardUtil.isValidCard(brokerDTO.getIdNumber())) {
            throw new InvalidIdNumberException();
        } else if (StringUtils.isBlank(brokerDTO.getName())){
            throw new BadRequestAlertException("A new broker name must not be null", "brokerManagement", "namenon-exists");
        } else if (brokerDTO.getId() != null) {
            throw new BadRequestAlertException("A new broker cannot already have an ID", "brokerManagement", "idexists");
        } else if (brokerRepository.findFirstByNameAndIdNumberAndBrokerDetailVerifiedOrderByLastModifiedDateDesc(brokerDTO.getName(), brokerDTO.getIdNumber(),true).isPresent()) {
            newBroker = brokerRepository.findFirstByNameAndIdNumberAndBrokerDetailVerifiedOrderByLastModifiedDateDesc(brokerDTO.getName(), brokerDTO.getIdNumber(),true).get();
        } else {
            do newBroker = brokerService.createBroker(brokerDTO); while (!newBroker.getBrokerDetail().isVerified());
        }
        BrokerDTO newBrokerDTO = brokerMapper.brokerToBrokerDTO(newBroker);
        return ResponseEntity
                .created(new URI("/api/brokers/" + newBrokerDTO.getName()))
                .body(newBrokerDTO);
    }
}
