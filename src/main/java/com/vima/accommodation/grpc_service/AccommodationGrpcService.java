package com.vima.accommodation.grpc_service;

import com.vima.accommodation.dto.gRPCObjectRec;
import com.vima.accommodation.mapper.AdditionalBenefitMapper;
import com.vima.accommodation.service.AdditionalBenefitService;
import com.vima.accommodation.service.SpecialInfoService;
import com.vima.gateway.*;
import com.vima.accommodation.mapper.AccommodationMapper;
import com.vima.accommodation.mapper.SpecialInfoMapper;
import com.vima.accommodation.service.AccommodationService;

import java.util.List;
import java.util.UUID;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.server.service.GrpcService;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@GrpcService
@RequiredArgsConstructor
public class AccommodationGrpcService extends AccommodationServiceGrpc.AccommodationServiceImplBase {

	private final AccommodationService accommodationService;
	private final SpecialInfoService specialInfoService;
	private final AdditionalBenefitService additionalBenefitService;
	private static final String DELETE_MESSAGE = "Accommodations are successfully deleted.";

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
		createAccomNode(accommodation.getId());
		responseObserver.onNext(accommodation);
		responseObserver.onCompleted();
	}

	private void createAccomNode(String userId){
		getBlockingStub().getStub().createAccomNode(Uuid.newBuilder().setValue(userId).build());
		getBlockingStub().getChannel().shutdown();
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

	@Override
	public void findAllBenefits(Empty empty, StreamObserver<BenefitList> responseObserver) {
		var response = additionalBenefitService.findAll();
		BenefitList benefitList = BenefitList.newBuilder()
			.addAllResponse(AdditionalBenefitMapper.convertEntityToDtoList(response))
			.build();
		responseObserver.onNext(benefitList);
		responseObserver.onCompleted();
	}

	@Override
	public void deleteHostAccommodations(DeleteHostAccommodationsRequest request, StreamObserver<DeleteHostAccommodationResponse> responseObserver) {
		accommodationService.deleteAllByHostId(Long.toString(request.getId()));
		DeleteHostAccommodationResponse response = DeleteHostAccommodationResponse.newBuilder()
			.setMessage(DELETE_MESSAGE)
			.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void findRecommended(Uuid request, StreamObserver<AccommodationList> responseObserver){
		var recommendedIds = getIds(request);
		var recommended = accommodationService.findRecommended(recommendedIds);
		responseObserver.onNext(AccommodationList.newBuilder().addAllResponse(AccommodationMapper.convertEntityToDtoList(recommended)).build());
		responseObserver.onCompleted();
	}

	private List<String> getIds(Uuid userId){
		var ids = getBlockingStub().getStub().recommend(userId);
		getBlockingStub().getChannel().shutdown();
		return ids.getIdsList();
	}

	private gRPCObjectRec getBlockingStub() {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9095)
				.usePlaintext()
				.build();
		return gRPCObjectRec.builder()
				.channel(channel)
				.stub(RecommendationServiceGrpc.newBlockingStub(channel))
				.build();
	}

	public void filterAccommodations(AccommodationFilterRequest request, StreamObserver<SearchList> responseObserver){

		var searchResponse = accommodationService.filterAccommodation(request);
		responseObserver.onNext(searchResponse);
		responseObserver.onCompleted();
	}

}
