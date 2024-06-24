package com.example.datinguserapispring.model.entity.user;

import com.example.datinguserapispring.model.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "online_status", schema = "dating_user_api")
public class OnlineStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    private Admin admin;
    private boolean isOnline;
    private LocalDateTime lastOnlineTime;


    public OnlineStatus(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public String toString() {
        return "OnlineStatus{" +
                "id='" + id + '\'' +
                ", isOnline=" + isOnline +
                ", lastOnlineTime=" + lastOnlineTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlineStatus that = (OnlineStatus) o;
        return isOnline == that.isOnline &&
                Objects.equals(id, that.id) &&
                Objects.equals(lastOnlineTime, that.lastOnlineTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isOnline, lastOnlineTime);
    }
}
