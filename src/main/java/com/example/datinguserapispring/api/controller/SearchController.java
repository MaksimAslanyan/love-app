package com.example.datinguserapispring.api.controller;


import com.example.datinguserapispring.dto.TestDTO;
import com.example.datinguserapispring.dto.search.request.SearchByCriteria;
import com.example.datinguserapispring.dto.search.response.ProfileSnapshotSearchResponseBot;
//import com.example.datinguserapispring.jobs.BotLikeScheduler;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.bot.GenerateBotService;
import com.example.datinguserapispring.service.bot.SearchBotService;
import com.example.datinguserapispring.service.search.SearchService;
import com.example.datinguserapispring.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final SearchBotService searchBotService;
    private final SecurityContextService securityContextService;
    private final RedisUtil<String> redisUtil;
//    private final BotLikeScheduler botLikeScheduler;


    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER')")
    public List<com.example.datinguserapispring.dto.user.response.ProfileSnapshotSearchResponse> search(@RequestBody SearchByCriteria criteria) {
        return searchService.search(criteria);
    }

    @PostMapping("/v1/search")
    @PreAuthorize("hasAuthority('USER')")
    public List<ProfileSnapshotSearchResponseBot> searchBots(@RequestBody SearchByCriteria criteria) {
        String userId = securityContextService.getUserDetails().getUsername();
        return searchBotService.searchBots(criteria, userId);
    }

    private static final List<String> VALID_COUNTRIES = List.of(
            "afghanistan", "albania", "andorra", "angola", "arabia", "argentina", "armenia",
            "australia", "austria", "azerbaijan", "bahamas", "bangladesh", "belgium", "belize",
            "bhutan", "bolivia", "bosnia and herzegovina", "brazil", "brunei", "bulgaria",
            "cambodia", "canada", "cape verde", "catalonia", "chile", "china", "colombia",
            "costa rica", "côte d'ivoire", "croatia", "cuba", "cyprus", "czech republic",
            "denmark", "dominican republic", "ecuador", "el salvador", "english", "estonia",
            "ethiopia", "finland", "france", "georgia", "germany", "ghana", "greece",
            "guatemala", "honduras", "hungary", "iceland", "india", "indonesia", "iran",
            "ireland", "israel", "italia", "jamaica", "japan", "kazakhstan", "kenya",
            "kosovo", "kyrgyzstan", "latvia", "liechtenstein", "lithuania", "luxembourg",
            "malaysia", "malta", "mexico", "moldova", "mongolia", "montenegro", "morocco",
            "myanmar", "nepal", "netherlands", "new zealand", "nicaragua", "north macedonia",
            "norway", "pakistan", "portugal", "quebec", "romania", "russia", "rwanda",
            "serbia", "singapore", "slovakia", "slovenia", "south africa", "south korea",
            "spain", "sri lanka", "sweden", "switzerland", "taiwan", "panama", "tajikistan",
            "thailand", "turkey", "turkmenistan", "ukraine", "united kingdom", "united states",
            "uruguay", "uzbekistan", "venezuela", "vietnam"
    );

    @PostMapping("/v1/test")
    public void test(@RequestBody TestDTO test) {
        Integer secret = test.secret();
        String country = getCountryByIndex(secret);
        String firstValue = redisUtil.getValue(secret.toString());
        System.out.println("First Value: " + firstValue);
        if (country != null) {
            redisUtil.putValue(secret.toString(), country);
            System.out.println("Country: " + country);
        } else {
            System.out.println("Invalid index for secret: " + secret);
        }

        String value = redisUtil.getValue(secret.toString());
        System.out.println("Value: " + value);
    }

    public String getCountryByIndex(int index) {
        if (index < 0 || index >= VALID_COUNTRIES.size()) {
            return null;
        }
        return VALID_COUNTRIES.get(index);
    }

}
