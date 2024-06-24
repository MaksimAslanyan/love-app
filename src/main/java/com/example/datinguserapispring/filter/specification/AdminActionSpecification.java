package com.example.datinguserapispring.filter.specification;

import com.example.datinguserapispring.dto.admin.AdminActionFetch;
import com.example.datinguserapispring.model.entity.user.AdminAction;
import com.example.datinguserapispring.model.enums.ActionType;
import org.springframework.data.jpa.domain.Specification;

public class AdminActionSpecification {

    private AdminActionSpecification() {}

    public static Specification<AdminAction> filterByActionType(ActionType actionType) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (actionType == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("actionType"), actionType);
        };
    }

    public static Specification<AdminAction> filterByAdminId(String adminId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (adminId == null || adminId.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("admin").get("id"), adminId);
        };
    }

    public static Specification<AdminAction> filterByAppleId(String appleId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (appleId == null || appleId.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("user").get("appleId"), appleId);
        };
    }

    public static Specification<AdminAction> filterByCriteria(AdminActionFetch fetchRequest) {
        return Specification
                .where(filterByActionType(fetchRequest.getActionType()))
                .and(filterByAdminId(fetchRequest.getAdminId()))
                .and(filterByAppleId(fetchRequest.getAppleId()));
    }
}
