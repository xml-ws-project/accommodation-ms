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

    public RatingAccommodation create(int value,String accommodationId,Long guestId){
        var rating = executeRating(value, accommodationId,guestId);
        calculateRating(value,accommodationId);
        return rating;
    }

    private RatingAccommodation executeRating(int value, String accommodationId, Long guestId){
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

    private void calculateRating(int value, String accommodationId){
        var accommodation = accommodationService.findById(UUID.fromString(accommodationId));
        if(accommodation.getAvgRating() == 0){
            calculateWhenZero(accommodation,value);
            return;
        }
        calculateWhenNotZero(accommodation.getId().toString());

    }

    private void calculateWhenZero(Accommodation accommodation,int value){
        accommodation.setAvgRating(value);
        accommodationRepository.save(accommodation);
    }

    private void calculateWhenNotZero(String accommodationId){
        var accommodation = accommodationService.findById(UUID.fromString(accommodationId));
        var avg = ratingAccommodationRepository.findAvg(accommodationId);
        accommodation.setAvgRating(avg);
        accommodationRepository.save(accommodation);
    }

    public boolean delete(Long id){
        var rating = ratingAccommodationRepository.findById(id).get();
        if(rating == null) return false;
        ratingAccommodationRepository.delete(rating);
        executeDelete(rating.getAccommodationId());
        return true;
    }

    private void executeDelete(String accommodationId){
        var numOfRates = ratingAccommodationRepository.findNumberOfAccommodationRatings(accommodationId);
        var accommodation = accommodationService.findById(UUID.fromString(accommodationId));
        if(numOfRates == 0){
            accommodation.setAvgRating(0);
            accommodationRepository.save(accommodation);
        }
        else {
            calculateWhenNotZero(accommodation.getId().toString());
        }
    }
}
