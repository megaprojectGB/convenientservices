package com.convenientservices.web.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dt")
    private Timestamp dt;

    @ManyToOne
    private User user;

    @ManyToOne
    private User master;

    @ManyToOne
    private PointOfServices pointOfServices;

    @ManyToMany
    @JoinTable(name = "service_properties",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    Collection<Service> services;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (id != booking.id) return false;
        if (dt != null ? !dt.equals(booking.dt) : booking.dt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (dt != null ? dt.hashCode() : 0);
        return result;
    }
}
