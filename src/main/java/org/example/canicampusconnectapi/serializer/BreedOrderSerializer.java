package org.example.canicampusconnectapi.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.example.canicampusconnectapi.model.dogRelated.Breed;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class BreedOrderSerializer extends JsonSerializer<List<Breed>> {

    @Override
    public void serialize(List<Breed> breeds, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        //TODO Réviser les serializers
        gen.writeStartArray();

        for (Breed breed : breeds) {
            gen.writeStartObject();

            gen.writeNumberField("id", breed.getId());
            gen.writeStringField("name", breed.getName());

            if (breed.getAvatarUrl() != null) {
                gen.writeStringField("avatarUrl", breed.getAvatarUrl());
            }

            //TODO vérifier ce qu'est le problème JSR310
            if (breed.getCreatedDate() != null) {
                gen.writeStringField("createdDate", breed.getCreatedDate().toString());
            }
            if (breed.getLastModifiedDate() != null) {
                gen.writeStringField("lastModifiedDate", breed.getLastModifiedDate().toString());
            }
            if (breed.getCreatedBy() != null) {
                gen.writeStringField("createdBy", breed.getCreatedBy());
            }
            if (breed.getLastModifiedBy() != null) {
                gen.writeStringField("lastModifiedBy", breed.getLastModifiedBy());
            }

            gen.writeEndObject();
        }

        gen.writeEndArray();
    }
}