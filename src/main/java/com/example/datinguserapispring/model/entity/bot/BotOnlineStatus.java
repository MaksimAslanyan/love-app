package com.example.datinguserapispring.model.entity.bot;


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
@Table(name = "bot_online_status", schema = "dating_user_api")
public class BotOnlineStatus {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private String id;
   @OneToOne
   private Bot2 bot2;
   private boolean isOnline;
   private LocalDateTime lastOnlineTime;

   public BotOnlineStatus(Bot2 bot2, boolean isOnline, LocalDateTime lastOnlineTime) {
      this.bot2 = bot2;
      this.isOnline = isOnline;
      this.lastOnlineTime = lastOnlineTime;
   }

   @Override
   public String toString() {
      return "BotOnlineStatus{" +
              "id='" + id + '\'' +
              ", bot2=" + bot2 +
              ", isOnline=" + isOnline +
              ", lastOnlineTime=" + lastOnlineTime +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      BotOnlineStatus that = (BotOnlineStatus) o;
      return isOnline == that.isOnline && Objects.equals(id, that.id) &&
              Objects.equals(bot2, that.bot2) &&
              Objects.equals(lastOnlineTime, that.lastOnlineTime);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, bot2, isOnline, lastOnlineTime);
   }
}
