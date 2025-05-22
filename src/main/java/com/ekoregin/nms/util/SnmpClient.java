package com.ekoregin.nms.util;

import com.ekoregin.nms.database.entity.CheckResult;
import com.ekoregin.nms.database.entity.CheckResultStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@Component
public class SnmpClient {
    private String port;
    private String ipAddress;
    private int snmpVersion = SnmpConstants.version2c;
    private TransportMapping<UdpAddress> transport;
    private Snmp snmp;

    private void startTransport() throws IOException {
        transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    private CommunityTarget<UdpAddress> getTarget(String community) {
        CommunityTarget<UdpAddress> communityTarget = new CommunityTarget<>();
        communityTarget.setCommunity(new OctetString(community));
        communityTarget.setVersion(snmpVersion);
        communityTarget.setAddress(new UdpAddress(ipAddress + "/" + port));
        communityTarget.setRetries(2);
        communityTarget.setTimeout(1000);
        return communityTarget;
    }

    private void stopTransport() throws IOException {
        try {
            if (transport != null) {
                transport.close();
                transport = null;
            }
        } finally {
            if (snmp != null) {
                snmp.close();
                snmp = null;
            }
        }
    }

    public CheckResult snmpGet(String community, String snmpOID) {
        CheckResult checkResult = new CheckResult();
        checkResult.setStatus(CheckResultStatus.ERROR);
        try {
            try {
                startTransport();
                CommunityTarget<UdpAddress> communityTarget = getTarget(community);

                PDU pdu = new PDU();
                pdu.add(new VariableBinding(new OID(snmpOID)));
                pdu.setType(PDU.GET);
                pdu.setRequestID(new Integer32(1));

                log.info("Sending Request to Agent {}:{} ...", ipAddress, port);
                ResponseEvent<UdpAddress> response;
                response = snmp.get(pdu, communityTarget);

                if (response != null) {
                    log.info("Got Response from Agent {}:{}", ipAddress, port);
                    PDU responsePDU = response.getResponse();

                    if (responsePDU != null) {
                        int errorStatus = responsePDU.getErrorStatus();
                        int errorIndex = responsePDU.getErrorIndex();
                        String errorStatusText = responsePDU.getErrorStatusText();

                        if (errorStatus == PDU.noError) {
                            log.info("Snmp Get Response = " + responsePDU.getVariableBindings());
                            String snmpResult = responsePDU.getVariableBindings().stream()
                                    .map(bind -> bind.getVariable().toString())
                                    .collect(Collectors.joining(","));
                            checkResult.setResult(snmpResult);
                            if (!snmpResult.equals("noSuchObject"))
                                checkResult.setStatus(CheckResultStatus.OK);
                        } else {
                            log.error("Error: Request Failed");
                            log.error("Error Status = " + errorStatus);
                            log.error("Error Index = " + errorIndex);
                            log.error("Error Status Text = " + errorStatusText);
                            checkResult.setResult("Error: Request Failed");
                        }
                    } else {
                        log.error("Error: Response PDU is null");
                        checkResult.setResult("Error: Response PDU is null");
                    }
                } else {
                    log.error("Error: Agent Timeout... {}:{}", ipAddress, port);
                    checkResult.setResult("Error: Agent Timeout... " + ipAddress + ":" + port + ":{}");
                }
            } finally {
                stopTransport();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return checkResult;
    }
}
