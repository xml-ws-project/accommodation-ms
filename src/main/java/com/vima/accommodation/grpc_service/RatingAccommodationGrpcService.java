package com.vima.accommodation.grpc_service;

import com.vima.accommodation.mapper.RatingAccommodationMapper;
import com.vima.accommodation.service.RatingAccommodationService;
import com.vima.gateway.RatingAccommodationServiceGrpc;
import com.vima.gateway.RatingAccommodationServiceOuterClass;
import com.vima.gateway.TextMessage;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;
import java.util.stream.Stream;

@GrpcService
@RequiredArgsConstructor
public class RatingAccommodationGrpcService extends RatingAccommodationServiceGrpc.RatingAccommodationServiceImplBase {

    private final RatingAccommodationService ratingAccommodationService;

    @Override
    public void create(RatingAccommodationServiceOuterClass.RatingAccommodationRequest request, StreamObserver<RatingAccommodationServiceOuterClass.RatingAccommodationResponse> responseObserver){
        var result = ratingAccommodationService.create(request.getValue(), request.getAccommodationId(),request.getGuestId());
        responseObserver.onNext(RatingAccommodationMapper.convertRatingAccommodationToRatingAccommodationResponse(result));
        responseObserver.onCompleted();
    }

    @Override
    public void delete(RatingAccommodationServiceOuterClass.ID request, StreamObserver<TextMessage> responseObserver){
        var result = ratingAccommodationService.delete(request.getId());
        responseObserver.onNext(TextMessage.newBuilder().setValue(result ? "Rating deleted." : "Error!").build());
        responseObserver.onCompleted();
    }

    @Override
    public void edit(RatingAccommodationServiceOuterClass.EditRatingAccommodationRequest request, StreamObserver<TextMessage> responseObserver){
        var result = ratingAccommodationService.edit(request.getId(), request.getNewValue());
        responseObserver.onNext(TextMessage.newBuilder().setValue(result ? "Rating edited" : "Error!").build());
        responseObserver.onCompleted();
    }
}

