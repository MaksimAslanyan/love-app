package com.example.datinguserapispring.filter.specification;

import com.example.datinguserapispring.dto.fetch.request.FetchRequest;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.Gender;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> filterByAppleId(String appleId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (appleId == null || appleId.isEmpty()) {
                return null;
            }
            return criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("appleId")), "%" + appleId.toLowerCase() + "%");
        };
    }

    public static Specification<User> filterByGender(Gender gender) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (gender == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("gender"), gender);
        };
    }


    public static Specification<User> filterByCriteria(FetchRequest fetchRequest) {
        Specification<User> specification = Specification
                .where(filterByAppleId(fetchRequest.getAppleId()))
                .and((root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.isFalse(root.get("isBlackList")));

        if (fetchRequest.getGender() != null && fetchRequest.getGender() != Gender.NOT_CHOSEN) {
            specification = specification.and(filterByGender(fetchRequest.getGender()));
        }

        specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isActive")));

        return specification;
    }

}
