package ql.shaun.elevator;

import com.alibaba.fastjson.JSONObject;
import ql.shaun.elevator.entity.CardParam;
import ql.shaun.elevator.entity.ReceivedMessage;
import ql.shaun.elevator.enums.CMDEnum;
import ql.shaun.elevator.utils.SocketStringUtil;
import ql.shaun.socket.TCPClient;
import ql.shaun.elevator.entity.ElevatorParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ElevatorProcess {
    private ElevatorParameters elevatorParameters;
    private TCPClient tcpClient;

    public ElevatorProcess(ElevatorParameters elevatorParameters) {
        this.elevatorParameters = elevatorParameters;

        this.tcpClient = new TCPClient(this.elevatorParameters.getIp(), this.elevatorParameters.getPort()) {
            @Override
            protected void onDataReceive(byte[] bytes, int size) {
                if (size > 0) {
                    String reply = SocketStringUtil.bytes2HexString(bytes);
                    ReceivingCmdProcess receivingCmdProcess = new ReceivingCmdProcess(reply);
                    ReceivedMessage receivedMessage = receivingCmdProcess.setReceivedMessage();
                    onReceived(receivedMessage);
                }
            }
        };
        this.tcpClient.connect();
    }

    public void writeCards(String serialNumber, String msgId, List<CardParam> cardList) {
        // 写卡准备
        Map<String, Object> data = new HashMap<>();
        data.put("msgId", "12345678");
        sendMessage(CMDEnum.BEGIN_WRITE_AUTH_CARD, data);
        // 开始写卡
        String json = JSONObject.toJSONString(cardList);
        data.put("serialNumber", serialNumber);
        data.put("data", json);
        data.put("msgId", msgId);
        sendMessage(CMDEnum.WRITE_AUTH_CARD, data);
        // 终止写卡
        data.put("msgId", "12345678");
        sendMessage(CMDEnum.END_WRITE_AUTH_CARD, data);
    }

    protected abstract void onReceived(ReceivedMessage receivedMessage);

    public void sendMessage(CMDEnum cmdEnum, Map<String, Object> data) {
        data.put("sn", this.elevatorParameters.getSnNum());
        data.put("password", this.elevatorParameters.getPassword());
        SendingCmdProcess sendingCmdProcess = new SendingCmdProcess(cmdEnum, data);
        byte[] cmd = sendingCmdProcess.getSendCommand();
        this.tcpClient.send(cmd);

    }

    public void Close() {
        this.tcpClient.close();
    }
}
