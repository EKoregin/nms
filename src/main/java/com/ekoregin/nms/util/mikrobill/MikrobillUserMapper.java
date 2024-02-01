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
        user.setMac(userInfoMap.get("mac"));
        user.setDevice(userInfoMap.get("device"));
        user.setTariff(userInfoMap.get("tariff"));
        user.setBalance(userInfoMap.get("balance"));
        user.setStatus(userInfoMap.get("status"));
        user.setContract(userInfoMap.get("contract"));
        user.setStopdate(userInfoMap.get("stopdate"));
        user.setIsArchive(userInfoMap.get("isarchived"));
        user.setTodaytraffic(userInfoMap.get("todaytraffic"));
        return user;
    }

    private static Map<String, String> parseUserInfo(ResultSet resultSet) throws SQLException {
        String otherInfo = resultSet.getString("otherinfo");
        log.info("RawMikrobillOtherInfo: " + otherInfo);
        Map<String, String> userInfoMap = new HashMap<>();
        List<String> result = Arrays.asList(otherInfo.split("\\|"));

        String vlan = result.stream()
                .filter(str -> str.startsWith("vlan_"))
                .findFirst().orElse("");
        userInfoMap.put("name", resultSet.getString("user_name"));
        userInfoMap.put("ip", resultSet.getString("usrip"));
        userInfoMap.put("vlan", vlan.replace("vlan_", ""));
        userInfoMap.put("mac", result.get(55));
        userInfoMap.put("device", result.get(18));
        userInfoMap.put("tariff", resultSet.getString("tarif"));
        userInfoMap.put("balance", resultSet.getString("ballance"));
        userInfoMap.put("status", resultSet.getString("state"));
        userInfoMap.put("contract", resultSet.getString("contract"));
        userInfoMap.put("stopdate", resultSet.getString("stopdate"));
        userInfoMap.put("isarchived", resultSet.getString("isarchived").equals("1") ? "Да" : "Нет");
        String rawTraffic = resultSet.getString("todaytraffic");
        String[] traffic = rawTraffic.split("/");
        String inputTraffic = String.format("%.2f", Long.parseLong(traffic[0].trim()) / 1000000.0);
        String outputTraffic = String.format("%.2f", Long.parseLong(traffic[1].trim()) / 1000000.0);
        userInfoMap.put("todaytraffic", inputTraffic + "/" + outputTraffic);
        return userInfoMap;
    }
}
