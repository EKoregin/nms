package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
import org.apache.commons.net.telnet.WindowSizeOptionHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
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
    public CheckResult checkExecute(Check check, Customer customer, Device device) {
        Device checkDevice = null;
        Map<String, String> paramsForCheck = new HashMap<>();
        if (check.getCheckScope().equals(CheckScope.CUSTOMER.name())) {
            log.info("Starting check execute with TELNET for customer");
            checkDevice = getDeviceForCheck(modelDeviceService, check, customer);
            paramsForCheck = getParamsForCheck(check, customer);
        } else if (check.getCheckScope().equals(CheckScope.DEVICE.name())) {
            log.info("Starting check execute with TELNET for device");
            checkDevice = device;
        }
        if (checkDevice == null)
            throw new RuntimeException("Check device cannot be null!");

        paramsForCheck.put("LOGIN", checkDevice.getLogin());
        paramsForCheck.put("PASSWORD", checkDevice.getPassword());
        /* Получить telnetCommands в следующем формате и заменить их на значения переменных
        username:=[#LOGIN];
        password:=[#PASSWORD];
        admin#=show fdb port [#PORT]
         */
        String commandsWithValues = replacingVariablesWithValues(check.getTelnetCommands(), paramsForCheck, customer);
        commandsWithValues = UtilService.convertStringWithExpressions(commandsWithValues);
        log.info("Telnet commands: {}", commandsWithValues);
        CheckResult checkResult = telnetExec(checkDevice, commandsWithValues);
        String regex = check.getRegexFilter();
        if (regex != null && !regex.isEmpty()) {
            log.info("Regex: " + regex);
            findRegexInStringAndPutToResult(regex, checkResult);
        }
        return checkResult;
    }

    private CheckResult telnetExec(Device device, String commands) {
        CheckResult checkResult = new CheckResult();

        TelnetClient telnet = new TelnetClient();
        WindowSizeOptionHandler windowSizeOptionHandler = new WindowSizeOptionHandler(100, 200, false, false, true, false);
        TerminalTypeOptionHandler terminalTypeOptionHandler = new TerminalTypeOptionHandler("VT100", false, false, true, false);
        try {
            telnet.addOptionHandler(terminalTypeOptionHandler);
            telnet.addOptionHandler(windowSizeOptionHandler);
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

                StringBuilder sb = new StringBuilder();

                boolean isCommand = false;
                for (String s : keyValueArr) {
                    String[] parts = s.split("==");
                    String wait = parts[0].trim();
                    String command = parts[1].trim();
                    log.info("Wait: {}", wait);
                    String outString = expect.expect(contains(wait.replace("cmd", ""))).getBefore();
                    if (isCommand) {
                        sb.append(outString);
                    }
                    if (wait.startsWith("cmd")) {
                        isCommand = true;
                        log.info("Set command flag. Wait = " + wait);
                    } else {
                        isCommand = false;
                    }
                    log.info("Message before wait: {}", outString);
                    log.info("SendLine: {}", command);
                    expect.sendLine(command);
                }
                checkResult.setStatus(CheckResultStatus.OK);
                checkResult.setResult(sb.toString());
                log.info("Result string:");
                log.info(sb.toString());
            }
        } catch (IOException e) {
            checkResult.setStatus(CheckResultStatus.ERROR);
            checkResult.setResult(e.getMessage());
        } catch (InvalidTelnetOptionException e) {
            throw new RuntimeException(e);
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
        if (result.isEmpty()) {
            result.append("нет данных");
        }
        checkResult.setResult(result.toString());
    }

}
