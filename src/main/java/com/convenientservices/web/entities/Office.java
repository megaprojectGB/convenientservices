package com.convenientservices.web.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "office")
public class Office {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Address address;

    @ManyToMany
    @JoinTable(name = "categoryDetails",
            joinColumns = @JoinColumn(name = "officeId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId"))
    private Collection<Category> categories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Office office = (Office) o;

        if (id != office.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
