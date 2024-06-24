package com.example.datinguserapispring.model.entity.bot;

import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.model.enums.ParentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties("bots2")
@Table(name = "bot1", schema = "dating_user_api")
public class Bot1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @OneToMany(mappedBy = "bot1")
    @Transient
    private List<Bot2> bots2;

    @ManyToOne
    @JoinColumn(name = "admin_db_id")
    private Admin admin;
    private String race;
    @Enumerated(value = EnumType.STRING)
    private ParentType parentType;
    private String identity;
    private byte minAge;
    private byte maxAge;

    public Bot1(Gender gender, String race, byte minAge, byte maxAge) {
        this.gender = gender;
        this.race = race;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public Bot1(Admin admin) {
        this.admin = admin;
    }


    @Override
    public String toString() {
        return "Bot1{" +
                "id='" + id + '\'' +
                ", gender=" + gender +
                ", race='" + race + '\'' +
                ", parentType=" + parentType +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bot1 bot1 = (Bot1) o;
        return minAge == bot1.minAge && maxAge == bot1.maxAge &&
                Objects.equals(id, bot1.id) && gender == bot1.gender &&
                Objects.equals(race, bot1.race);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gender, race, minAge, maxAge);
    }
}
