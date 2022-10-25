package org.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import ql.shaun.elevator.ElevatorProcess;
import ql.shaun.elevator.entity.CardParam;
import ql.shaun.elevator.entity.ElevatorParameters;
import ql.shaun.elevator.entity.ReceivedMessage;
import ql.shaun.elevator.enums.CMDEnum;

import java.io.IOException;
import java.util.*;

/**
 * Hello world!
 */
public class App {
    private static List<CardParam> cardList = new ArrayList<>();
    private static int serialNumber = 1;

    public static void main(String[] args) throws IOException {

        // String config="{\"deviceConfig\": {\"1497119233056927746\": \"2\", \"1501077835278893058\": \"1\", \"1504643558022479874\": \"1\", \"1509049226854854657\": \"2\"}, \"elevatorConfig\": {\"1467685798063517697\": [\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\"], \"1467728413672800257\": [\"9\", \"10\", \"11\", \"13\", \"14\", \"15\", \"16\", \"12\"]}}";
        // JSONObject jsonResult = JSONObject.parseObject(config);
        // JSONObject deviceObject= jsonResult.getJSONObject("deviceConfig");
        // deviceObject.keySet().forEach(key->{
        //     Object invokeValue=  deviceObject.get(key);
        //     int result=Integer.parseInt(invokeValue.toString());
        // });
        //
        // JSONObject elevatorObject = jsonResult.getJSONObject("elevatorConfig");
        // elevatorObject.keySet().forEach(key->{
        //     JSONArray invokeValue=  elevatorObject.getJSONArray(key);
        //     String result= StringUtils.join(invokeValue,",");
        //     String aa="";
        // });

        elevatorTest();
    }

    private static void elevatorTest() {
        System.out.println("Begin World!");
        ElevatorParameters elevatorParameters = new ElevatorParameters();
        elevatorParameters.setPassword("FFFFFFFF");
        elevatorParameters.setIp("192.168.99.201");
        elevatorParameters.setPort(8000);
        elevatorParameters.setSnNum("MC-5816A00000005");
        final ElevatorProcess elevatorProcess = new ElevatorProcess(elevatorParameters) {
            @Override
            protected void onReceived(ReceivedMessage receivedMessage) {
//                this.Close();
                if (!receivedMessage.getMsgId().equals("11111111")) {
                    return;
                }
                if (cardList.size() == 0) {
                    // 终止写卡
                    Map<String, Object> data = new HashMap<>();
                    data.put("msgId", "12345678");
                    sendMessage(CMDEnum.END_WRITE_AUTH_CARD, data);
                } else {
                    Map<String, Object> data = new HashMap<>();
                    // 开始写卡
                    String json = "";
                    if (cardList.size() >= 3) {
                        json = JSONObject.toJSONString(cardList.subList(0, 3));
                    } else {
                        json = JSONObject.toJSONString(cardList.subList(0, cardList.size()));
                    }
                    data.put("serialNumber", serialNumber);
                    data.put("data", json);
                    data.put("msgId", "11111111");
                    sendMessage(CMDEnum.WRITE_AUTH_CARD, data);
                    serialNumber += 1;
                    cardList.removeAll(cardList.subList(0, 3));
                }
            }
        };

        Map<String, Object> data = new HashMap<>();
        data.put("msgId", "12345678");
        //读取tcp信息
//        elevatorProcess.sendMessage(CMDEnum.READ_TCP_PARAM, data);
        // 读取SN号
//        elevatorProcess.sendMessage(CMDEnum.READ_SN, data);
        //获取版本号
//        elevatorProcess.sendMessage(CMDEnum.GET_VERSION, data);
        //设备运行信息
        // elevatorProcess.sendMessage(CMDEnum.DEVICE_RUNTIME_INFO, data);
        // 写入时间
        // Calendar calendar = Calendar.getInstance();
        // String second = String.format("%2d", calendar.get(Calendar.SECOND)).replace(" ", "0");
        // String minute = String.format("%2d", calendar.get(Calendar.MINUTE)).replace(" ", "0");
        // String hour = String.format("%2d", calendar.get(Calendar.HOUR_OF_DAY)).replace(" ", "0");
        // String day = String.format("%2d", calendar.get(Calendar.DAY_OF_MONTH)).replace(" ", "0");
        // String month = String.format("%2d", calendar.get(Calendar.MONTH) + 1).replace(" ", "0");
        // String week = String.format("%2d", calendar.get(Calendar.DAY_OF_MONTH)).replace(" ", "0");
        // String year = String.format("%2d", calendar.get(Calendar.YEAR)).replace(" ", "0").substring(2, 4);
        // StringBuilder sysDate = new StringBuilder();
        // sysDate.append(second);
        // sysDate.append(minute);
        // sysDate.append(hour);
        // sysDate.append(day);
        // sysDate.append(month);
        // sysDate.append(week);
        // sysDate.append(year);
        // String resultTime = sysDate.toString();
        // data.put("data", resultTime);
        // elevatorProcess.sendMessage(CMDEnum.WRITE_TIME, data);
        // 读取系统时间
        // elevatorProcess.sendMessage(CMDEnum.READ_TIME, data);
        // 开门
        // String[] doorArray = {"1", "2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
        // String[] doorArray = {"1", "2", "3"};
        // data.put("data", StringUtils.join(doorArray, ","));
        // elevatorProcess.sendMessage(CMDEnum.OPEN_DOOR, data);
        // 关门
//        String[] doorArray = {"1"};
//        data.put("data", StringUtils.join(doorArray, ","));
//        elevatorProcess.sendMessage(CMDEnum.CLOSEC_DOOR, data);
        // 设置门常开
//        String[] doorArray = {"1"};
//        data.put("data", StringUtils.join(doorArray, ","));
//        elevatorProcess.sendMessage(CMDEnum.SET_ALWAYS_OPEN, data);
        // 读取授权卡信息
       // elevatorProcess.sendMessage(CMDEnum.READ_AUTH_CARD_LIST, data);

        // 设置读卡器位数
        // 1三字节-八位 2四字节-十位 3二字节-四位 4禁用
        // data.put("data", 2);
        // elevatorProcess.sendMessage(CMDEnum.SET_READ_BIT, data);

        // 读取所有卡  1排序卡区域 2非排序卡区域 3所有区域
//        data.put("data",1);
//        elevatorProcess.sendMessage(CMDEnum.READ_ALL_CARD, data);
        // 清空所有授权卡  1排序卡区域 2非排序卡区域 3所有区域
//        data.put("data","2");
//        elevatorProcess.sendMessage(CMDEnum.EMPTY_ALL_CARD,data);

        // 添加授权卡至非排序卡区域
        // CardParam cardParam=new CardParam();
        // cardParam.setCardNum("0213661050");
        // cardParam.setPassword("12345678");
        // cardParam.setValidDate("2023-03-21 00:00:00");
        // cardParam.setValidTimes(65535); //设置为65535表示不受限制
        // // String[] doorArray = {"1", "2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
        // // String[] doorArray = {"1"};
        //  String[] doorArray = {"1", "2","3","4","5","6","7","8"};
        // cardParam.setFloorNum(StringUtils.join(doorArray, ","));
        // cardParam.setAuthority("0");
        // cardParam.setStatus("0");
        // String json=JSONObject.toJSONString(cardParam);
        // data.put("data",json);
        // elevatorProcess.sendMessage(CMDEnum.ADD_AUTH_CARD,data);


        // 读取单个授权卡，十位卡号0004233195
        // data.put("data", "4233195");
        // elevatorProcess.sendMessage(CMDEnum.READ_UNIQUE_CARD, data);

        // 删除授权卡
//        String[] cardArray = {"23331113"};
//        data.put("data",StringUtils.join(cardArray,","));
//        elevatorProcess.sendMessage(CMDEnum.DELETE_AUTH_CARD,data);

        // 批量写卡，授权到排序区域
//        String[] cardArray = {"03085319"};
// //        String[] cardArray = {"1"};
//        List<CardParam> cardList = new ArrayList<>();
//        for (int i = 0; i < cardArray.length; i++) {
//            CardParam cardParam = new CardParam();
//            cardParam.setCardNum(cardArray[i]);
//            cardParam.setPassword("12345678");
//            cardParam.setValidDate("2023-03-21 00:00:00");
//            cardParam.setValidTimes(65535); //设置为65535表示不受限制
//            String[] doorArray = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
//            cardParam.setFloorNum(StringUtils.join(doorArray, ","));
//            cardParam.setAuthority("0");
//            cardParam.setStatus("0");
//            cardList.add(cardParam);
//        }
//        elevatorProcess.writeCards("1", "11223345", cardList);

        // 按批次写卡测试
//        List<String> idNums = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            idNums.add(String.valueOf(i + 1));
//        }
//        cardList.clear();
//        for (int i = 0; i < idNums.size(); i++) {
//            CardParam cardParam = new CardParam();
//            cardParam.setCardNum(idNums.get(i));
//            cardParam.setPassword("12345678");
//            cardParam.setValidDate("2023-03-21 00:00:00");
//            cardParam.setValidTimes(65535); //设置为65535表示不受限制
//            String[] doorArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
//            cardParam.setFloorNum(StringUtils.join(doorArray, ","));
//            cardParam.setAuthority("0");
//            cardParam.setStatus("0");
//            cardList.add(cardParam);
//        }
//
//        data.put("msgId", "12345678");
//        elevatorProcess.sendMessage(CMDEnum.BEGIN_WRITE_AUTH_CARD, data);
//        // 开始写卡
//        String json = JSONObject.toJSONString(cardList.subList(0, 3));
//        data.put("serialNumber", serialNumber);
//        data.put("data", json);
//        data.put("msgId", "11111111");
//        elevatorProcess.sendMessage(CMDEnum.WRITE_AUTH_CARD, data);
//        serialNumber += 2;
//        cardList.removeAll(cardList.subList(0, 3));

        // 开启实时监控
//        elevatorProcess.sendMessage(CMDEnum.START_MONITOR,data);
        // 关闭实时监控
//        elevatorProcess.sendMessage(CMDEnum.STOP_MONITOR,data);
        // 查看实时监控状态
//        elevatorProcess.sendMessage(CMDEnum.MONITOR_STATUS,data);
    }
}
