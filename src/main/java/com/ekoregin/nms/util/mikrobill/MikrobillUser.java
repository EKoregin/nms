package com.ekoregin.nms.util.mikrobill;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MikrobillUser {

    private String name;
    private String ip;
    private String vlan;
    private String device;

    @Override
    public String toString() {
        return name +
               ", ip=" + ip +
               ", vlan=" + vlan +
               ", device=" + device + "\r\n";
    }
}
