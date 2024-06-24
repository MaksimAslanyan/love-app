package com.example.datinguserapispring.model.entity.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "session", schema = "dating_user_api")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @OneToOne
    private User user;
    private UUID deviceId;
    private String ip;
    private String refreshToken;
    private String accessToken;
    private LocalDateTime createdAt;
    private String platform;
}
