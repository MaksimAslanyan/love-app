package com.example.datinguserapispring.service.lookingFor.impl;

import com.example.datinguserapispring.model.entity.dictionary.LookingFor;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.LookingForRepository;
import com.example.datinguserapispring.service.lookingFor.LookingForService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LookingForServiceImpl implements LookingForService {


    private final LookingForRepository lookingForRepository;

    @Override
    public List<String> getUserLookingFor(String id) {
        List<LookingFor> userLookingForList = lookingForRepository.findAllByUserId(id);
        return getLookingForListStr(userLookingForList);
    }

    @Override
    public List<String> getUserLookingFor(User user) {
        return getLookingForListStr(user.getLookingFor());
    }

    @Override
    public List<String> getBotLookingFor(String id){
        List<LookingFor> botLookingForList = lookingForRepository.findAllByBot2Id(id);
        return getLookingForListStr(botLookingForList);
    }

    private  List<String> getLookingForListStr(List<LookingFor> lookingForList) {
        List<String> lookingForListStr = new ArrayList<>();
        for (LookingFor userTarget : lookingForList) {
            String lookingFor = userTarget.getLookingFor();

            if (lookingFor == null) {
                lookingForListStr.addAll( getRandomLookingForList());
            }

            if(lookingFor != null){
                lookingFor = userTarget.getLookingFor();
            }

            lookingForListStr.add(lookingFor);
        }
        return lookingForListStr;
    }

    private List<String> getRandomLookingForList() {
        String[] options = {
                "No Strings Attached",
                "Friends With Benefits",
                "Dating",
                "Sexting",
                "One Night Stand"
        };

        List<String> lookingForList = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i < 4; i++) {
            lookingForList.add(options[random.nextInt(options.length)]);
        }

        return lookingForList;
    }
}
