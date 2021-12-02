package com.convenientservices.web.mapper;

import com.convenientservices.web.dto.ServiceDto;
import com.convenientservices.web.entities.Service;
import com.convenientservices.web.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class TimeToStringMapper {
    public String timeToString(Long seconds) {
        return Utils.getStringTime(seconds);
    }

    public List<ServiceDto> getListDto(List<Service> list) {
        List<ServiceDto> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ServiceDto dto = new ServiceDto();
            dto.setId(list.get(i).getId());
            dto.setName(list.get(i).getName());
            dto.setTime(Utils.getStringTime(list.get(i).getDuration()));
            dto.setCategory(list.get(i).getCategory());
            result.add(dto);
        }
        return result;
    }
}
