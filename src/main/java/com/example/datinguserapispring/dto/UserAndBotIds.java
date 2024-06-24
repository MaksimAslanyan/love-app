package com.example.datinguserapispring.dto;


import java.util.Set;

public record UserAndBotIds(Set<String> bot2Ids, Set<String> userIds){}

