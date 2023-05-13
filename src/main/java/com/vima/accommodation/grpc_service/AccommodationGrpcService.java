package com.vima.accommodation.grpc_service;

import com.vima.accommodation.service.AdditionalBenefitService;
import com.vima.accommodation.service.SpecialInfoService;
import com.vima.gateway.SearchList;
import com.vima.gateway.SearchRequest;
import com.vima.accommodation.mapper.AccommodationMapper;
import com.vima.accommodation.mapper.SpecialInfoMapper;
import com.vima.accommodation.service.AccommodationService;
import com.vima.gateway.AccommodationList;
import com.vima.gateway.AccommodationRequest;
import com.vima.gateway.AccommodationResponse;
import com.vima.gateway.AccommodationServiceGrpc;
import com.vima.gateway.AdditionalBenefitRequest;
import com.vima.gateway.AdditionalBenefitResponse;
import com.vima.gateway.Empty;
import com.vima.gateway.SpecialInfoRequest;
import com.vima.gateway.SpecialInfoResponse;
import com.vima.gateway.UpdateAccommodationRequest;
import com.vima.gateway.Uuid;

import java.util.UUID;

import net.devh.boot.grpc.server.service.GrpcService;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@GrpcService
@RequiredArgsConstructor
public class AccommodationGrpcService extends AccommodationServiceGrpc.AccommodationServiceImplBase {

	private final AccommodationService accommodationService;
	private final SpecialInfoService specialInfoService;
	private final AdditionalBenefitService additionalBenefitService;

	@Override
	public void findAll(Empty request, StreamObserver<AccommodationList> responseObserver) {
		var accommodationList = AccommodationMapper.convertEntityToDtoList(accommodationService.findAll());
		AccommodationList responseList = AccommodationList.newBuilder()
				.addAllResponse(accommodationList)
				.build();
		responseObserver.onNext(responseList);
		responseObserver.onCompleted();
	}

	@Override
	public void create(AccommodationRequest request, StreamObserver<AccommodationResponse> responseObserver) {
		var accommodation = AccommodationMapper.convertEntityToDto(accommodationService.create(request));
		responseObserver.onNext(accommodation);
		responseObserver.onCompleted();
	}

	@Override
	public void update(UpdateAccommodationRequest request, StreamObserver<Empty> responseObserver) {
		accommodationService.update(request);
		Empty empty = Empty.newBuilder().build();
		responseObserver.onNext(empty);
		responseObserver.onCompleted();
	}

	@Override
	public void findById(Uuid request, StreamObserver<AccommodationResponse> responseObserver) {
		var accommodation = AccommodationMapper.convertEntityToDto(accommodationService.findById(UUID.fromString(request.getValue())));
		responseObserver.onNext(accommodation);
		responseObserver.onCompleted();
	}

	@Override
	public void findAllByHostId(Uuid request, StreamObserver<AccommodationList> responseObserver) {
		var accommodationList = AccommodationMapper.convertEntityToDtoList(accommodationService.findAllByHostId(request.getValue()));
		AccommodationList responseList = AccommodationList.newBuilder()
			.addAllResponse(accommodationList)
			.build();
		responseObserver.onNext(responseList);
		responseObserver.onCompleted();
	}

	@Override
	public void addBenefit(AdditionalBenefitRequest request, StreamObserver<AdditionalBenefitResponse> responseObserver) {
		var benefit = additionalBenefitService.addBenefit(request);
		AdditionalBenefitResponse benefitResponse = AdditionalBenefitResponse.newBuilder()
				.setId(benefit.getId().toString())
				.setName(benefit.getName())
				.setIcon(benefit.getIcon())
				.build();
		responseObserver.onNext(benefitResponse);
		responseObserver.onCompleted();
	}

	@Override
	public void createSpecialPeriod(SpecialInfoRequest request, StreamObserver<SpecialInfoResponse> responseObserver) {
		var specialInfo = SpecialInfoMapper.convertEntityToDto(specialInfoService.createSpecialPeriod(request));
		responseObserver.onNext(specialInfo);
		responseObserver.onCompleted();
	}

	@Override
	public void searchAccommodation(SearchRequest request, StreamObserver<SearchList> responseObserver) {
		var searchResponse = accommodationService.searchAccommodations(request);
		responseObserver.onNext(searchResponse);
		responseObserver.onCompleted();
	}
}
