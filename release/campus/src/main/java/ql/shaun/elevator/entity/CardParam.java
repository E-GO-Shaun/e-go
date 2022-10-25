package ql.shaun.elevator.entity;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import ql.shaun.elevator.utils.PortAddress;
import ql.shaun.elevator.utils.SocketStringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Serialization
public class CardParam implements Serializable {
    // 卡号
    private String cardNum;

    // 密码
    private String password;

    // 有效日期
    private String validDate;

    // 开门时段 --按照端口编号传递
    private List<Integer> timeList;

    // 取值范围0-65534，0表示次数用光了。65535表示不受限制
    private Integer validTimes;
    // 权限(楼层)  楼层数字，多个用,隔开
    private String floorNum;

    // 特权
    // 0普通卡
    // 1	首卡
    // 2	常开
    // 3	巡更
    // 4	防盗设置卡
    // 5	管理卡
    private String authority;

    // 状态,0：正常状态；1：挂失；2：黑名单
    private String status;

    // 节假日--暂不启用
    private List<String> holidays;

    @Override
    public String toString() {
        StringBuilder dataCommand = new StringBuilder();
        Integer card = Integer.parseInt(this.cardNum);
        String cardHex=SocketStringUtil.number2HexByEveryTwoBit(card);
        dataCommand.append(cardHex);
        dataCommand.append("FFFFFFFF");
        dataCommand.append(SocketStringUtil.strDate2BCDStr(this.validDate));
        dataCommand.append(this.get01fromNumber());
        String times = SocketStringUtil.intToHex(this.validTimes);
        dataCommand.append(times);
        String[] ports = this.floorNum.toString().split(",");
        PortAddress portAddress = new PortAddress(ports);
        dataCommand.append(portAddress.toString());

        String authNumber = SocketStringUtil.intToHexStr(this.authority);
        if (authNumber.length() == 1) {
            authNumber = "0" + authNumber;
        }
        dataCommand.append(authNumber);
        dataCommand.append("0" + this.status);
        // 借节日
        dataCommand.append("00000000");
        // 读卡时间间隔
        dataCommand.append("000000000000");
        return dataCommand.toString();
    }

    private String get01fromNumber() {
        String[] numberArray = new String[64];
        if (this.timeList != null && this.timeList.stream().count() > 0) {
            for (int i = 0; i < this.timeList.stream().count(); i++) {
                int num = this.timeList.get(i);
                numberArray[num] = "01";
            }
        }
        // 处理空值
        for (int i = 0; i < numberArray.length; i++) {
            if (numberArray[i] == null || !numberArray[i].equals("01")) {
                numberArray[i] = "00";
            }
        }
        return StringUtils.join(numberArray);
    }

    public static CardParam getBean(String cardParamStr) {
        String cardNum=cardParamStr.substring(0,10);
        String password=cardParamStr.substring(10,18);
        String validDate=cardParamStr.substring(18,28);
        String openTimes=cardParamStr.substring(28,156);
        String validTimes=cardParamStr.substring(156,160);
        String auth=cardParamStr.substring(160,178);
        String special=cardParamStr.substring(178,180);
        String status=cardParamStr.substring(180,182);
        String holidays=cardParamStr.substring(182,190);
        String readTime=cardParamStr.substring(190,202);
        CardParam cardParam = new CardParam();
        cardParam.setCardNum(SocketStringUtil.hex2Int10(cardNum).toString());
        cardParam.setPassword(password);
        cardParam.setValidDate(SocketStringUtil.BCDDate2Str(validDate+"00"));
//        cardParam.setTimeList();
        cardParam.setValidTimes(SocketStringUtil.hex2Int10(validTimes));
        PortAddress portAddress=new PortAddress(null);
        List<String> floorList=portAddress.getFloorByCommand(auth);
        cardParam.setFloorNum(StringUtils.join(floorList,","));
        cardParam.setAuthority(special);
        cardParam.setStatus(SocketStringUtil.hex2Int10(status).toString());
//        cardParam.setHolidays(holidays);
//        cardParam.
        return cardParam;
    }

    public static List<CardParam> getList(String cardParamAll) {
        String cardNum = cardParamAll.substring(0, 8);
        Integer nums = SocketStringUtil.hex2Int10(cardNum);
        List<CardParam> cardParams = new ArrayList<>();
        for (int i = 0; i < nums; i++) {
            CardParam cardParam = getBean(cardParamAll.substring(i * 202 + 8, i * 202 + 202 + 8));
            cardParams.add(cardParam);
        }
        return cardParams;
    }
}
