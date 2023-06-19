package com.vima.accommodation.service;

import com.vima.accommodation.dto.gRPCUserObject;
import com.vima.accommodation.model.Accommodation;
import com.vima.accommodation.model.RatingAccommodation;
import com.vima.accommodation.repository.AccommodationRepository;
import com.vima.accommodation.repository.RatingAccommodationRepository;
import com.vima.accommodation.util.email.EmailService;

import communication.FindUserRequest;
import communication.UserDetailsResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RatingAccommodationService {

    private final RatingAccommodationRepository ratingAccommodationRepository;
    private final AccommodationService accommodationService;
    private final AccommodationRepository accommodationRepository;
    private final EmailService emailService;
    @Value("${channel.address.auth-ms}")
    private String channelAuthAddress;

    @Transactional
    public RatingAccommodation create(int value,String accommodationId,Long guestId){
        var rating = executeRating(value, accommodationId,guestId);
        calculateRating(value,accommodationId);
        notifyHost(rating);
        return rating;
    }

    private void notifyHost(RatingAccommodation rating) {
        Accommodation accommodation = accommodationService.findById(UUID.fromString(rating.getAccommodationId()));
        UserDetailsResponse host = retrieveUser(accommodation.getHostId());
        UserDetailsResponse guest = retrieveUser(String.valueOf(rating.getGuestId()));
        if (host.getNotificationOptions().getAccommodationRating()) {
            String subject = "Accommodation rating notification";
            String body = "Dear " + host.getUsername() + ", " +
                "guest " + guest.getUsername() + " rated your accommodation " + accommodation.getName() + " with a rating " + rating.getValue() + "." +
                "Best regards," +
                "Admin";
            emailService.sendSimpleMail(host.getUsername(), subject, body);
        }
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

    public boolean edit (Long id, int newValue){
        var rating = ratingAccommodationRepository.findById(id).get();
        if(rating == null) return false;

        return executeEdit(rating, newValue);
    }

    public boolean executeEdit(RatingAccommodation rating, int newValue){
        rating.setValue(newValue);
        ratingAccommodationRepository.save(rating);
        calculateWhenNotZero(rating.getAccommodationId());
        return true;
    }

    private UserDetailsResponse retrieveUser(String userId) {
        var userBlockingStub = getBlockingUserStub();
        var user = userBlockingStub.getStub()
            .findById(FindUserRequest.newBuilder().setId(userId)
                .build());
        userBlockingStub.getChannel().shutdown();
        return user;
    }

    private gRPCUserObject getBlockingUserStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(channelAuthAddress, 9092)
            .usePlaintext()
            .build();
        return gRPCUserObject.builder()
            .channel(channel)
            .stub(communication.userDetailsServiceGrpc.newBlockingStub(channel))
            .build();
    }
}
