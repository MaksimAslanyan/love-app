package com.example.datinguserapispring.model.entity;


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
@Table(name = "notification", schema = "dating_user_api")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    @JoinColumn(name = "user_info_id")
    private User user;
    private LocalDateTime sendTime;
    private String opponentId;
    private String userType;
    private boolean isSend;


    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", sendTime=" + sendTime +
                ", opponentId='" + opponentId + '\'' +
                ", userType='" + userType + '\'' +
                ", isSend=" + isSend +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) &&
                Objects.equals(sendTime, that.sendTime) &&
                Objects.equals(opponentId, that.opponentId) &&
                Objects.equals(userType, that.userType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, sendTime, opponentId, userType);
    }
}
