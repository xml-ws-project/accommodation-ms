package com.vima.accommodation.dto;

import com.vima.gateway.ReservationServiceGrpc;

import io.grpc.ManagedChannel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class gRPCObject {

	ManagedChannel channel;
	ReservationServiceGrpc.ReservationServiceBlockingStub stub;
}
