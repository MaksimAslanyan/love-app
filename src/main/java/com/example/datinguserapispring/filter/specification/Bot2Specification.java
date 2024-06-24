package com.example.datinguserapispring.filter.specification;

import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.enums.ParentType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class Bot2Specification {

    private Bot2Specification() {}

    public static Specification<Bot2> findByBot1IdBot2IdAndParentType(String bot1Id,
                                                                      String bot2Id,
                                                                      ParentType parentType) {
        return (Root<Bot2> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate bot1IdPredicate = criteriaBuilder.equal(root.get("bot1").get("id"), bot1Id);
            Predicate bot2IdLikePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("id")), "%" + bot2Id.toLowerCase() + "%");
            Predicate parentTypePredicate = criteriaBuilder.equal(root.get("parentType"), parentType);

            return criteriaBuilder.and(bot1IdPredicate, bot2IdLikePredicate, parentTypePredicate);
        };
    }

}
