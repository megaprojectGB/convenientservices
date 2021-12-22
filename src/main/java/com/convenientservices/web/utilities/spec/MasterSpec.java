package com.convenientservices.web.utilities.spec;

import com.convenientservices.web.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MasterSpec {

    public static Specification<User> nameLike(String firstName) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.
                or(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase(Locale.ROOT) + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + firstName.toLowerCase(Locale.ROOT) + "%"));
    }

    public static Specification<User> serviceLike(String service) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.join("masterServices").get("name")), "%" + service.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<User> roleEqual(String role) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.join("roles").get("name"), "%" + role + "%");
    }

}
