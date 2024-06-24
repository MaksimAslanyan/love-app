package com.example.datinguserapispring.model.entity.bot;

import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.Location;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.dictionary.LookingFor;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.entity.user.UserLike;
import com.example.datinguserapispring.model.enums.ParentType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bot2", schema = "dating_user_api")
public class Bot2 implements Serializable {
    @Id
    private String id;
    private String nameBot;
    private byte age;
    private double distance;
    private String packagePath;
    @ManyToOne
    @JoinColumn(name = "admin_db_id")
    private Admin admin;
    @ManyToOne
    @JoinColumn(name = "bot1_id")
    private Bot1 bot1;

    @ManyToOne
    @JoinColumn(name = "user_info_id")
    private User user;

    @OneToMany(mappedBy = "bot2",
            cascade = CascadeType.ALL)
    private List<Photo> photo;
    @OneToMany(mappedBy = "bot",
            cascade = CascadeType.ALL)
    @Transient
    private List<Location> locations;

    @OneToMany(mappedBy = "bot2",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Transient
    private Set<UserLike> userLikes;

    @OneToMany(mappedBy = "bot2",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LookingFor> lookingFor;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "bot2")
    private BotOnlineStatus botOnlineStatus;

    @Enumerated(EnumType.STRING)
    private ParentType parentType;


    @Override
    public String toString() {
        return "Bot2{" +
                "id='" + id + '\'' +
                ", nameBot='" + nameBot + '\'' +
                ", age=" + age +
                ", distance=" + distance +
                ", parentType=" + parentType +
                ", packagePath='" + packagePath + '\'' +
                '}';
    }

    public Bot2(String nameBot,
                byte age,
                double distance,
                Admin admin,
                Bot1 bot1,
                User user) {
        this.id = UUID.randomUUID().toString();
        this.nameBot = nameBot;
        this.age = age;
        this.distance = distance;
        this.admin = admin;
        this.bot1 = bot1;
        this.user = user;
        this.parentType = ParentType.ADMIN;
    }

    public Bot2(String botId, String botName, Byte age, Double distance) {
        this.id = botId;
        this.nameBot = botName;
        this.age = age;
        this.distance = distance;
    }

    public Bot2(String id,
                Admin admin,
                String nameBot,
                byte age,
                double distance,
                Bot1 bot1,
                ParentType parentType,
                User user) {
        this.id = id;
        this.admin = admin;
        this.nameBot = nameBot;
        this.age = age;
        this.distance = distance;
        this.bot1 = bot1;
        this.parentType = parentType;
        this.user = user;
    }

    public Bot2(String id,
                Admin admin,
                String nameBot,
                Bot1 bot1,
                ParentType parentType,
                String packagePath) {
        this.id = id;
        this.admin = admin;
        this.nameBot = nameBot;
        this.bot1 = bot1;
        this.parentType = parentType;
        this.packagePath = packagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bot2 bot2 = (Bot2) o;
        return age == bot2.age && Double.compare(bot2.distance, distance) == 0 &&
                Objects.equals(id, bot2.id) && Objects.equals(admin, bot2.admin) &&
                Objects.equals(bot1, bot2.bot1)  && Objects.equals(nameBot, bot2.nameBot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, admin, bot1, nameBot, age, distance);
    }
}
