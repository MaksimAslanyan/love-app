package com.example.datinguserapispring.service.block.impl;

import com.example.datinguserapispring.dto.block.request.BlockRequest;
import com.example.datinguserapispring.dto.block.response.BlockResponse;
import com.example.datinguserapispring.exception.AuthenticationException;
import com.example.datinguserapispring.exception.Bot2NotFoundException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.Block;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.UserType;
import com.example.datinguserapispring.repository.BlockRepository;
import com.example.datinguserapispring.repository.Bot2Repository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.service.block.BlockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final Bot2Repository bot2Repository;


    @Transactional
    @Override
    public BlockResponse block(BlockRequest blockRequest, String id) {
        User blockingUser = getUserById(id);
        Bot2 blockedBot = getBot2ById(blockRequest.getBlockedUserId());
        Block existingBlock = findExistingBlock(blockingUser, blockedBot);

        if (existingBlock == null) {
            createAndSaveBlock(blockingUser, blockedBot);
        } else {
            toggleBlockAndSave(existingBlock);
        }

        return new BlockResponse(blockRequest.getBlockedUserId());
    }

    private void createAndSaveBlock(User blockingUser, Bot2 blockedBot) {
        Block block = Block.builder()
                .blockingUser(blockingUser)
                .blockedBot(blockedBot)
                .isBlocked(true)
                .userType(UserType.BOT)
                .build();
        blockRepository.save(block);
    }

    private void toggleBlockAndSave(Block existingBlock) {
        existingBlock.setBlocked(!existingBlock.isBlocked());
        blockRepository.save(existingBlock);
    }

    private User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException(Error.USER_NOT_FOUND));
    }

    private Bot2 getBot2ById(String botId) {
        return bot2Repository.findById(botId)
                .orElseThrow(() -> new Bot2NotFoundException(Error.BOT2_NOT_FOUND));
    }

    private Block findExistingBlock(User blockingUser, Bot2 blockedBot) {
        return blockRepository.findByBlockingUserIdAndBlockedBotId(blockingUser.getId(), blockedBot.getId());
    }

}
