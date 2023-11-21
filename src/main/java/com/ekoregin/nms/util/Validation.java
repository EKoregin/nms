package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.TypeTechParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Validation {

    public static void verifyParameterForValidity(TypeTechParameter type, String parameterValue) {
        String regexRule = type.getRegexRule();
        if (!regexRule.isEmpty()) {
            log.info("Regex rule: " + regexRule);
            if(!parameterValue.matches(regexRule)) {
                log.warn("TechParameter: {} with value: {} not pass verify", type.getName(), parameterValue);
                throw new IllegalArgumentException("Неверный формат параметра: " + type.getName());
            }
            log.info("TTP: {} with value: {} pass verify", type.getName(), parameterValue);
        }
    }
}
