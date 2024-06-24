package com.example.datinguserapispring.repository.impl;


import com.example.datinguserapispring.dto.location.LatLonDTO;
import com.example.datinguserapispring.dto.search.request.SearchByCriteria;
import com.example.datinguserapispring.model.entity.Location;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.SearchRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

import static com.example.datinguserapispring.constants.Geo.*;

@Repository
public class SearchRepositoryImpl implements SearchRepository {


    private static final String LOCATIONS = "locations";
    private static final double[] PREMIUM_DISTANCE_PROBABILITIES = {0.15, 0.30, 0.25, 0.15, 0.10, 0.05};
    private static final double[] FREE_DISTANCE_PROBABILITIES = {0.50, 0.30, 0.15, 0.05};
    private static final int MIN_FREE_DISTANCE = 4;
    private static final int MIN_PREMIUM_DISTANCE = 1;
    private static final int MAX_DISTANCE = 30;
    private final EntityManager entityManager;

    public SearchRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> searchUserByCriteria(LatLonDTO myPlace, SearchByCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        Join<User, Location> locationJoin = userRoot.join(LOCATIONS);

        // Calculate the differences between the latitudes and longitudes
        Expression<Double> latDiff = calculateLatitudeDifference(criteriaBuilder, locationJoin, myPlace.getLat());
        Expression<Double> lonDiff = calculateLongitudeDifference(criteriaBuilder, locationJoin, myPlace.getLon());

        // Calculate the distance using the Haversine formula
        Expression<Double> distance = calcuateDistance(criteriaBuilder, latDiff, lonDiff);

        // Create predicates for age and radius criteria
        Predicate agePredicate = criteriaBuilder.between(userRoot.get("age"), criteria.getAgeFrom(), criteria.getAgeTo());
        Predicate radiusPredicate = criteriaBuilder.le(distance, criteria.getRadius());

        // Combine the predicates using conjunction (AND)
        Predicate combinedPredicate = criteriaBuilder.and(agePredicate, radiusPredicate);

        // Set the combined predicate as the where clause in the query
        criteriaQuery.where(combinedPredicate);

        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(criteria.getMaxResults());

        return query.getResultList();
    }



    private static Expression<Double> calcuateDistance(CriteriaBuilder criteriaBuilder,
                                                       Expression<Double> latDiff,
                                                       Expression<Double> lonDiff) {
        Expression<Double> a = calculateA(criteriaBuilder, latDiff, lonDiff);
        Expression<Double> c = calculateC(criteriaBuilder, a);

        return criteriaBuilder.prod(EARTH_RADIUS, c);
    }

    private static Expression<Double> calculateA(CriteriaBuilder criteriaBuilder,
                                                 Expression<Double> latDiff,
                                                 Expression<Double> lonDiff) {

        Expression<Double> sinLatDiffHalf = criteriaBuilder
                .function(SIN, Double.class, criteriaBuilder.quot(latDiff, 2.0));
        Expression<Double> sinLonDiffHalf = criteriaBuilder
                .function(SIN, Double.class, criteriaBuilder.quot(lonDiff, 2.0));
        Expression<Double> cosLatDiffHalf = criteriaBuilder
                .function(COS, Double.class, criteriaBuilder.quot(latDiff, 2.0));
        Expression<Double> cosLonDiffHalf = criteriaBuilder
                .function(COS, Double.class, criteriaBuilder.quot(lonDiff, 2.0));

        Expression<Double> sinLatDiffHalfSquared = criteriaBuilder
                .prod(sinLatDiffHalf, sinLatDiffHalf);
        Expression<Double> sinLonDiffHalfSquared = criteriaBuilder
                .prod(sinLonDiffHalf, sinLonDiffHalf);
        Expression<Double> cosLatDiffHalfCosLonDiffHalf = criteriaBuilder
                .prod(cosLatDiffHalf, cosLonDiffHalf);

        return criteriaBuilder.sum(sinLatDiffHalfSquared, criteriaBuilder
                .prod(cosLatDiffHalfCosLonDiffHalf, sinLonDiffHalfSquared));
    }

    private static Expression<Double> calculateC(CriteriaBuilder criteriaBuilder, Expression<Double> a) {
        Expression<Double> sqrtA = criteriaBuilder
                .function(SQRT, Double.class, a);
        Expression<Double> sqrtOneMinusA = criteriaBuilder
                .function(SQRT, Double.class, criteriaBuilder.diff(1.0, a));
        Expression<Double> atan2 = criteriaBuilder.function(ATAN2, Double.class, sqrtA, sqrtOneMinusA);

        return criteriaBuilder.prod(2.0, atan2);
    }

    private Expression<Double> calculateLatitudeDifference(CriteriaBuilder criteriaBuilder,
                                                           Join<User, Location> locationJoin,
                                                           double latitude) {
        Expression<Double> lat1Rad = criteriaBuilder
                .prod(criteriaBuilder.literal(latitude), DEGREES_TO_RADIANS);
        Expression<Double> lat2Rad = criteriaBuilder
                .prod(locationJoin.get(LAT).as(Double.class), DEGREES_TO_RADIANS);

        return criteriaBuilder.diff(lat2Rad, lat1Rad);
    }

    private Expression<Double> calculateLongitudeDifference(CriteriaBuilder criteriaBuilder,
                                                            Join<User, Location> locationJoin,
                                                            double longitude) {
        Expression<Double> lon1Rad = criteriaBuilder
                .prod(criteriaBuilder.literal(longitude), DEGREES_TO_RADIANS);
        Expression<Double> lon2Rad = criteriaBuilder
                .prod(locationJoin.get(LON).as(Double.class), DEGREES_TO_RADIANS);

        return criteriaBuilder.diff(lon2Rad, lon1Rad);
    }
}
