package com.example.datinguserapispring.dto.user.request;

import com.example.datinguserapispring.dto.lookingFor.LookingForRequest;
import com.example.datinguserapispring.model.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class PatchProfileRequest {
    @JsonProperty("name")
    @NotNull
    @NotBlank
    private String name;

    @JsonProperty("city")
    private String city;

    @JsonProperty("country")
    private String country;

    @JsonProperty("gender")
    @NotNull
    @NotBlank
    private Gender gender;

    @JsonProperty("targetGenders")
    private List<String> targetGenders;

    @JsonProperty("dob")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @JsonProperty("lookingFor")
    private LookingForRequest lookingFor;


}
