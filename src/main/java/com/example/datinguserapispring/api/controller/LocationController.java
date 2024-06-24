package com.example.datinguserapispring.api.controller;


import com.example.datinguserapispring.dto.location.request.AddLocationForUserRequest;
import com.example.datinguserapispring.dto.user.AddressDTO;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final SecurityContextService securityContextService;

    @PostMapping("/location")
    public AddressDTO addLocationForUser(@RequestBody AddLocationForUserRequest request) {
        String id = securityContextService.getUserDetails().getUsername();
        return locationService.addLocation(request, id);
    }
}
