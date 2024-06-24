package com.example.datinguserapispring.service.location;

import com.example.datinguserapispring.dto.user.AddressDTO;
import com.example.datinguserapispring.dto.location.request.AddLocationForUserRequest;

public interface LocationService {

    AddressDTO addLocation(AddLocationForUserRequest userRequest, String id);
}
