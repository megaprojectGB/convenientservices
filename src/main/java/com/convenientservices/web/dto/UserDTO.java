package com.convenientservices.web.dto;

import com.convenientservices.web.entities.PointOfServices;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String status;
    private boolean activated;
    private String phone;
    private String email;
    private String role;
    private List<PointOfServices> favoriteCompanies;
    private List<ServiceDto> masterServices;
    private List<PointOfServices> masterPos;
}

