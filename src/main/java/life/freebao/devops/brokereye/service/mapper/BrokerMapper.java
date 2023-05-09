package life.freebao.devops.brokereye.service.mapper;

import life.freebao.devops.brokereye.domain.Broker;
import life.freebao.devops.brokereye.service.dto.BrokerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class BrokerMapper {
    public abstract BrokerDTO brokerToBrokerDTO(Broker broker);
    public abstract Broker brokerDTOToBroker(BrokerDTO brokerDTO);

}
