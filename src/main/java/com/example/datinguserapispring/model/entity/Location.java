package com.example.datinguserapispring.model.entity;

import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.User;
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
@Table(name = "location", schema = "dating_user_api")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    @JoinColumn(name = "bot2_id")
    private Bot2 bot;
    @ManyToOne
    @JoinColumn(name = "user_info_id")
    private User user;
    private LocalDateTime createdAt;
    private double lat;
    private double lon;
    private String timeZone;

    public Location(Bot2 bot, double lat, double lon, LocalDateTime createdAt) {
        this.createdAt = createdAt;
        this.bot = bot;
        this.lat = lat;
        this.lon = lon;
    }

    public Location(User user, double lat, double lon, LocalDateTime createdAt, String timeZone) {
        this.createdAt = createdAt;
        this.user = user;
        this.lat = lat;
        this.lon = lon;
        this.timeZone = timeZone;
    }


    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", bot=" + bot +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", lat=" + lat +
                ", lon=" + lon +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.lat, lat) == 0 &&
                Double.compare(location.lon, lon) == 0 &&
                Objects.equals(id, location.id) &&
                Objects.equals(bot, location.bot) &&
                Objects.equals(user, location.user) &&
                Objects.equals(createdAt, location.createdAt) &&
                Objects.equals(timeZone, location.timeZone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bot, user, createdAt, lat, lon, timeZone);
    }
}
