package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.CheckResult;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.entity.Device;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;
import org.springframework.stereotype.Component;

import javax.net.SocketFactory;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Component
public class CheckExecutorMikrotik implements CheckExecutor {
    @Override
    public CheckResult checkExecute(Check check, Customer customer, Device device) {

        Inet ipAddress = device.getIp();
        String login = device.getLogin();
        String password = device.getPassword();
        int managePort = device.getPort();
        String command = check.getTelnetCommands();

        try (ApiConnection connection = ApiConnection.connect(SocketFactory.getDefault(), ipAddress.getAddress(), managePort, 20000)){
            log.info("Starting check execute with Microtik API");
            log.info("Login to device IP: {}", ipAddress);
            connection.login(login, password);
            log.info("Execute command \" {} \" on device {}", command, device.getName());
            List<Map<String, String>> resultMap =  connection.execute(command);
            for (Map<String, String> result : resultMap) {
                System.out.println(result);
            }
        } catch (MikrotikApiException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
