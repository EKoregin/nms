package com.ekoregin.nms.util.mikrobill;

import com.ekoregin.nms.database.entity.CheckResult;
import com.ekoregin.nms.database.entity.CheckResultStatus;
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
        if (users.isEmpty()) {
            result.setStatus(CheckResultStatus.NODATA);
        }
        result.setResult(users.stream()
                .map(Object::toString)
                .reduce("", String::concat));
        return result;
    }
}
