package com.example.datinguserapispring.model.entity.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "free_gift", schema = "dating_user_api")
public class FreeGift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    private User user;
    private long boosts;
    private long superLike;
    private LocalDateTime updatedAt;
}
