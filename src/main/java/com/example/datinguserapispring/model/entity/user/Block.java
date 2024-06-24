package com.example.datinguserapispring.model.entity.user;


import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.enums.UserType;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "block", schema = "dating_user_api")
public class Block  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    @JoinColumn(name = "blocking_user_id")
    private User blockingUser;
    @ManyToOne
    @JoinColumn(name = "blocked_user_id")
    private User blockedUser;
    @ManyToOne
    @JoinColumn(name = "blocked_bot_id")
    private Bot2 blockedBot;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private boolean isBlocked;

    @Override
    public String toString() {
        return "Block{" +
                "id='" + id + '\'' +
                ", blockingUser=" + blockingUser +
                ", blockedUser=" + blockedUser +
                ", blockedBot=" + blockedBot +
                ", userType=" + userType +
                ", isBlocked=" + isBlocked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return isBlocked == block.isBlocked && Objects.equals(id, block.id) &&
                Objects.equals(blockingUser, block.blockingUser) &&
                Objects.equals(blockedUser, block.blockedUser) &&
                Objects.equals(blockedBot, block.blockedBot) && userType == block.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, blockingUser, blockedUser, blockedBot, userType, isBlocked);
    }
}
