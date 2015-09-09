package sample.data.jpa.service;

import sample.data.jpa.domain.Rating;

public interface ReviewsSummary {

	long getNumberOfReviewsWithRating(Rating rating);

}
