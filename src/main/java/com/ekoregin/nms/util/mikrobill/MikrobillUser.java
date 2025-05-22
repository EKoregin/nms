package com.ekoregin.nms.util.mikrobill;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MikrobillUser {

    private String name;
    private String ip;
    private String vlan;
    private String mac;
    private String device;
    private String tariff;
    private String balance;
    private String status;
    private String contract;
    private String stopdate;
    private String isArchive;
    private String todaytraffic;

    @Override
    public String toString() {
        return "Логин: " + name + "\n" +
               "IP: " + ip + "\n" +
               "Vlan: " + vlan + "\n" +
               "Device: " + device + "\n" +
               "MAC: " + mac + "\n" +
               "Тариф: " + tariff + "\n" +
               "Баланс: " + balance + "\n" +
               "Статус: " + status + "\n" +
               "Договор: " + contract + "\n" +
               "Stopdate: " + stopdate + "\n" +
               "Архив: " + isArchive + "\n" +
               "Трафик(Mb): " + todaytraffic + "\r\n";
    }
}
