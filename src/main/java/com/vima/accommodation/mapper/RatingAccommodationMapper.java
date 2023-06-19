package com.vima.accommodation.mapper;

import com.vima.accommodation.converter.LocalDateConverter;
import com.vima.accommodation.model.RatingAccommodation;
import com.vima.gateway.RatingAccommodationServiceOuterClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingAccommodationMapper {

    public static RatingAccommodationServiceOuterClass.RatingAccommodationResponse convertRatingAccommodationToRatingAccommodationResponse(RatingAccommodation rating){
        var response = RatingAccommodationServiceOuterClass.RatingAccommodationResponse.newBuilder()
                .setId(rating.getId())
                .setValue(rating.getValue())
                .setAccommodationId(rating.getAccommodationId().toString())
                .setGuestId(rating.getGuestId())
                .setDate(LocalDateConverter.convertLocalDateToGoogleTimestamp(rating.getDate()))
                .build();

        return response;
    }
}
