package com.example.datinguserapispring.service.block;

import com.example.datinguserapispring.dto.block.request.BlockRequest;
import com.example.datinguserapispring.dto.block.response.BlockResponse;

public interface BlockService {
    BlockResponse block(BlockRequest blockRequest, String id);
}
