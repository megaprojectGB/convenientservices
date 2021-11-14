package com.convenientservices.web.mapper;

import com.convenientservices.web.entities.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class BooleanYNMapper {
    public String boolAssString(Boolean bool) {
        return null == bool ?
                null : (bool ?
                "Archived" : "Active"
        );
    }

    public Boolean asBoolean(String bool) {
        return null == bool ?
                null : (bool.trim().toLowerCase().startsWith("y") ?
                Boolean.TRUE : Boolean.FALSE
        );
    }

    public String collectionAsString(Collection<Role> list) {
        return null == list ?
                null : (list.stream().findFirst().get().getName()
        );
    }

    public Collection<Role> stringAsCollection(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return null == roleName ?
                null : (new ArrayList<>(Arrays.asList(role)));
    }
}
