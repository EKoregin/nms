package com.ekoregin.nms.util.mikrobill;

import com.ekoregin.nms.entity.CheckResult;
import com.ekoregin.nms.entity.CheckResultStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MikrobillUserService {

    private final MikrobillUserRepository mikrobillUserRepository;

    public CheckResult findUserInfoByName(String userName) {
        var users =  mikrobillUserRepository.findUserByName(userName);
        CheckResult result = new CheckResult();
        result.setStatus(CheckResultStatus.OK);
        result.setResult(users.stream()
                .map(Object::toString)
                .reduce("", String::concat));

        return result;
    }
}
