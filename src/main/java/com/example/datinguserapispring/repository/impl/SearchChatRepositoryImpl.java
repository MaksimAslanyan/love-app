package com.example.datinguserapispring.repository.impl;

import com.example.datinguserapispring.dto.chat.AdminChatDTO;
import com.example.datinguserapispring.dto.chat.ChatCategory;
import com.example.datinguserapispring.dto.chat.ChatDTO;
import com.example.datinguserapispring.model.entity.bot.Bot1;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.chat.Chat;
import com.example.datinguserapispring.model.entity.chat.ChatMember;
import com.example.datinguserapispring.model.entity.chat.ChatMemberAdmin;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.ChatType;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.repository.SearchChatRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Repository
public class SearchChatRepositoryImpl implements SearchChatRepository{

    private final EntityManager entityManager;

    public SearchChatRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<AdminChatDTO> findUnreadMessages(String adminId, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = buildQuery(cb, adminId, new ChatCategory());

        return getAdminChatDTOS(pageable, query);
    }

    private List<AdminChatDTO> getAdminChatDTOS(Pageable pageable, CriteriaQuery<Object[]> query) {
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Object[]> resultList = typedQuery.getResultList();

        List<AdminChatDTO> adminChatDTOs = new ArrayList<>();
        for (Object[] result : resultList) {
            AdminChatDTO adminChatDTO = new AdminChatDTO(
                    (Chat) result[0],       // chat_id
                    (String) result[1],     // bot2_id
                    (String) result[2],     // bot2_name
                    (String) result[3],     // admin_id
                    (String) result[4],     // bot1_id
                    (Gender) result[5],     // bot1_gender
                    (String) result[6],     // bot1_race
                    (Byte) result[7],       // bot1_minAge
                    (Byte) result[8],       // bot1_maxAge
                    (String) result[9],     // userId
                    (String) result[10],    // name
                    (Gender) result[11],    // gender
                    (Boolean) result[12],   // isPremium
                    (Boolean) result[13]    // isBlackList
            );
            adminChatDTOs.add(adminChatDTO);
        }
        return adminChatDTOs;
    }

    private CriteriaQuery<Object[]> buildQuery(CriteriaBuilder cb, String adminId, ChatCategory category) {
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<ChatMemberAdmin> chatMemberAdminRoot = query.from(ChatMemberAdmin.class);
        Join<ChatMemberAdmin, Bot2> bot2Join = chatMemberAdminRoot.join("bot2", JoinType.INNER);
        Join<ChatMemberAdmin, Chat> chatJoin = chatMemberAdminRoot.join("chat", JoinType.INNER);
        Join<Bot2, Bot1> bot1Join = bot2Join.join("bot1", JoinType.INNER);
        Join<Chat, ChatMember> chatMemberJoin = chatJoin.join("chatMembers", JoinType.INNER);
        Join<ChatMember, User> userJoin = chatMemberJoin.join("user", JoinType.INNER);

        buildMultiselectCriteria(
                query,
                bot2Join,
                chatJoin,
                bot1Join,
                userJoin);


        Predicate adminIdPredicate = cb.equal(bot2Join.get("admin").get("id"), adminId);
        Predicate chatTypePredicate = buildChatTypePredicate(cb, chatJoin, category);
        Predicate finalPredicate = cb.and(adminIdPredicate, chatTypePredicate);

        query.where(finalPredicate).orderBy(cb.desc(chatJoin.get("lastActivity")));
        query.distinct(true);


        return query;
    }

    private void buildMultiselectCriteria(CriteriaQuery<Object[]> query,
                                          Join<ChatMemberAdmin, Bot2> bot2Join,
                                          Join<ChatMemberAdmin, Chat> chatJoin,
                                          Join<Bot2, Bot1> bot1Join,
                                          Join<ChatMember, User> userJoin) {
        query.multiselect(
                chatJoin.alias("chat"),
                bot2Join.get("id").alias("bot2_id"),
                bot2Join.get("nameBot").alias("bot2_name"),
                bot2Join.get("admin").get("id").alias("admin_id"),
                bot1Join.get("id").alias("bot1_id"),
                bot1Join.get("gender").alias("bot1_gender"),
                bot1Join.get("race").alias("bot1_race"),
                bot1Join.get("minAge").alias("bot1_minAge"),
                bot1Join.get("maxAge").alias("bot1_maxAge"),
                userJoin.get("id").alias("id"),
                userJoin.get("name").alias("name"),
                userJoin.get("gender").alias("gender"),
                userJoin.get("isPremium").alias("isPremium"),
                userJoin.get("isBlackList").alias("isBlackList")
        );
    }

    private Predicate buildChatTypePredicate(CriteriaBuilder cb, Join<ChatMemberAdmin, Chat> chatJoin, ChatCategory category) {
        if (category != null && category.getChatType() != null) {
            return cb.equal(chatJoin.get("chatType"), category.getChatType());
        } else {
            return cb.notEqual(chatJoin.get("chatType"), ChatType.NULL);
        }
    }

}
