package ql.shaun.elevator.entity;

import lombok.Data;

@Data
public class ReceivedMessage {
    private Boolean result = false;
    private String msgId;
    // 1标准应答成功，2密码错误，3校验码错误......
    private int rType;
    private String message;
    private String codeOri;
    private ReadRecordMessage readRecordMessage;
}
