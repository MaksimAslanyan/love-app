package com.example.datinguserapispring.projection;

import java.time.LocalDateTime;

public interface AdminChatProjection {
    String getChatId();
    String getChatType();
    String getUserId();
    String getGender();
    String getName();
    Boolean getIsPremium();
    Boolean getIsBlackList();
    String getCity();
    String getCountry();
    String getBot2Id();
    Byte getAge();
    String getNameBot();
    String getBot1Id();
    String getBotGender();
    String getRace();
    Byte getMinAge();
    Byte getMaxAge();
    String getChatMessageId();
    LocalDateTime getLastMessageDate();
    LocalDateTime getLastActivity();
    String getLastMessage();
    Boolean getIsRead();
    String getChatMemberId();
    String getChatMemberAdmin();
    String getPhotoId();
    Boolean getIsOnline();

}
