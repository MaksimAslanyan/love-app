package com.example.datinguserapispring.service.baninfo.impl;

import com.example.datinguserapispring.dto.ban.request.BanInfoRequest;
import com.example.datinguserapispring.dto.ban.response.CreateBanResponse;
import com.example.datinguserapispring.dto.ban.response.DeleteBanResponse;
import com.example.datinguserapispring.dto.ban.response.GetBanInfoResponse;
import com.example.datinguserapispring.model.entity.user.BanInfo;
import com.example.datinguserapispring.repository.BanInfoRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.service.baninfo.BanInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BanInfoServiceImpl implements BanInfoService {

    private final BanInfoRepository banInfoRepository;
    private final UserRepository userRepository;


    public CreateBanResponse banUser(String userId, BanInfoRequest request) {
        BanInfo banInfo = BanInfo.builder()
                .user(userRepository.findById(userId).get())
                .dateUntil(request.getDateUntil())
                .isForever(request.isForever())
                .reason(request.getReason())
                .build();
        banInfoRepository.save(banInfo);
        return new CreateBanResponse(userId);
    }

    public GetBanInfoResponse getByUserId(String userId) {
        BanInfo banInfo = banInfoRepository.findFirstByUserId(userId).orElse(null);

        if (banInfo != null) {
            return new GetBanInfoResponse(
                    true,
                    banInfo.getDateUntil(),
                    banInfo.isForever(),
                    banInfo.getReason()
            );
        } else {
            return new GetBanInfoResponse(
                    false,
                    null,
                    null,
                    null
            );
        }
    }

    @Transactional
    public DeleteBanResponse deleteBan(String userId) {
        banInfoRepository.deleteBanInfoByUserId(userId);
        return new DeleteBanResponse(userId);
    }
}