package com.example.datinguserapispring.model.entity.chat;

import com.example.datinguserapispring.model.entity.bot.Bot2;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_member_admin", schema = "dating_user_api")
public class ChatMemberAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @OneToOne
    private Bot2 bot2;

    public ChatMemberAdmin(String id) {
        this.id = id;
    }

    public ChatMemberAdmin(Chat chat, Bot2 bot2) {
        this.chat = chat;
        this.bot2 = bot2;
    }

    @Override
    public String toString() {
        return "ChatMemberAdmin{" +
                "id='" + id + '\'' +
                ", chat=" + chat +
                ", bot2=" + bot2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMemberAdmin that = (ChatMemberAdmin) o;
        return Objects.equals(id, that.id) && Objects.equals(chat, that.chat) && Objects.equals(bot2, that.bot2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chat, bot2);
    }
}
