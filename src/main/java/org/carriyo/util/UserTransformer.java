package org.carriyo.util;

import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserTransformer {
    public static String getDocumentJsonString(Map<String, AttributeValue> newImage) throws JsonProcessingException {
        String firstName = newImage.get("firstName").getS();
        String lastName = newImage.get("lastName").getS();
        String userId = newImage.get("userid").getS();
        String email = newImage.get("email").getS();

        Map<String, AttributeValue> rawMobileNumber = newImage.get("mobileNumber").getM();
        String number = rawMobileNumber.get("number").getS();
        String countryCode = rawMobileNumber.get("countryCode").getS();
        MobileNumber mobileNumber = new MobileNumber(number, countryCode);

        List<AttributeValue> rawAddresses = newImage.get("addresses").getL();
        List<Address> addresses = new ArrayList<>();
        rawAddresses.forEach(address -> addresses.add(parseAddress(address.getM())));

        User user = new User(userId, firstName, lastName, email, mobileNumber, addresses);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(user);
    }

    public static Address parseAddress(Map<String, AttributeValue> rawAddress) {
        String country = rawAddress.get("country").getS();
        String city = rawAddress.get("city").getS();
        String postalCode = rawAddress.get("postalCode").getS();
        String addressLine1 = rawAddress.get("addressLine1").getS();
        String addressLine2 = rawAddress.get("addressLine2").getS();
        String addressLine3 = rawAddress.get("addressLine3").getS();
        String state = rawAddress.get("state").getS();
        String landmark = rawAddress.get("landmark").getS();

        List<AttributeValue> rawTags = rawAddress.get("addressTags").getL();
        List<String> tags = new ArrayList<>();
        rawTags.forEach(tag -> tags.add(tag.getS()));

        return new Address(addressLine1, addressLine2, addressLine3, landmark, postalCode, city, state, country, tags);
    }

}
