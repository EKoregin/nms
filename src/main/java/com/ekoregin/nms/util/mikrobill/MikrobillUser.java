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
        return name +
               ", IP=" + ip +
               ", Vlan=" + vlan +
               ", Device=" + device +
               ", MAC=" + mac +
               ", Тариф=" + tariff +
               ", Баланс=" + balance +
               ", Статус=" + status +
               ", Договор=" + contract +
               ", Stopdate=" + stopdate +
               ", Архив=" + isArchive +
               ", Трафик(Mb)=" + todaytraffic + "\r\n";
    }
}
