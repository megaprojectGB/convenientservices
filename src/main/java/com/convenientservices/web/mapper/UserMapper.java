package com.convenientservices.web.mapper;

import com.convenientservices.web.dto.UserDTO;
import com.convenientservices.web.entities.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = BooleanYNMapper.class)
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "archived", target = "status")
    @Mapping(source = "roles", target = "role")
    @Mapping(source = "activated", target = "activated")
    UserDTO fromUser(User user);

    @InheritInverseConfiguration
    User toUser(UserDTO userDTO);

    List<User> toUserList(List<UserDTO> userDTOS);

    List<UserDTO> fromUserList(List<User> users);
}







