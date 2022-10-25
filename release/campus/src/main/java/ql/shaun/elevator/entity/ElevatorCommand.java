package ql.shaun.elevator.entity;

import lombok.Data;
import ql.shaun.elevator.utils.SocketStringUtil;

@Data
public class ElevatorCommand {
    private String start = "7E";

    private String sn;

    public void setSn(String sn) {
        this.sn = SocketStringUtil.stringToAscii(sn);
    }

    private String password;
    private String msgId;
    private String cmd;
    private String dataLength;
    private String data = "";

    public void setData(String data) {
        data = data.toUpperCase();
        String newData = "";
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            if (s.equals("7E")) {
                s = "7F01";
            } else if (s.equals("7F")) {
                s = "7F02";
            }
            newData += s;
            num = num + 2;
        }
        this.data = newData;
    }

    private String checkCode;
    private String end = "7E";
}
