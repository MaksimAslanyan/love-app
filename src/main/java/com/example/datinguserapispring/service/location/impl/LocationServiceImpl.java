package com.example.datinguserapispring.service.location.impl;


import com.example.datinguserapispring.dto.location.request.AddLocationForUserRequest;
import com.example.datinguserapispring.dto.user.AddressDTO;
import com.example.datinguserapispring.exception.ConvertingException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.model.entity.Location;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.AddressRepository;
import com.example.datinguserapispring.repository.LocationRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private static final String GET = "GET";
    private static final String ACCEPT_LANGUAGE = "accept-language";
    private static final String EN = "en";
    private static final String ADDRESS = "address";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String VILLAGE = "village";
    private static final String STATE = "state";
    private static final String DEFAULT = "default";

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;


    @Override
    @Transactional
    public AddressDTO addLocation(AddLocationForUserRequest userRequest, String id) {
        log.info("Saving location with this ID: {}", id);
        if (userRequest.getLon() == 0 && userRequest.getLat() == 0){
            return null;
        }
        AddressDTO savedLocation = saveLocationForUser(userRequest, id);
        log.info("Location saved successfully for user with ID: {}", id);
        return new AddressDTO(savedLocation.getCountry(), savedLocation.getCity());
    }

    private AddressDTO saveLocationForUser(AddLocationForUserRequest userRequest, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        AddressDTO userLocation =
                findUserLocation(userRequest);
        Address address =
                new Address(user, userLocation.getCity(), userLocation.getCountry());

        Location location = createLocation(userRequest, user);
        if (userLocation.getCity() != null || userLocation.getCountry() != null) {
            addressRepository.deleteAllByUserId(userId);
            addressRepository.save(address);
            locationRepository.save(location);
        }

        return new AddressDTO(userLocation.getCountry(), userLocation.getCity());
    }

    private AddressDTO findUserLocation(AddLocationForUserRequest location) {
        String city = null;
        String country = null;
        AddressDTO defaultResponse = validateLocation(location);
        if (defaultResponse != null) return defaultResponse;

        try {
            String urlString = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" +
                    location.getLat() + "&lon=" + location.getLon();
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET);
            connection.setRequestProperty(ACCEPT_LANGUAGE, EN);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    String response = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject addressObject = jsonObject.optJSONObject(ADDRESS);
                    if (addressObject != null) {
                        if (addressObject.has(COUNTRY)) {
                            country = addressObject.getString(COUNTRY);
                        }
                        if (addressObject.has(CITY)) {
                            city = addressObject.getString(CITY);
                        }
                        if (addressObject.has(VILLAGE)) {
                            city = addressObject.getString(VILLAGE);
                        }
                        if (addressObject.has(STATE) && city == null) {
                            city = addressObject.getString(STATE);
                        }
                    }
                }
            }

            connection.disconnect();
        } catch (Exception e) {
            throw new ConvertingException(Error.FAILED_JSON_CONVERTING);
        }

        return new AddressDTO(country, city);
    }

    private AddressDTO validateLocation(AddLocationForUserRequest location) {
        double lat = location.getLat();
        double lon = location.getLon();
        String zoneDateId = location.getTimeZone();

        if (lat == 0.0 && lon == 0.0 && zoneDateId.isEmpty()) {
            return new AddressDTO(DEFAULT, DEFAULT);
        }
        return null;
    }

    private Location createLocation(AddLocationForUserRequest userRequest, User user) {
        return Location.builder()
                .user(user)
                .lat(userRequest.getLat())
                .lon(userRequest.getLon())
                .timeZone(userRequest.getTimeZone())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
