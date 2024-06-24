package com.example.datinguserapispring.model.entity.user;

import com.example.datinguserapispring.model.entity.bot.Bot2;
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
@Table(name = "user_like", schema = "dating_user_api")
public class UserLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    @JoinColumn(name = "user_info_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "bot2_id")
    private Bot2 bot2;
    private String opponentId;
    private String userAuthorType;
    private boolean isBotLiked;
    private boolean isLikedYou;
    private boolean isHasMatchChat;
    private LocalDateTime createdAt;


    @Override
    public String toString() {
        return "UserLike{" +
                "id='" + id + '\'' +
                ", opponentId='" + opponentId + '\'' +
                ", userAuthorType='" + userAuthorType + '\'' +
                ", isBotLiked=" + isBotLiked +
                ", isLikedYou=" + isLikedYou +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLike userLike = (UserLike) o;
        return isBotLiked == userLike.isBotLiked &&
                isLikedYou == userLike.isLikedYou &&
                Objects.equals(id, userLike.id) &&
                Objects.equals(opponentId, userLike.opponentId) &&
                Objects.equals(userAuthorType, userLike.userAuthorType) &&
                Objects.equals(createdAt, userLike.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, bot2, opponentId, userAuthorType, isBotLiked, isLikedYou, createdAt);
    }
}
