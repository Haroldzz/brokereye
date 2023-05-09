package life.freebao.devops.brokereye.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import life.freebao.devops.brokereye.domain.Broker;
import life.freebao.devops.brokereye.domain.BrokerDetail;
import life.freebao.devops.brokereye.domain.Captcha;
import life.freebao.devops.brokereye.page.circ.BrokerQueryPage;
import life.freebao.devops.brokereye.repository.BrokerRepository;
import life.freebao.devops.brokereye.repository.CaptchaRepository;
import life.freebao.devops.brokereye.service.dto.BrokerDTO;
import life.freebao.devops.brokereye.service.enumeration.QueryResponseType;
import life.freebao.devops.brokereye.service.mapper.BrokerMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class BrokerService {
    private final Logger log = LoggerFactory.getLogger(BrokerService.class);
    private final BrokerRepository brokerRepository;
    private final CaptchaRepository captchaRepository;
    private final BrokerQueryPage brokerQueryPage;
    private final BrokerMapper brokerMapper;
    @Value("${application.webdriver.circ.query.days:180}")
    private String queryDays;

    public BrokerService(BrokerRepository brokerRepository, CaptchaRepository captchaRepository, BrokerQueryPage brokerQueryPage, BrokerMapper brokerMapper) {
        this.brokerRepository = brokerRepository;
        this.captchaRepository = captchaRepository;
        this.brokerQueryPage = brokerQueryPage;
        this.brokerMapper = brokerMapper;
    }

    public Broker createBroker(BrokerDTO brokerDTO) throws IOException, InterruptedException {
        // new broker and detail
        Broker newBroker = brokerMapper.brokerDTOToBroker(brokerDTO);
        BrokerDetail newBrokerDetail = new BrokerDetail();
        String name = brokerDTO.getName();
        String idNumber = brokerDTO.getIdNumber();
        newBroker.setName(name);
        newBroker.setIdNumber(idNumber);
        // submit request and wait 1 second to get all responses. The wait is important.
        HashMap<String, String> brokerQueryResponseMap = brokerQueryPage.getBrokerQueryPage(name, idNumber);
        TimeUnit.MILLISECONDS.sleep(1000L);
        // get and save captcha
        String captchaImageDigest = brokerQueryResponseMap.get("captchaImageDigest");
        String captchaImageOcr = brokerQueryResponseMap.get("captchaImageOcr");
        Captcha newCaptcha = new Captcha(null, captchaImageDigest, captchaImageOcr);
        log.info("newCaptcha = {}", newCaptcha);
        Captcha captcha = captchaRepository.save(newCaptcha);
        log.info("captcha = {}", captcha);
        // get response with constants in QueryResponseType
        Map<String, String> subMap = new HashMap<>();
        for (QueryResponseType type : QueryResponseType.values()) {
            if (getSubMap(brokerQueryResponseMap, type.getResponse()) != null)
                subMap = getSubMap(brokerQueryResponseMap, type.getResponse());
        }
        log.info("subMap = {}", subMap);
        // QueryResponseType.SUCCESS.getResponse()
        if (checkKey(subMap, QueryResponseType.SUCCESS.getResponse())) {
            Map<String, String> brokerInfoMap = new HashMap<>();
            for (Map.Entry<String, String> entry : subMap.entrySet()) {
                String value = StringUtils.substringBetween(entry.getValue(), "[", "]");
                brokerInfoMap = new ObjectMapper().readValue(value, new TypeReference<Map<String, String>>(){});
                log.info("brokerInfoMap = {}", brokerInfoMap);
            }
            // set broker detail
            newBrokerDetail.setActivated("0".equals(brokerInfoMap.get("is_status")));
            newBrokerDetail.setApplyDate(Instant.parse(brokerInfoMap.get("applytime") + "T00:00:00.00Z"));
            newBrokerDetail.setBeginDate("".equals(brokerInfoMap.get("BEGIN_DATE")) ? null : Instant.parse(brokerInfoMap.get("BEGIN_DATE") + "T00:00:00.00Z"));
            newBrokerDetail.setBusinessScope(brokerInfoMap.get("businesscope").trim());
            newBrokerDetail.setEndDate("".equals(brokerInfoMap.get("end_date")) ? null : Instant.parse(brokerInfoMap.get("end_date") + "T00:00:00.00Z"));
            newBrokerDetail.setGender("男".equals(brokerInfoMap.get("gender")) ? 1 : 2);
            newBrokerDetail.setInstitutionType(Integer.valueOf(brokerInfoMap.get("ORG_CATE")));
            newBrokerDetail.setOnlineSales("是".equals(brokerInfoMap.get("internetbusiness")));
            newBrokerDetail.setPersonId(brokerInfoMap.get("personid").trim());
            newBrokerDetail.setVerified(true);
            log.info("newBrokerDetail = {}", newBrokerDetail);
            // set broker
            newBroker.setBrokerDetail(newBrokerDetail);
            newBroker.setPracticeNumber(brokerInfoMap.get("practicecode").trim());
            newBroker.setPracticeArea(brokerInfoMap.get("practicearea").trim());
            newBroker.setInsuranceInstitute(brokerInfoMap.get("insurnnce_code").trim());
        }
        // QueryResponseType.NONE.getResponse()
        if (checkKey(subMap, QueryResponseType.NONE.getResponse())) {
            Map<String, String> brokerInfoMap;
            for (Map.Entry<String, String> entry : subMap.entrySet()) {
                String value = StringUtils.substringBefore(entry.getValue(), ",");
                brokerInfoMap = new ObjectMapper().readValue(value, new TypeReference<Map<String, String>>(){});
                log.info("brokerInfoMap = {}", brokerInfoMap);
            }
            // set broker detail
            newBrokerDetail.setActivated(false);
            newBrokerDetail.setApplyDate(null);
            newBrokerDetail.setBeginDate(null);
            newBrokerDetail.setBusinessScope(null);
            newBrokerDetail.setEndDate(null);
            newBrokerDetail.setGender(null);
            newBrokerDetail.setInstitutionType(null);
            newBrokerDetail.setOnlineSales(false);
            newBrokerDetail.setPersonId(null);
            newBrokerDetail.setVerified(true);
            log.info("newBrokerDetail = {}", newBrokerDetail);
            // set broker
            newBroker.setBrokerDetail(newBrokerDetail);
            newBroker.setPracticeNumber(null);
            newBroker.setPracticeArea(null);
            newBroker.setInsuranceInstitute(null);
        }
        // QueryResponseType.CAPTCHA.getResponse()
        if (checkKey(subMap, QueryResponseType.CAPTCHA.getResponse())) {
            Map<String, String> brokerInfoMap;
            for (Map.Entry<String, String> entry : subMap.entrySet()) {
                String value = StringUtils.substringBeforeLast(entry.getValue(), "}") + '}';
                brokerInfoMap = new ObjectMapper().readValue(value, new TypeReference<Map<String, String>>(){});
                log.info("brokerInfoMap = {}", brokerInfoMap);
            }
            // set broker detail
            newBrokerDetail.setActivated(false);
            newBrokerDetail.setApplyDate(null);
            newBrokerDetail.setBeginDate(null);
            newBrokerDetail.setBusinessScope(null);
            newBrokerDetail.setEndDate(null);
            newBrokerDetail.setGender(null);
            newBrokerDetail.setInstitutionType(null);
            newBrokerDetail.setOnlineSales(false);
            newBrokerDetail.setPersonId(null);
            newBrokerDetail.setVerified(false);
            log.info("newBrokerDetail = {}", newBrokerDetail);
            // set broker
            newBroker.setBrokerDetail(newBrokerDetail);
            newBroker.setPracticeNumber(null);
            newBroker.setPracticeArea(null);
            newBroker.setInsuranceInstitute(null);
        }
        // save and return new broker
        log.info("Created broker: {}", newBroker);
        brokerRepository.save(newBroker);
        return newBroker;
    }
    private Map<String, String> getSubMap(Map<String, String> map, String subKey) {
        Map<String, String> subMap = null;
        for (String key : map.keySet()) {
            if (key.contains(subKey)) {
//                Map<String, String> brokerInfoMap = map.entrySet().stream().filter((entry) ->entry.getKey().contains("insurance.do?query")).collect(Collectors.toMap((entry) -> entry.getKey(), (entry) -> entry.getValue()));
                subMap = map
                        .entrySet()
                        .stream()
                        .filter((entry) -> entry.getKey().contains(subKey))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                log.info("Filter response map: {}", subMap);
            }
        }
        return subMap;
    }
    private boolean checkKey(Map<String, String> map, String subKey){
        return map.keySet().stream().anyMatch(key -> key.contains(subKey));
    }
    // schedule to update brokers
    @Scheduled(cron = "${application.webdriver.circ.query.cron:0 0 * * * ?}")
    public void updateAllByGroupByNameAndIdNumberHavingLastModifiedDateLessThan() throws IOException, InterruptedException {
        List<Broker> brokers = brokerRepository
                .findAllByGroupByNameAndIdNumberHavingLastModifiedDateLessThan(Instant.now().minus(Long.parseLong(queryDays), ChronoUnit.DAYS));
        if (brokers != null && !CollectionUtils.isEmpty(brokers)) {
            for (Broker broker : brokers) {
                BrokerDTO brokerDTO = brokerMapper.brokerToBrokerDTO(broker);
                brokerDTO.setId(null);
                Broker newBroker = createBroker(brokerDTO);
                log.info("newBroker = {}", newBroker);
                TimeUnit.MILLISECONDS.sleep(Double.valueOf(Math.random()*3000 + 1000.0).longValue());
            }
        }
    }

}
