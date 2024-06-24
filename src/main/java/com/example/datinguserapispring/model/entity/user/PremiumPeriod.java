package com.example.datinguserapispring.model.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "premium_period", schema = "dating_user_api")
public class PremiumPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_info_id")
    private User user;
    private LocalDateTime fromDate;
    private LocalDateTime untilDate;
    private boolean active;

    public PremiumPeriod(User user, LocalDateTime fromDate, LocalDateTime untilDate) {
        this.user = user;
        this.fromDate = fromDate;
        this.untilDate = untilDate;
    }

    @Override
    public String toString() {
        return "PremiumPeriod{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", fromDate=" + fromDate +
                ", untilDate=" + untilDate +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PremiumPeriod that = (PremiumPeriod) o;
        return active == that.active &&
                Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(fromDate, that.fromDate) &&
                Objects.equals(untilDate, that.untilDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, fromDate, untilDate, active);
    }
}
