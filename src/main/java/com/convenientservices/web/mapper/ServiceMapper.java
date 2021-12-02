package com.convenientservices.web.mapper;

import com.convenientservices.web.dto.ServiceDto;
import com.convenientservices.web.entities.Service;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = TimeToStringMapper.class)
public interface ServiceMapper {
    ServiceMapper MAPPER = Mappers.getMapper(ServiceMapper.class);

    @Mapping(source = "duration", target = "time")
    ServiceDto fromService(Service service);

    @InheritInverseConfiguration
    Service toService(ServiceDto userDTO);

    List<Service> toServiceList(List<ServiceDto> serviceDTOS);

    List<ServiceDto> fromServiceList(List<Service> services);
}
