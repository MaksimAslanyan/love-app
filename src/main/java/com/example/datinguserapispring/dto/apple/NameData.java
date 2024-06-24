package com.example.datinguserapispring.dto.apple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class NameData {
    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return "NameData(firstName='" + firstName + "', lastName='" + lastName + "')";
    }
}
