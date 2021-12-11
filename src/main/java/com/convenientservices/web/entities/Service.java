package com.convenientservices.web.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service")
public class Service {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private Long duration;

    @ManyToOne
    @JoinColumn(name = "service_category_id")
    private ServiceCategory category;

    @ManyToMany(mappedBy = "services")
    List<Booking> bookings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Service service = (Service) o;

        if (id != service.id) return false;
        if (name != null ? !name.equals(service.name) : service.name != null) return false;
        if (duration != null ? !duration.equals(service.duration) : service.duration != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", category=" + category +
                '}';
    }
}
