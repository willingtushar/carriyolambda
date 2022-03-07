package org.carriyo.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private MobileNumber mobileNumber;
    private List<Address> addresses;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class MobileNumber{
    private String countryCode;
    private String number;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Address {
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String landmark;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private List<String> addressTags;
}
