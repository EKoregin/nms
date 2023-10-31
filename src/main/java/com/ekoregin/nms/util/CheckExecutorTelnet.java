package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.Result;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.sf.expectit.matcher.Matchers.contains;


@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Component
public class CheckExecutorTelnet implements CheckExecutor {

    private final ModelDeviceService modelDeviceService;

    @Override
    public CheckResult checkExecute(Check check, Customer customer) {
        log.info("Starting check execute with TELNET");
        Device checkDevice = getDeviceForCheck(modelDeviceService, check, customer);
        if (checkDevice == null)
            throw new RuntimeException("Check device cannot be null!");
        Map<String, String> paramsForCheck = getParamsForCheck(check, customer);
        paramsForCheck.put("LOGIN", checkDevice.getLogin());
        paramsForCheck.put("PASSWORD", checkDevice.getPassword());
        /* Получить telnetCommands в следующем формате и заменить их на значения переменных
        username:=[#LOGIN];
        password:=[#PASSWORD];
        admin#=show fdb port [#PORT]
         */
        String commandsWithValues = replacingVariablesWithValues(check.getTelnetCommands(), paramsForCheck);
        log.info("Telnet commands: {}", commandsWithValues);
        CheckResult checkResult = telnetExec(checkDevice, commandsWithValues);
        String regex = check.getSubstRules();
        if (regex != null && !regex.isEmpty()) {
            log.info("Regex: " + regex);
            findRegexInStringAndPutToResult(regex, checkResult);
        }
        return checkResult;
    }

    private CheckResult telnetExec(Device device, String commands) {
        CheckResult checkResult = new CheckResult();

        TelnetClient telnet = new TelnetClient();
        try {
            telnet.connect(device.getIp().getAddress(), device.getPort());
            StringBuilder bufferIn = new StringBuilder();
            StringBuilder bufferOut = new StringBuilder();
            try (Expect expect = new ExpectBuilder()
                    .withOutput(telnet.getOutputStream())
                    .withInputs(telnet.getInputStream())
                    .withEchoOutput(bufferOut)
                    .withEchoInput(bufferIn)
                    .withExceptionOnFailure()
                    .build()) {

                String[] keyValueArr = commands.replaceAll("\n", "").trim().split(";");

                int keyValueArrLength = keyValueArr.length - 1;

                for (int i = 0; i < keyValueArrLength; i++) {
                    String[] parts = keyValueArr[i].split("==");
                    log.info("Wait Response: {}", parts[0].trim());
                    Result tempResult = expect.expect(contains(parts[0].trim()));
                    log.info("Message before Response: {}", tempResult.getBefore());
                    log.info("SendLine: {}", parts[1].trim());
                    expect.sendLine(parts[1].trim());
                }
                String finishKey = keyValueArr[keyValueArrLength].split("==")[0].trim();
                String finishCommand = keyValueArr[keyValueArrLength].split("==")[1].trim();
                log.info("Finish key: {}, Finish value={}", finishKey, finishCommand);
                Result result = expect.expect(contains(finishKey));
                checkResult.setStatus(CheckResultStatus.OK);
                checkResult.setResult(result.getBefore());
                expect.sendLine(finishCommand);
                log.info(result.getBefore());
            }
        } catch (IOException e) {
            checkResult.setStatus(CheckResultStatus.ERROR);
            checkResult.setResult(e.getMessage());
        }
        return checkResult;
    }

    private void findRegexInStringAndPutToResult(String regex, CheckResult checkResult) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(checkResult.getResult());
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group(0)).append("\n");
        }
        checkResult.setResult(result.toString());
    }

}
