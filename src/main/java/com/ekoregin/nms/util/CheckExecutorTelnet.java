package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.CheckResult;
import com.ekoregin.nms.entity.Customer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Component
public class CheckExecutorTelnet implements CheckExecutor {


    @Override
    public CheckResult checkExecute(Check check, Customer customer) {
        log.info("Starting check execute with TELNET");

        return null;
    }
}
