package com.example.datinguserapispring.model.entity.user;


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
@Table(name = "like_record", schema = "dating_user_api")
public class LikeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_info_id")
    private User user;
    private LocalDateTime likedAt;


    @Override
    public String toString() {
        return "LikeRecord{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", likedAt=" + likedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeRecord that = (LikeRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(likedAt, that.likedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, likedAt);
    }
}
