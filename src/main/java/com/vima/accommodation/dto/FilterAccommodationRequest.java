package com.vima.accommodation.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterAccommodationRequest {

    int minPrice;
    int maxPrice;
    List<String> benefits;
    String hostName;
}
