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
        PAYMENT_FAILED("20", "支付失败");

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
