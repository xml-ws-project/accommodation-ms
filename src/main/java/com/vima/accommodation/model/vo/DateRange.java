package com.vima.accommodation.model.vo;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Embeddable
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DateRange {

	LocalDate start;
	LocalDate end;

	public DateRange(LocalDate start, LocalDate end) {
		if (start.isAfter(end)) throw new IllegalStateException();
		this.start = start;
		this.end = end;
	}

	public boolean isPartlyOverlap(DateRange dateRange) {
		return isIncludingDate(dateRange.start) || isIncludingDate(dateRange.end);
	}

	public boolean isIncludingDate(LocalDate date) {
		return (start.isBefore(date) || start.isEqual(date)) && (end.isAfter(date) || end.isEqual(date));
	}

	public boolean isIncludingPeriod(DateRange dateRange) {
		return (start.isBefore(dateRange.start) || start.isEqual(dateRange.start)) && (end.isAfter(dateRange.end) || end.isEqual(dateRange.end));
	}

	public boolean isEmpty() {
		return start == null && end == null;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof final DateRange dateRange)) {
			return false;
		}
		return start.equals(dateRange.start) && end.equals(dateRange.end);
	}

	@Override
	public int hashCode() {
		return Objects.hash(start, end);
	}
}
