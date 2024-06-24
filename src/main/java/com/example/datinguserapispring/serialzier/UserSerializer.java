package com.example.datinguserapispring.serialzier;

import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.user.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {
            jsonGenerator.writeStartObject();

            // Serialize user properties excluding photos
            jsonGenerator.writeStringField("id", user.getId());
            // Include other fields as needed

            // Serialize photos as IDs only
            jsonGenerator.writeArrayFieldStart("photos");
            for (Photo photo : user.getPhotos()) {
                try {
                    jsonGenerator.writeString(photo.getId());
                } catch (IOException e) {
                    // Handle the exception as needed (log it, throw a custom exception, etc.)
                    // For example, you can log the exception and continue with the loop
                    e.printStackTrace();
                }
            }

            // Close the array
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        } catch (IOException e) {
            // Handle the exception as needed (log it, throw a custom exception, etc.)
            throw new RuntimeException("Error during serialization", e);
        }
    }
}
