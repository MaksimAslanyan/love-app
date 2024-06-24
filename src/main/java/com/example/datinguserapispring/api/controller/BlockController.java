package com.example.datinguserapispring.api.controller;


import com.example.datinguserapispring.dto.block.request.BlockRequest;
import com.example.datinguserapispring.dto.block.response.BlockResponse;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.block.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;
    private final SecurityContextService securityContextService;

    @PostMapping("/block")
    public BlockResponse block(@RequestBody BlockRequest blockRequest) {
        String id = securityContextService.getUserDetails().getUsername();
        return blockService.block(blockRequest, id);
    }
}
