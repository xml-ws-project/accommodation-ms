package com.vima.accommodation.service;

import com.vima.accommodation.model.SpecialInfo;
import com.vima.gateway.SpecialInfoRequest;

import java.util.List;
import java.util.UUID;

public interface SpecialInfoService {
	SpecialInfo createSpecialPeriod(SpecialInfoRequest request);
}
