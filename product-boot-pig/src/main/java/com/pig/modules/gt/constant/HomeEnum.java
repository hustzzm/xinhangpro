package com.pig.modules.gt.constant;

import lombok.Data;
import lombok.Getter;

@Data
public class HomeEnum {

    /**
     * 验证状态枚举
     */
    @Getter
    public enum CommonEnum {

        TO_BE_PAID("5", "待支付"),
        PAYMENT("10", "已支付"),
        PAYMENT_FAILED("20", "支付失败"),
        ORDER_NO("O", "O"), // order表的no
        DEVICE_INFO("WEB", "WEB"), // 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
        FEE_TYPE("CNY", "CNY"), // 符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型
        TRADE_TYPE("trade_type", "JSAPI"); // 交易类型


        public static String getValue(String key) {
            CommonEnum[] carTypeEnums = values();
            for (CommonEnum carTypeEnum : carTypeEnums) {
                if (carTypeEnum.getKey().equals(key)) {
                    return carTypeEnum.getValue();
                }
            }
            return "支付失败";
        }

        CommonEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private String key;

        private String value;
    }

    /**
     * 验证状态枚举
     */
    public enum PostTypeEnum {

        LIKES(1, "likes"), //点赞
        UNLIKE(2, "unlike"),//取消点赞
        SHARES(3, "shares"),//转贴
        UNSHARE(4, "unshare");//取消转贴

        PostTypeEnum(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        private Integer key;

        private String value;

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 帖子种类
     */
    public enum NewsTypeEnum {

        HOME(1, "home"), //Home主页
        LIVE(2, "live");//直播


        NewsTypeEnum(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        private Integer key;

        private String value;

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
