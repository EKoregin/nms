package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.ModelDeviceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;
import org.springframework.stereotype.Component;

import javax.net.SocketFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Component
public class CheckExecutorMikrotik implements CheckExecutor {

    private final ModelDeviceService modelDeviceService;

    @Override
    public CheckResult checkExecute(Check check, Customer customer, Device device) {

        Device checkDevice = null;
        Map<String, String> paramsForCheck = new HashMap<>();
        String checkScope = check.getCheckScope();
        if (checkScope.equals(CheckScope.CUSTOMER.name())) {
            log.info("Starting check execute with REST API for customer");
            checkDevice = getDeviceForCheck(modelDeviceService, check, customer);
            paramsForCheck = getParamsForCheck(check, customer);
        } else if (checkScope.equals(CheckScope.DEVICE.name())) {
            log.info("Starting check execute with REST API for device");
            checkDevice = device;
        }
        if (checkDevice == null)
            throw new RuntimeException("Check device cannot be null!");

        String commandsWithValues =
                replacingVariablesWithValues(check.getTelnetCommands(), paramsForCheck, customer);

        String ipAddress = checkDevice.getIp().getAddress();
        String login = checkDevice.getLogin();
        String password = checkDevice.getPassword();
        int managePort = checkDevice.getPort();
        String commands = commandsWithValues;
        //https://github.com/GideonLeGrange/mikrotik-java/tree/master
        StringBuilder reportCommand = new StringBuilder().append("Выполненные команды").append("\n");
        try (ApiConnection connection = ApiConnection.connect(SocketFactory.getDefault(), ipAddress, managePort, 10000)) {
            connection.setTimeout(3000);
            log.info("Starting check execute: {} with Mikrotik API", check.getCheckName());
            log.info("Login to device IP: {} and port {}", ipAddress, managePort);
            connection.login(login, password);
            List<String> commandList = Arrays.stream(commands.split(";")).toList();
            for (String command : commandList) {
                log.info("Execute command \"{}\" on device {}", command, checkDevice.getName());
                execCommand(connection, command, reportCommand);
            }
        } catch (MikrotikApiException e) {
            log.warn(e.getLocalizedMessage());
        }
        CheckResult checkResult = new CheckResult();
        checkResult.setResult(reportCommand.toString());
        return checkResult;
    }

    private synchronized void execCommand(ApiConnection connection,  String command, StringBuilder reportCommand) {
        try {
            if (command.trim().matches("/ip/address/remove \\.id=\\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\\b")) {
                reportCommand.append(getIdAnThenRemoveByNetworkIp(connection, command.split("=")[1]));
            } else {
                connection.execute(command);
                reportCommand.append(command);
            }
        } catch (MikrotikApiException e) {
            log.warn(e.getLocalizedMessage());
        }
    }

    private String getIdAnThenRemoveByNetworkIp(ApiConnection connection, String ip) throws MikrotikApiException {
        log.info("Remove IP for network: {}", ip);
        StringBuilder doneCommand = new StringBuilder();
        List<Map<String, String>> resultMap = connection.execute("/ip/address/print where network=" + ip);
        List<String> idIpList = resultMap.stream().map(result -> result.get(".id")).toList();
        for (String idIp : idIpList) {
            String command = "/ip/address/remove .id=" + idIp;
            log.info("Remove IP command: " + command);
            connection.execute(command);
            doneCommand.append("\n").append(command);
        }
        return doneCommand.toString();
    }
}
