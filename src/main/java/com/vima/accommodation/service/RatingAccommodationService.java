package com.vima.accommodation.service;

import com.vima.accommodation.model.Accommodation;
import com.vima.accommodation.model.RatingAccommodation;
import com.vima.accommodation.repository.AccommodationRepository;
import com.vima.accommodation.repository.RatingAccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingAccommodationService {

    private final RatingAccommodationRepository ratingAccommodationRepository;
    private final AccommodationService accommodationService;
    private final AccommodationRepository accommodationRepository;

    public RatingAccommodation create(int value,UUID accommodationId,Long guestId){
        var rating = executeRating(value, accommodationId,guestId);
        calculateRating(value,accommodationId);
        return rating;
    }

    private RatingAccommodation executeRating(int value, UUID accommodationId, Long guestId){
        var rating = RatingAccommodation.builder()
                .id(Math.abs(new Random().nextLong()))
                .value(value)
                .accommodationId(accommodationId)
                .guestId(guestId)
                .date(LocalDate.now())
                .build();

        ratingAccommodationRepository.save(rating);
        return rating;
    }

    private void calculateRating(int value, UUID accommodationId){
        var accommodation = accommodationService.findById(accommodationId);
        if(accommodation.getAvgRating()== 0){
            calculateWhenZero(accommodation,value);
            return;
        }
        calculateWhenNotZero(accommodation.getId());

    }

    private void calculateWhenZero(Accommodation accommodation,int value){
        accommodation.setAvgRating(value);
        accommodationRepository.save(accommodation);
    }

    private void calculateWhenNotZero(UUID accommodationId){
        var accommodation = accommodationService.findById(accommodationId);
        var number = ratingAccommodationRepository.findNumberOfAccommodationRatings(accommodation.getId());
        var sum = ratingAccommodationRepository.findSumOfAccommodationRatings(accommodation.getId());
        accommodation.setAvgRating((sum * 1.00)/number);

        accommodationRepository.save(accommodation);
    }
}
