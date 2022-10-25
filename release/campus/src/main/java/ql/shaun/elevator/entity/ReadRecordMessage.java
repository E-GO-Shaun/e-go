package ql.shaun.elevator.entity;

import lombok.Data;
import ql.shaun.elevator.utils.SocketStringUtil;

@Data
public class ReadRecordMessage {
    private String cardNum;
    private String recordTime;
    private String readNum;
    private String recordStatus;

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
        Integer status = SocketStringUtil.hex2Int(recordStatus);
        switch (status) {
            case 1:
                this.recordMessage = "合法开门";
                break;
            case 2:
                this.recordMessage = "密码开门--卡号为密码";
                break;
            case 3:
                this.recordMessage = "卡加密码";
                break;
            case 4:
                this.recordMessage = "手动输入卡加密码";
                break;
            case 5:
                this.recordMessage = "首卡开门";
                break;
            case 6:
                this.recordMessage = "门常开--常开工作方式中，刷卡进入常开状态";
                break;
            case 7:
                this.recordMessage = "多卡开门--门未开，等待继续读卡";
                break;
            case 8:
                this.recordMessage = "重复读卡";
                break;
            case 9:
                this.recordMessage = "有效期过期";
                break;
            case 10:
                this.recordMessage = "开门时段过期";
                break;
            case 11:
                this.recordMessage = "节假日无效";
                break;
            case 12:
                this.recordMessage = "非法卡";
                break;
            case 13:
                this.recordMessage = "巡更卡--不开门";
                break;
            case 14:
                this.recordMessage = "探测锁定";
                break;
            case 15:
                this.recordMessage = "无有效次数";
                break;
            case 16:
                this.recordMessage = "防潜回";
                break;
            case 17:
                this.recordMessage = "密码错误--卡号为错误密码";
                break;
            case 18:
                this.recordMessage = "密码加卡模式密码错误--卡号为卡号";
                break;
            case 19:
                this.recordMessage = "锁定时(读卡)或(读卡加密码)开门";
                break;
            case 20:
                this.recordMessage = "锁定时(密码开门)";
                break;
            case 21:
                this.recordMessage = "首卡未开门";
                break;
            case 22:
                this.recordMessage = "挂失卡";
                break;
            case 23:
                this.recordMessage = "黑名单卡";
                break;
            case 24:
                this.recordMessage = "门内上限已满，禁止入门";
                break;
            case 25:
                this.recordMessage = "开启防盗主机(设置卡)";
                break;
            case 26:
                this.recordMessage = "关闭防盗主机(设置卡)";
                break;
            case 27:
                this.recordMessage = "开启防盗主机(密码)";
                break;
            case 28:
                this.recordMessage = "关闭防盗主机(密码)";
                break;
            case 29:
                this.recordMessage = "互锁时(读卡)或(读卡加密码)开门";
                break;
            case 30:
                this.recordMessage = "互锁时(密码开门)";
                break;
            case 31:
                this.recordMessage = "全卡开门";
                break;
            case 32:
                this.recordMessage = "多卡开门--等待下张卡";
                break;
            case 33:
                this.recordMessage = "多卡开门--组合错误";
                break;
            case 34:
                this.recordMessage = "非首卡时段刷卡无效";
                break;
            case 35:
                this.recordMessage = "非首卡时段密码无效";
                break;
            case 36:
                this.recordMessage = "禁止刷卡开门";
                break;
            case 37:
                this.recordMessage = "禁止密码开门";
                break;
            case 38:
                this.recordMessage = "门内已刷卡，等待门外刷卡。（门内外刷卡验证）";
                break;
            case 39:
                this.recordMessage = "门外已刷卡，等待门内刷卡。（门内外刷卡验证）";
                break;
            case 40:
                this.recordMessage = "请刷管理卡(在开启管理卡功能后提示)";
                break;
            case 41:
                this.recordMessage = "请刷普通卡(在开启管理卡功能后提示)";
                break;
            default:
                this.recordMessage = "";
        }
    }

    private String recordMessage;
}
