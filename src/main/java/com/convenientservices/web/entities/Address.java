package com.convenientservices.web.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (id != address.id) return false;
        if (zipcode != null ? !zipcode.equals(address.zipcode) : address.zipcode != null) return false;
        if (address1 != null ? !address1.equals(address.address1) : address.address1 != null) return false;
        if (address2 != null ? !address2.equals(address.address2) : address.address2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (zipcode != null ? zipcode.hashCode() : 0);
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        return result;
    }
}
