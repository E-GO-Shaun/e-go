package ql.shaun.elevator.entity;

import lombok.Data;
import ql.shaun.elevator.utils.SocketStringUtil;

@Data
public class IpInfo {
    // 字节6 -去符号16进制
    private String macAddress;

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress.replaceAll("-", "").replaceAll(":", "");
    }

    // 字节4 -去符号16进制
    private String ipAddress;

    public void setIpAddress(String ipAddress) {
        this.ipAddress = dataFormat(ipAddress);
    }

    // 字节4 -去符号16进制
    private String subnetMask;

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = dataFormat(subnetMask);
    }

    // 字节4 -去符号16进制
    private String gatewayIp;

    public void setGatewayIp(String gatewayIp) {
        this.gatewayIp = dataFormat(gatewayIp);
    }

    // 字节4 -去符号16进制
    private String dns;

    public void setDns(String dns) {
        this.dns = dataFormat(dns);
    }

    // 字节4 -去符号16进制
    private String backupDns;

    public void setBackupDns(String backupDns) {
        this.backupDns = dataFormat(backupDns);
    }

    // 1.tcp client 2.tcp server 3.mixed
    // 字节1
    private String tcpMode;
    // 字节2
    private String localTcpPort;

    public void setLocalTcpPort(String localTcpPort) {
        this.localTcpPort = SocketStringUtil.intToHexStr(localTcpPort);
    }

    // 字节2
    private String localUdpPort;

    public void setLocalUdpPort(String localUdpPort) {
        this.localUdpPort = SocketStringUtil.intToHexStr(localUdpPort);
    }

    // 字节2
    private String targetPort;

    public void setTargetPort(String targetPort) {
        this.targetPort = SocketStringUtil.intToHexStr(targetPort);
    }

    // 字节4
    private String targetIp;

    public void setTargetIp(String targetIp) {
        this.targetIp = dataFormat(targetIp);
    }

    // 字节100
    private String targetDomain;

    public void setTargetDomain(String targetDomain) {
        String ascii = SocketStringUtil.stringToAscii(targetDomain);
        this.targetDomain = String.format("%0100d", ascii);
    }

    private String dataFormat(String ipAddress) {
        String[] ipArray = ipAddress.split(".");
        String result = "";
        for (int i = 0; i < ipArray.length; i++) {
            result += SocketStringUtil.intToHexStr(ipArray[i]);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder ipInfo = new StringBuilder();
        ipInfo.append(macAddress);
        ipInfo.append(ipAddress);
        ipInfo.append(subnetMask);
        ipInfo.append(gatewayIp);
        ipInfo.append(dns);
        ipInfo.append(backupDns);
        ipInfo.append(tcpMode);
        ipInfo.append(localTcpPort);
        ipInfo.append(localUdpPort);
        ipInfo.append(targetPort);
        ipInfo.append(targetIp);
        ipInfo.append(targetDomain);
        return ipInfo.toString();
    }
}
