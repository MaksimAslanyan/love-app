package com.example.datinguserapispring.model.entity.user;

import com.example.datinguserapispring.model.entity.Location;
import com.example.datinguserapispring.model.entity.Notification;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.dictionary.LookingFor;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.serialzier.UserSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonSerialize(using = UserSerializer.class)
@Table(name = "user_info", schema = "dating_user_api")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private LocalDateTime createdAt;
    private String name;
    private String appleId;
    private LocalDateTime lastActivity;
    private int age;
    private boolean isBlackList;
    private boolean isActive;
    private String roles;
    private boolean isPremium;
    private String token;
    private long likeCount;
    private String language;
    private String targetGender;
    private LocalDateTime likeDayLimit;
    private String password;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "user_name")
    private String userName;

    @JsonFormat(pattern = "dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dob;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private OnlineStatus onlineStatus;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    @Transient
    private List<PremiumPeriod> premiumPeriods;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Transient
    private Set<UserLike> userLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Location> locations;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<LookingFor> lookingFor;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Transient
    private List<Notification> notifications;

    @OneToMany(mappedBy = "blockingUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Block> blocks = new ArrayList<>();

    @OneToMany(mappedBy = "blockingUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Transient
    private Set<Block> blockedBots = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Transient
    private List<Bot2> ownBots;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Transient
    private List<LikeRecord> likeRecords;


    public User(boolean isBlackList) {
        this.isBlackList = isBlackList;
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", likeDayLimit=" + likeDayLimit +
                ", appleId='" + appleId + '\'' +
                ", lastActivity=" + lastActivity +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", dob=" + dob +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", targetGender='" + targetGender + '\'' +
                ", age=" + age +
                ", isBlackList=" + isBlackList +
                ", isActive=" + isActive +
                ", roles='" + roles + '\'' +
                ", isPremium=" + isPremium +
                ", token='" + token + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age &&
                isBlackList == user.isBlackList &&
                isActive == user.isActive &&
                isPremium == user.isPremium &&
                Objects.equals(id, user.id) &&
                Objects.equals(createdAt, user.createdAt) &&
                Objects.equals(likeDayLimit, user.likeDayLimit) &&
                Objects.equals(appleId, user.appleId) &&
                Objects.equals(lastActivity, user.lastActivity) &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(password, user.password) &&
                gender == user.gender &&
                Objects.equals(dob, user.dob) &&
                Objects.equals(name, user.name) &&
                Objects.equals(language, user.language) &&
                Objects.equals(targetGender, user.targetGender) &&
                Objects.equals(roles, user.roles) &&
                Objects.equals(token, user.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, createdAt, appleId,
                lastActivity, userName, password,
                gender, dob, name,
                language, targetGender, age,
                isBlackList, isActive, roles,
                isPremium, token, likeDayLimit);
    }
}
