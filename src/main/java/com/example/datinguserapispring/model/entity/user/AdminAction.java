package com.example.datinguserapispring.model.entity.user;

import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.enums.ActionType;
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
@Table(name = "admin_action", schema = "dating_user_api")
public class AdminAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    @JoinColumn(name = "user_info_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "admin_db_id")
    private Admin admin;
    private String imageId;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    private long actionCount;


    @Override
    public String toString() {
        return "AdminAction{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", admin=" + admin +
                ", imageId='" + imageId + '\'' +
                ", createdAt=" + createdAt +
                ", actionType=" + actionType +
                ", actionCount=" + actionCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminAction that = (AdminAction) o;
        return actionCount == that.actionCount && Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) && Objects.equals(admin, that.admin) &&
                Objects.equals(imageId, that.imageId) && Objects.equals(createdAt, that.createdAt)
                && actionType == that.actionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, admin, imageId, createdAt, actionType, actionCount);
    }
}
