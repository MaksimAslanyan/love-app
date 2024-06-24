package com.example.datinguserapispring.model.entity.user;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ban_info", schema = "dating_user_api")
public class BanInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    private User user;
    private LocalDate dateUntil;
    private boolean isForever;
    private String reason;


}
