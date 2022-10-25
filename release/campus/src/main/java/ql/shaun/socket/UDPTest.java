package ql.shaun.socket;

import ql.shaun.elevator.utils.SocketStringUtil;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class UDPTest {

    public static void main(String[] args) throws IOException {
        int port = 8101;
        String command = "01FE00";
        String dataLength = "00000002";
        String data = "6677";
        String body = "30303030303030303030303030303030" + "FFFFFFFF" + "00000001" + command + dataLength + data;
        String checkCode = SocketStringUtil.getADD8Sum(body);
        if (checkCode.toUpperCase().equals("7F")) {
            checkCode = "7F02";
        } else if (checkCode.toUpperCase().equals("7E")) {
            checkCode = "7F01";
        }
        String sentCommand = "7E" + body + checkCode + "7E";

        byte[] dataBytes = SocketStringUtil.hexString2Bytes(sentCommand.toUpperCase());

        SocketAddress socketAddress = new InetSocketAddress("255.255.255.255", port);
        DatagramPacket packet = new DatagramPacket(dataBytes, dataBytes.length, socketAddress);

        Enumeration<NetworkInterface> nif = NetworkInterface.getNetworkInterfaces();
        while (nif.hasMoreElements()) {
            NetworkInterface networkInterface = nif.nextElement();
            Enumeration<InetAddress> nifAddresses = networkInterface.getInetAddresses();
            if (nifAddresses.hasMoreElements()) {
                DatagramSocket socket = new DatagramSocket(0, nifAddresses.nextElement());
                try {
                    socket.send(packet);
                    byte[] arr = new byte[1024];
                    DatagramPacket packet2 = new DatagramPacket(arr, arr.length);
                    socket.setSoTimeout(3000);
                    socket.receive(packet2);
                    if (packet2 != null && packet2.getLength() > 0) {
                        byte[] arr1 = packet2.getData();
                        String reply = SocketStringUtil.bytes2HexString(arr1);
                        System.out.println(reply);
                    }
                } catch (Exception ex) {

                }
                finally {
                    socket.close();
                }
            }
        }
    }
}
