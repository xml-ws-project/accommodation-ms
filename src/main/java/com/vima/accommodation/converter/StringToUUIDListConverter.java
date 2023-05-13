package com.vima.accommodation.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StringToUUIDListConverter {

	public static List<UUID> convert(List<String> ids) {
		List<UUID> uuids = new ArrayList<>();
		ids.forEach(id -> {
			uuids.add(UUID.fromString(id));
		});
		return uuids;
	}
}
