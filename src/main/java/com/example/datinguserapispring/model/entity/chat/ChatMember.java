package com.example.datinguserapispring.model.entity.chat;

import com.example.datinguserapispring.model.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_member", schema = "dating_user_api")
public class ChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @OneToOne
    private User user;

    public ChatMember(String id) {
        this.id = id;
    }

    public ChatMember(Chat chat, User user) {
        this.chat = chat;
        this.user = user;
    }

    @Override
    public String toString() {
        return "ChatMember{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMember that = (ChatMember) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
