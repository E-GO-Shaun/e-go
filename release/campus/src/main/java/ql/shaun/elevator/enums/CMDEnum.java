package ql.shaun.elevator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CMDEnum {
    /**
     * 默认值
     */
    DEFAULT("000000","00000000"),
    /**
     * 读取SN
     */
    READ_SN("410200","00000000"),

    /**
     * 设置通讯密码
     */
    SET_PASSWORD("410300","00000004"),

    /**
     * 读取TCP参数
     */
    READ_TCP_PARAM("410600","00000000"),

    /**
     * 设置TCP参数
     */
    SET_TCP_PARAM("410601","00000089"),

    /**
     * 获取设备版本
     */
    GET_VERSION("410800","00000000"),

    /**
     * 读取设备运行时信息
     */
    DEVICE_RUNTIME_INFO("410900","00000000"),
    /**
     * 开启实时监控
     */
    START_MONITOR("410B00","00000000"),
    /**
     * 关闭实时监控
     */
    STOP_MONITOR("410B01","00000000"),
    /**
     * 实时监控状态
     */
    MONITOR_STATUS("410B02","00000000"),
    /**
     * 读取时间
     */
    READ_TIME("420100","00000000"),
    /**
     * 写入时间
     */
    WRITE_TIME("420200","00000007"),
    /**
     * 开门
     */
    OPEN_DOOR("430200","00000009"),

    /**
     * 关门
     */
    CLOSEC_DOOR("430201","00000009"),

    /**
     * 设置门常开
     */
    SET_ALWAYS_OPEN("430202","00000009"),

    /**
     * 锁定
     */
    LOCK("430300","00000008"),

    /**
     * 解锁
     */
    UNLOCK("430301","00000008"),

    /**
     * 读取授权卡信息
     */
    READ_AUTH_CARD_LIST("470100","00000000"),

    /**
     * 清空所有授权卡
     */
    EMPTY_ALL_CARD("470200","00000001"),

    /**
     * 读取所有授权卡
     */
    READ_ALL_CARD("470300","00000001"),

    /**
     * 读取单个授权卡
     */
    READ_UNIQUE_CARD("470301","00000005"),

    /**
     * 添加授权卡-非排序
     */
    // Todo 变长数据处理，以及授权卡信息如何传递问题
    ADD_AUTH_CARD("470400","00000065"),

    /**
     * 删除授权卡
     */
    DELETE_AUTH_CARD("470500","00000069"),

    /**
     * 开始写入排序卡-排序
     */
    BEGIN_WRITE_AUTH_CARD("470700","00000000"),

    /**
     * 写入排序卡
     */
    WRITE_AUTH_CARD("470701",""),

    /**
     * 终止写入排序卡
     */
    END_WRITE_AUTH_CARD("470702","00000000"),

    /**
     * 设置读卡器位数
     */
    SET_READ_BIT("410A09","00000001"),
    ;


    final String cmd;
    final String len;

}
