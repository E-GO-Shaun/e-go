package ql.shaun.elevator;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import ql.shaun.elevator.entity.CardParam;
import ql.shaun.elevator.entity.ReadRecordMessage;
import ql.shaun.elevator.entity.ReceivedMessage;
import ql.shaun.elevator.utils.SocketStringUtil;

import java.util.ArrayList;
import java.util.List;

public class ReceivingCmdProcess {
    private String reply;

    public ReceivingCmdProcess(String reply) {
        reply = reply.toUpperCase().replaceAll("7F01", "7E").replaceAll("7F02", "7F");
        this.reply = reply;
    }

    public ReceivedMessage setReceivedMessage() {
        ReceivedMessage receivedMessage = new ReceivedMessage();
        String msgId = this.reply.substring(2, 10);
        String dataCommand = this.reply.substring(50, 56);
        String dataLengthStr = this.reply.substring(56, 64);
        Integer dataLength = SocketStringUtil.hex2Int10(dataLengthStr);
        String dataReceived = this.reply.substring(64, 64 + dataLength * 2);
        receivedMessage.setResult(true);
        switch (dataCommand) {
            case "210100":
                receivedMessage.setRType(1);
                receivedMessage.setMessage("应答OK");
                break;
            case "210200":
                receivedMessage.setResult(false);
                receivedMessage.setRType(2);
                receivedMessage.setMessage("密码错误");
                break;
            case "210300":
                receivedMessage.setResult(false);
                receivedMessage.setRType(3);
                receivedMessage.setMessage("校验码错误");
                break;
            case "510200":   // 获取SN号
                receivedMessage.setRType(4);
                String snNumber = SocketStringUtil.asciiToString(dataReceived);
                receivedMessage.setMessage(snNumber);
                break;
            case "510600":   // 读取网络信息
                receivedMessage.setRType(5);
                receivedMessage.setMessage("IP信息读取，暂不处理：" + dataReceived);
                break;
            case "510800":  //获取版本号
                receivedMessage.setRType(6);
                String version = SocketStringUtil.asciiToString(dataReceived);
                StringBuffer sb = new StringBuffer();
                sb.append(version).replace(2, 2, ".");
                receivedMessage.setMessage(sb.toString());
                break;
            case "510900":  //获取设备运行信息
                receivedMessage.setRType(7);
                receivedMessage.setMessage("设备运行信息，暂不处理：" + dataReceived);
                break;
            case "510B02":  //获取设备运行信息
                String status = dataReceived.substring(0, 2);
                int status_result = SocketStringUtil.hex2Int10(status);
                if (status_result == 0) {
                    receivedMessage.setRType(15);
                    receivedMessage.setMessage("设备未开启：" + dataReceived);
                } else {
                    receivedMessage.setRType(16);
                    receivedMessage.setMessage("设备已开启：" + dataReceived);
                }
                break;
            case "190100":  //获取设备读卡
                receivedMessage.setRType(8);
                String cardAsc = dataReceived.substring(0, 10);
                String time = dataReceived.substring(10, 22);
                String device_num = dataReceived.substring(22, 24);
                String state = dataReceived.substring(24, 26);
                ReadRecordMessage readRecordMessage = new ReadRecordMessage();
                readRecordMessage.setReadNum(device_num);
                readRecordMessage.setCardNum(SocketStringUtil.hex2Long10(cardAsc).toString());
                readRecordMessage.setRecordTime(SocketStringUtil.BCDDate2Str(time));
                readRecordMessage.setRecordStatus(SocketStringUtil.hex2Int10(state).toString());
                receivedMessage.setMessage("获取设备读卡，获取数据：" + dataReceived);
                receivedMessage.setReadRecordMessage(readRecordMessage);
                break;
            case "5707FF":  //批量写卡到排序区域
                receivedMessage.setRType(9);
                receivedMessage.setMessage("批量写卡到排序区域失败，暂不处理：" + dataReceived);
                break;
            case "570100":  // 读取授权卡信息
                receivedMessage.setRType(10);
                String orderedNum = dataReceived.substring(0, 8);
                String orderedUsedNum = dataReceived.substring(8, 16);
                String unOrderedNum = dataReceived.substring(16, 24);
                String unOrderedUsedNum = dataReceived.substring(24, 32);
                List<String> info = new ArrayList<>();
                info.add(SocketStringUtil.hex2Int10(orderedNum).toString());
                info.add(SocketStringUtil.hex2Int10(orderedUsedNum).toString());
                info.add(SocketStringUtil.hex2Int10(unOrderedNum).toString());
                info.add(SocketStringUtil.hex2Int10(unOrderedUsedNum).toString());
                receivedMessage.setMessage(StringUtils.join(info, ","));
                break;
            case "570300":     // 读取所有授权卡
                receivedMessage.setRType(11);
                List<CardParam> cardParams = CardParam.getList(dataReceived);
                receivedMessage.setMessage(JSONObject.toJSONString(cardParams));
                break;
            case "5703FF":     // 读取所有授权卡传输结束
                receivedMessage.setRType(12);
                receivedMessage.setMessage("读取所有授权卡传输结束，暂不处理：" + dataReceived);
                break;
            case "570301":     // 读取单张授权卡
                if (dataReceived.substring(0, 10).equals("FFFFFFFFFF")) {
                    // 读取卡无结果
                    receivedMessage.setRType(14);
                    receivedMessage.setMessage("no cards found!");
                } else {
                    // 读取卡有结果
                    receivedMessage.setRType(13);
                    CardParam cardParam = CardParam.getBean(dataReceived);
                    receivedMessage.setMessage(JSONObject.toJSONString(cardParam));
                }
                break;
            case "520100": //读取时间，时间格式 ssmmHHddMMWWyy 秒分时日月周年
                receivedMessage.setRType(15);
                String second="00";
                String minute=dataReceived.substring(2, 4);
                String hour=dataReceived.substring(4, 6);
                String day=dataReceived.substring(6, 8);
                String month=dataReceived.substring(8, 10);
                String week=dataReceived.substring(10, 12);
                String year=dataReceived.substring(12, 14);
                String sysTime="20"+year+"-"+month+"-"+day+" "+hour+":"+ minute+":"+second;
                receivedMessage.setMessage(sysTime);
            case "5704FF":  //批量写卡到排序区域
                receivedMessage.setRType(16);
                receivedMessage.setMessage("写入卡失败，请检查容量" + dataReceived);
                break;
        }
        receivedMessage.setMsgId(msgId);
        receivedMessage.setCodeOri(this.reply);
        return receivedMessage;
    }
}
