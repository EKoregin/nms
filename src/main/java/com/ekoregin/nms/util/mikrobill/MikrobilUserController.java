package com.ekoregin.nms.util.mikrobill;

import com.ekoregin.nms.entity.CheckResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mikrobilluser")
@RequiredArgsConstructor
public class MikrobilUserController {

    private final MikrobillUserService mikrobillUserService;

    @GetMapping("{userName}")
    public CheckResult findUserByName(@PathVariable("userName") String userName) {
        return mikrobillUserService.findUserInfoByName(userName);
    }
}
