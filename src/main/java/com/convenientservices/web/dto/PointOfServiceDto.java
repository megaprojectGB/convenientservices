package com.convenientservices.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointOfServiceDto {
    private Long id;
    private String name;
    private String state;
    private String city;
    private String address;
    private String category;
    private String zip;
    private Long selector;
}
