package com.example.datinguserapispring.model.entity.chat;

import com.example.datinguserapispring.model.enums.ChatType;
import com.example.datinguserapispring.projection.ChatProjection;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

@SqlResultSetMapping(
        name = "ChatDtoMapping",
        classes = @ConstructorResult(
                targetClass = ChatProjection.class,
                columns = {
                        @ColumnResult(name = "chatId", type = String.class),
                        @ColumnResult(name = "isMatch", type = Boolean.class),
                        @ColumnResult(name = "bot2Id", type = String.class),
                        @ColumnResult(name = "age", type = Byte.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "distance", type = Double.class),
                        @ColumnResult(name = "isOnline", type = Boolean.class),
                        @ColumnResult(name = "chatMessageId", type = String.class),
                        @ColumnResult(name = "lastMessageDate", type = LocalDateTime.class),
                        @ColumnResult(name = "lastMessage", type = String.class),
                        @ColumnResult(name = "isRead", type = Boolean.class),
                        @ColumnResult(name = "chatMemberId", type = String.class),
                        @ColumnResult(name = "chatMemberAdminId", type = String.class),
                        @ColumnResult(name = "photoId", type = String.class)
                }
        )
)
@Table(name = "chat", schema = "dating_user_api")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private LocalDateTime createdDate;
    private LocalDateTime lastActivity;
    @Enumerated(EnumType.STRING)
    private ChatType chatType;
    private boolean isMatch;

    @OneToMany(mappedBy = "chat", cascade = {CascadeType.ALL, CascadeType.DETACH}, orphanRemoval = true)
    private List<ChatMember> chatMembers;

    @OneToMany(mappedBy = "chat", cascade = {CascadeType.ALL, CascadeType.DETACH}, orphanRemoval = true)
    private List<ChatMemberAdmin> chatMemberAdmins;

    @OneToMany(mappedBy = "chat", cascade = {CascadeType.ALL, CascadeType.DETACH}, orphanRemoval = true)
    private List<ChatMessage> chatMessages;


    public Chat(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Chat(LocalDateTime createdDate,
                ChatType chatType,
                LocalDateTime lastActivity) {
        this.createdDate = createdDate;
        this.chatType = chatType;
        this.lastActivity = lastActivity;
    }

    public Chat(LocalDateTime createdDate,
                ChatType chatType,
                LocalDateTime lastActivity,
                boolean isMatch) {
        this.createdDate = createdDate;
        this.chatType = chatType;
        this.lastActivity = lastActivity;
        this.isMatch = isMatch;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", createdDate=" + createdDate +
                ", lastActivity=" + lastActivity +
                ", chatType=" + chatType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) && Objects.equals(createdDate, chat.createdDate) &&
                Objects.equals(lastActivity, chat.lastActivity) && chatType == chat.chatType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, lastActivity, chatType);
    }
}
