package com.example.datinguserapispring.model.entity;


import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.PhotoState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "photo", schema = "dating_user_api")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String parentType;
    private byte isAvatar;
    @Column(name = "is_approve")
    private boolean approve;
    private String url;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_info_id")
    @JsonBackReference
    private User user;
    @ManyToOne
    @JoinColumn(name = "bot2_id")
    private Bot2 bot2;
    @ManyToOne
    @JoinColumn(name = "admin_db_id")
    private Admin admin;
    @Enumerated(EnumType.STRING)
    private PhotoState photoState;


    public Photo(boolean isApprove) {
        this.approve = isApprove;
    }

    public Photo(String parentType, byte isAvatar,
                 String url, LocalDateTime createdAt,
                 User user, Bot2 bot2) {
        this.parentType = parentType;
        this.isAvatar = isAvatar;
        this.url = url;
        this.createdAt = createdAt;
        this.user = user;
        this.bot2 = bot2;
    }

    public Photo(String id,
                 String parentType,
                 byte isAvatar,
                 boolean approve,
                 String url,
                 LocalDateTime createdAt,
                 User user,
                 Bot2 bot2,
                 Admin admin) {
        this.id = id;
        this.parentType = parentType;
        this.isAvatar = isAvatar;
        this.approve = approve;
        this.url = url;
        this.createdAt = createdAt;
        this.user = user;
        this.bot2 = bot2;
        this.admin = admin;
    }

    public Photo(String parentType, byte isAvatar, String url, LocalDateTime createdAt, Bot2 bot2) {
        this.parentType = parentType;
        this.isAvatar = isAvatar;
        this.url = url;
        this.createdAt = createdAt;
        this.photoState = PhotoState.APPROVE;
        this.approve = true;
        this.bot2 = bot2;
    }

    public Photo(String parentType, String url, LocalDateTime createdAt) {
        this.parentType = parentType;
        this.url = url;
        this.createdAt = createdAt;
    }

    public Photo(String parentType, byte isAvatar, String url, LocalDateTime createdAt, Admin admin) {
        this.parentType = parentType;
        this.isAvatar = isAvatar;
        this.url = url;
        this.createdAt = createdAt;
        this.photoState = PhotoState.APPROVE;
        this.approve = true;
        this.admin = admin;
    }

    public Photo(String parentType, byte isAvatar,
                 String url, LocalDateTime createdAt,
                 User user, boolean isApprove) {
        this.parentType = parentType;
        this.isAvatar = isAvatar;
        this.url = url;
        this.createdAt = createdAt;
        this.approve = isApprove;
        this.user = user;
        this.photoState = PhotoState.WAITING_TO_APPROVE;
    }




    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", parentType='" + parentType + '\'' +
                ", isAvatar=" + isAvatar +
                ", isApprove=" + approve +
                ", url='" + url + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return isAvatar == photo.isAvatar &&
                approve == photo.approve &&
                Objects.equals(id, photo.id) &&
                Objects.equals(parentType, photo.parentType) &&
                Objects.equals(url, photo.url) &&
                Objects.equals(createdAt, photo.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentType, isAvatar, approve, url, createdAt);
    }


    public void setApprove(boolean approve) {
        this.approve = approve;
    }
}
