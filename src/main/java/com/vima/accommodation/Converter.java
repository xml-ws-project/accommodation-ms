package com.vima.accommodation;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class Converter {
	public static LocalDate convertGoogleTimeStampToLocalDate(Timestamp timestamp) {
		return Instant
			.ofEpochSecond(timestamp.getSeconds() , timestamp.getNanos())
			.atZone(ZoneOffset.UTC)
			.toLocalDate();
	}
}
