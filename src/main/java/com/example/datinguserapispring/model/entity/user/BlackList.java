package com.example.datinguserapispring.model.entity.user;


import com.example.datinguserapispring.model.entity.Admin;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "black_list", schema = "dating_user_api")
public class BlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    private Admin admin;
    @ManyToOne
    private User blockedUser;
    private String appleId;

    public BlackList(Admin admin, User blockedUser, String appleId) {
        this.admin = admin;
        this.blockedUser = blockedUser;
        this.appleId = appleId;
    }

    @Override
    public String toString() {
        return "BlackList{" +
                "id='" + id + '\'' +
                ", admin=" + admin +
                ", blockedUser=" + blockedUser +
                ", appleId='" + appleId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlackList blackList = (BlackList) o;
        return Objects.equals(id, blackList.id) &&
                Objects.equals(admin, blackList.admin) &&
                Objects.equals(blockedUser, blackList.blockedUser) &&
                Objects.equals(appleId, blackList.appleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, admin, blockedUser, appleId);
    }
}
