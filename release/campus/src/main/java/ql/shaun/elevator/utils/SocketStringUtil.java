package ql.shaun.elevator.utils;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SocketStringUtil {

    public static String number2HexByEveryTwoBit(Integer numberStr) {
        return String.format("%010x", numberStr);
    }

    @SneakyThrows
    public static String strDate2BCDStr(String strDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        Date date = simpleDateFormat.parse(strDate);
        SimpleDateFormat realDate = new SimpleDateFormat("yyMMddHHmm");
        return realDate.format(date);
    }

    @SneakyThrows
    public static String BCDDate2Str(String strDate) {
        SimpleDateFormat realDate = new SimpleDateFormat("yyMMddHHmmss");
        Date date = realDate.parse(strDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sbu.append(Integer.toHexString(chars[i] & 0xFF));
        }
        return sbu.toString();
    }

    public static String asciiToString(String data) {
        int len = data.length();
        int num = 0;
        StringBuilder str = new StringBuilder();
        while (num < len) {
            String s = data.substring(num, num + 2);
            int ac = Integer.parseInt(s, 16);
            char c = (char) ac;
            str.append(c);
            num = num + 2;
        }
        return str.toString();
    }

    public static String getADD8Sum(String data) {
        if (data == null || data.equals("")) {
            return "00";
        }
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }

        String hex = Integer.toHexString(total);
        len = hex.length();
        if (len >= 2) {
            hex = hex.substring(len - 2, len);
        } else {
            hex = "0" + hex;
        }
        return hex;
    }

    public static String intToHexStr(String data) {
        Integer hex = Integer.parseInt(data, 16);
        String hexStr = Integer.toHexString(hex);
        if (hexStr.length() == 1) {
            hexStr = "0" + hexStr;
        }
        return hexStr;
    }

    public static String intToHex(Integer num) {
        String hexStr = Integer.toHexString(num);
        return hexStr;
    }

    public static Integer hex2Int(String hex) {
        return Integer.parseInt(hex, 10);
    }

    public static Integer hex2Int10(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static Long hex2Long10(String hex) {
        return Long.parseLong(hex, 16);
    }
}
