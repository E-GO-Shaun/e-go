package ql.shaun.elevator;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import ql.shaun.elevator.entity.CardParam;
import ql.shaun.elevator.entity.ElevatorCommand;
import ql.shaun.elevator.entity.IpInfo;
import ql.shaun.elevator.enums.CMDEnum;
import ql.shaun.elevator.utils.PortAddress;
import ql.shaun.elevator.utils.SocketStringUtil;

import java.util.List;
import java.util.Map;

public class SendingCmdProcess {
    private String snNumber;
    private ElevatorCommand elevatorCommand;

    public SendingCmdProcess(CMDEnum cmdEnum, Map<String, Object> data) {
        this.snNumber = data.get("sn").toString();
        this.elevatorCommand = new ElevatorCommand();
        InitParams(cmdEnum, data);
    }

    private void InitParams(CMDEnum cmdEnum, Map<String, Object> transData) {
        // 基础参数绑定
        elevatorCommand.setSn(this.snNumber);
        elevatorCommand.setMsgId(transData.get("msgId").toString());
        elevatorCommand.setPassword(transData.get("password").toString());
        // 交互参数绑定
        this.elevatorCommand.setCmd(cmdEnum.getCmd());
        this.elevatorCommand.setDataLength(cmdEnum.getLen());
        // 绑定交互数据
        Object data = transData.get("data");
        if (!cmdEnum.equals(CMDEnum.DEFAULT) && data != null) {
            if (cmdEnum.equals(CMDEnum.SET_PASSWORD)) {
                this.elevatorCommand.setData(data.toString());
            } else if (cmdEnum.equals(CMDEnum.SET_TCP_PARAM)) {
                ObjectMapper objectMapper = new ObjectMapper();
                IpInfo zfsp = objectMapper.convertValue(data, IpInfo.class);
                this.elevatorCommand.setData(zfsp.toString());
            } else if (cmdEnum.equals(CMDEnum.OPEN_DOOR) || cmdEnum.equals(CMDEnum.CLOSEC_DOOR) || cmdEnum.equals(CMDEnum.SET_ALWAYS_OPEN)) {
                // 传入楼层号集合 转化成16进制
                String[] ports = data.toString().split(",");
                PortAddress portAddress = new PortAddress(ports);
                this.elevatorCommand.setData(portAddress.toString());
            } else if (cmdEnum.equals(CMDEnum.LOCK) || cmdEnum.equals(CMDEnum.UNLOCK)) {
                String[] ports = data.toString().split(",");
                PortAddress portAddress = new PortAddress(ports);
                String lData = portAddress.toString().substring(0, 16);
                this.elevatorCommand.setData(lData);
            } else if (cmdEnum.equals(CMDEnum.EMPTY_ALL_CARD)) {
                String areaNum = data.toString();
                if (areaNum.length() == 1) {
                    areaNum = String.format("%02d", Integer.parseInt(areaNum));
                }
                this.elevatorCommand.setData(areaNum);
            } else if (cmdEnum.equals(CMDEnum.READ_ALL_CARD)) {
                String type=String.format("%02d", Integer.parseInt(data.toString()));
                this.elevatorCommand.setData(type);
            } else if (cmdEnum.equals(CMDEnum.READ_UNIQUE_CARD)) {
                // 传入10位卡号，高位补0
                String cardNum = data.toString();
                cardNum = SocketStringUtil.number2HexByEveryTwoBit(Integer.parseInt(cardNum));
                this.elevatorCommand.setData(cardNum);
            } else if (cmdEnum.equals(CMDEnum.ADD_AUTH_CARD)) {
                CardParam cardParam = JSONObject.parseObject(data.toString(), CardParam.class);
                // 长度变化
                this.elevatorCommand.setDataLength("00000069");
                this.elevatorCommand.setData("00000001"+cardParam.toString());
            } else if (cmdEnum.equals(CMDEnum.DELETE_AUTH_CARD)) {
                String[] cardArray = data.toString().split(",");
                // 设置长度，并拼接卡号
                int len = cardArray.length * 5 + 4;
                this.elevatorCommand.setDataLength(String.format("%08x", len));
                StringBuilder cards = new StringBuilder();
                // 卡片数量
                cards.append(String.format("%08x", cardArray.length));
                for (int i = 0; i < cardArray.length; i++) {
                    cards.append(SocketStringUtil.number2HexByEveryTwoBit(Integer.parseInt(cardArray[i])));
                }
                this.elevatorCommand.setData(cards.toString());
            } else if (cmdEnum.equals(CMDEnum.WRITE_AUTH_CARD)) {
                List<CardParam> cardList = JSONObject.parseArray(data.toString(), CardParam.class);
                int len = cardList.size() * 65 + 4 + 4;
                this.elevatorCommand.setDataLength(String.format("%08x", len));
                StringBuilder cards = new StringBuilder();
                // 序号
                String serialNumber = transData.get("serialNumber").toString();
                cards.append(String.format("%08x", Integer.parseInt(serialNumber)));
                // 数量
                cards.append(String.format("%08x", cardList.size()));
                for (int i = 0; i < cardList.size(); i++) {
                    CardParam cardParam = cardList.get(i);
                    cards.append(cardParam.toString());
                }
                this.elevatorCommand.setData(cards.toString());
            }
            else if(cmdEnum.equals(CMDEnum.WRITE_TIME)){
                this.elevatorCommand.setData(data.toString());
            }
            else if(cmdEnum.equals(CMDEnum.SET_READ_BIT)){
                String type=String.format("%02d", Integer.parseInt(data.toString()));
                this.elevatorCommand.setData(type);
            }
        }
    }

    public byte[] getSendCommand() {

        StringBuilder cmdBody = new StringBuilder();
        cmdBody.append(this.elevatorCommand.getSn());
        cmdBody.append(this.elevatorCommand.getPassword());
        cmdBody.append(this.elevatorCommand.getMsgId());
        cmdBody.append(this.elevatorCommand.getCmd());
        cmdBody.append(this.elevatorCommand.getDataLength());
        if (this.elevatorCommand.getData() != null) {
            cmdBody.append(this.elevatorCommand.getData());
        }
        // 生成校验码
        String checkCode = SocketStringUtil.getADD8Sum(cmdBody.toString().replaceAll("7F01", "7E").replaceAll("7F02", "7F"));
        if (checkCode.toUpperCase().equals("7F")) {
            checkCode = "7F02";
        } else if (checkCode.toUpperCase().equals("7E")) {
            checkCode = "7F01";
        }
        cmdBody.insert(0, this.elevatorCommand.getStart());
        cmdBody.append(checkCode);
        cmdBody.append(this.elevatorCommand.getEnd());
        String commandStr = cmdBody.toString().toUpperCase();
        return SocketStringUtil.hexString2Bytes(commandStr);
    }
}
