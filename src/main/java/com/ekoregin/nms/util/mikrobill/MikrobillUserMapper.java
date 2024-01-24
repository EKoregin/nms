package com.ekoregin.nms.util.mikrobill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class MikrobillUserMapper implements RowMapper<MikrobillUser> {

    @Override
    public MikrobillUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        MikrobillUser user = new MikrobillUser();
        var userInfoMap = parseUserInfo(resultSet);
        user.setName(userInfoMap.get("name"));
        user.setIp(userInfoMap.get("ip"));
        user.setVlan(userInfoMap.get("vlan"));
        user.setDevice(userInfoMap.get("device"));
        return user;
    }

    private static Map<String, String> parseUserInfo(ResultSet resultSet) throws SQLException {
        String rawInfo = resultSet.getString("result");
        log.info("RawMikrobillUserInfo: " + rawInfo);
        Map<String, String> userInfoMap = new HashMap<>();
        List<String> ipDevicePair = Arrays.asList(rawInfo.split("/><"));
        //  Парсим первый элемент и достаем IP и название
        List<String> result = Arrays.asList(ipDevicePair.get(0).replaceAll("(\\|)\\1+", "$1").split("\\|"));
        String name = result.get(0).replace("<", "");
        String ip = result.get(2);
        //Достаем влан
        String vlan = result.stream()
                .filter(str -> str.startsWith("vlan_"))
                .findFirst().orElse("");
        // Парсим второй элемент и достаем параметры подключения
        List<String> result2 = Arrays.asList(ipDevicePair.get(1).replaceAll("(\\|)\\1+", "$1").split("\\|"));
        String srcDev = result2.get(1);
        String device = "";
        if (srcDev.matches(".*/.*"))
            device = srcDev;
        userInfoMap.put("name", name);
        userInfoMap.put("ip", ip);
        userInfoMap.put("vlan", vlan);
        userInfoMap.put("device", device);
        log.info("End Result: " + name + " : " + ip + " : " + vlan + " : " + device);
        return userInfoMap;
    }
}
