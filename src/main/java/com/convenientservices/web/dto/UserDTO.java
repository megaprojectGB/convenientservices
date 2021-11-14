package com.convenientservices.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}

