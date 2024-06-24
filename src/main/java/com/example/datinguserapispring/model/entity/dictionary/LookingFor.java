package com.example.datinguserapispring.model.entity.dictionary;


import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.User;
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
@Table(name = "looking_for_table", schema = "dating_user_api")
public class LookingFor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String lookingFor;
    private LocalDateTime createdAt;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_info_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "bot2_id")
    private Bot2 bot2;

    public LookingFor(String lookingFor, User user, LocalDateTime createdAt) {
        this.lookingFor = lookingFor;
        this.user = user;
        this.createdAt = createdAt;
    }

    public LookingFor(String lookingFor, Bot2 bot2) {
        this.lookingFor = lookingFor;
        this.bot2 = bot2;
    }

    @Override
    public String toString() {
        return "LookingFor{" +
                "id='" + id + '\'' +
                ", lookingFor='" + lookingFor + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", bot2=" + bot2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LookingFor that = (LookingFor) o;
        return Objects.equals(id, that.id) && Objects.equals(lookingFor, that.lookingFor) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lookingFor, user);
    }
}
