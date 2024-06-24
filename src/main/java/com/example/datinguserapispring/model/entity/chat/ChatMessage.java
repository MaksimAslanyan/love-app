package com.example.datinguserapispring.model.entity.chat;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_message", schema = "dating_user_api")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;
    @OneToOne
    private ChatMember chatMember;
    @OneToOne
    private ChatMemberAdmin chatMemberAdmin;

    private String photoId;
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String message;

    private boolean isRead;

    private boolean canSee;

    public ChatMessage(String chatMessageId, String lastMessage, LocalDateTime lastMessageDate, String photoId, Boolean isRead, String chatMemberId, String chatMemberAdminId) {
        this.id = chatMessageId;
        this.message = lastMessage;
        this.createdDate = lastMessageDate;
        this.photoId = photoId;
        this.isRead = isRead;
        this.chatMember = new ChatMember(chatMemberId);
        this.chatMemberAdmin = new ChatMemberAdmin(chatMemberAdminId);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", photoId='" + photoId + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                ", canSee=" + canSee +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return isRead == that.isRead && canSee == that.canSee &&
                Objects.equals(id, that.id) && Objects.equals(chat, that.chat) &&
                Objects.equals(photoId, that.photoId) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(updatedDate, that.updatedDate) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                photoId, createdDate,
                updatedDate, message, isRead, canSee);
    }
}
