package com.convenientservices.web.utilities.spec;


import com.convenientservices.web.entities.PointOfServices;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PosSpec {

    public static Specification<PointOfServices> cityEq(String city) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("address").get("city").get("name"), "%" + city + "%");
    }

    public static Specification<PointOfServices> categoryEq(String category) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("name"), category);
    }

}
