package com.vima.accommodation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rating", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingAccommodation {

    @Id
    @Column(name = "id",nullable = false,unique = true)
    Long id;

    @Column(nullable = false)
    int value;

    @Column(nullable = false)
    String accommodationId;

    @Column(nullable = false)
    Long guestId;

    @Column(nullable = false)
    LocalDate date;
}
