package com.example.datinguserapispring.projection;

import java.time.LocalDateTime;

public interface ChatProjection {
    String getChatId();

    Boolean getIsMatch();

    String getBot2Id();

    Byte getAge();

    String getName();

    Double getDistance();

    Boolean getIsOnline();

    String getChatMessageId();

    LocalDateTime getLastMessageDate();

    String getLastMessage();

    Boolean getIsRead();

    String getChatMember();

    String getChatMemberAdmin();

    String getPhotoId();
}


