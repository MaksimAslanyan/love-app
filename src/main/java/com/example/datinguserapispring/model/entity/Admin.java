package com.example.datinguserapispring.model.entity;


import com.example.datinguserapispring.model.entity.bot.Bot1;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.AdminAction;
import com.example.datinguserapispring.model.entity.user.BlackList;
import com.example.datinguserapispring.model.entity.user.OnlineStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_db", schema = "dating_user_api")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String nickName;
    private String login;
    private String password;
    private String name;
    private int role;
    private int age;
    private boolean isDeleted;

    @OneToMany(mappedBy = "id")
    @Transient
    private List<Bot1> bot1s;
    @OneToMany
    @JoinColumn(name = "id")
    private List<Photo> photo;
    @OneToMany(mappedBy = "id")
    @Transient
    private List<BlackList> blackList;
    @OneToMany(mappedBy = "id")
    private List<AdminAction> adminActions;
    @OneToMany(mappedBy = "id")
    @Transient
    private List<Bot2> bot2s;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "admin")
    private OnlineStatus onlineStatus;



    public Admin(String id, String nickName, String name, int role) {
        this.id = id;
        this.nickName = nickName;
        this.name = name;
        this.role = role;
    }

    public Admin(String nickName, String login, String password, String name, int role, int age) {
        this.nickName = nickName;
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", onlineStatus=" + onlineStatus +
                ", role=" + role +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return role == admin.role &&
                age == admin.age &&
                Objects.equals(id, admin.id) &&
                Objects.equals(nickName, admin.nickName) &&
                Objects.equals(login, admin.login) &&
                Objects.equals(password, admin.password) &&
                Objects.equals(name, admin.name) &&
                Objects.equals(photo, admin.photo) &&
                Objects.equals(blackList, admin.blackList) &&
                Objects.equals(adminActions, admin.adminActions) &&
                Objects.equals(bot2s, admin.bot2s) &&
                Objects.equals(onlineStatus, admin.onlineStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, nickName, login,
                password, name, photo,
                blackList, adminActions, bot2s,
                onlineStatus, role, age);
    }
}
