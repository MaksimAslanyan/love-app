package com.example.datinguserapispring.api.controller;


import com.example.datinguserapispring.constants.Races;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RaceController {

    @GetMapping("/dictionary/races")
    @PostAuthorize("hasAuthority('OWNER') OR hasAuthority('ADMIN') OR hasAuthority('MODERATOR')")
    public List<String> getRaces() {
        return Races.RACES;
    }
}
