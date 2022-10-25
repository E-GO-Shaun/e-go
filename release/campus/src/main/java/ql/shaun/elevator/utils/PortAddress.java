package ql.shaun.elevator.utils;

import java.util.ArrayList;
import java.util.List;

public class PortAddress {

    private String[][] ports = {{"0000", "0000"}, {"0000", "0000"}, {"0000", "0000"}, {"0000", "0000"},
            {"0000", "0000"}, {"0000", "0000"}, {"0000", "0000"}, {"0000", "0000"}, {"0000", "0000"}};

    public PortAddress(String[] portList) {
        if (portList == null) return;
        for (int i = 0; i < portList.length; i++) {
            int floorNumber = Integer.parseInt(portList[i]);
            int remainder = floorNumber % 8;
            // 判断属于高位还是低位
            int position = (remainder >= 5 || remainder == 0) ? 0 : 1;
            // 判断在当前四位的第几位
            int bit = 0;
            int pt = remainder % 4;
            if (pt == 0 || pt == 2) {
                bit = pt;
            } else {
                bit = (pt == 1 ? 3 : 1);
            }
            // 计算是最终数组的第几位
            int index = floorNumber / 8;
            if (floorNumber % 8 > 0) index++;
            index--;
            String nowPort = ports[index][position];
            // 修改当前位后，重新赋值
            StringBuilder sb = new StringBuilder(nowPort);
            sb.replace(bit, bit + 1, "1");
            ports[index][position] = sb.toString();
        }
    }

    public List<String> getFloorByCommand(String data) {
        List<String> floors = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            if (i * 2 + 2 > data.length()) break;
            String s = data.substring(i * 2, i * 2 + 2);
            Integer number = SocketStringUtil.hex2Int10(s);
            String bitNumber = Integer.toBinaryString(number);
            if (bitNumber.length() < 8) {
                bitNumber = String.format("%08d", Integer.parseInt(bitNumber));
            }
            for (int j = 0; j < bitNumber.length(); j++) {
                String result = bitNumber.substring(j, j + 1);
                if (result.equals("1")) {
                    floors.add(String.valueOf(j + 8 * i + 1));
                }
            }
        }
        return floors;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < ports.length; i++) {
            String highNumber = ports[i][0];
            String lowNumber = ports[i][1];
            String highHex = Integer.toHexString(Integer.valueOf(highNumber, 2).intValue());
            String lowHex = Integer.toHexString(Integer.valueOf(lowNumber, 2).intValue());
            result.append(highHex);
            result.append(lowHex);
        }
        return result.toString();
    }

}
