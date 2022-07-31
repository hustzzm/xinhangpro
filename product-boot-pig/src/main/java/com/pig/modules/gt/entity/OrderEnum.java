package com.pig.modules.gt.entity;

public enum OrderEnum {
    PENDING_PAYMENT("5", "待付款"),

    PAYMENT_RECEIVED("10", "已支付"),

    PAYMENT_FAILED("20", "支付失败");

    /**
     * 值
     */
    private final String code;

    /**
     * 名称
     */
    private final String name;

    public static OrderEnum getByCode(String code) {
        if (null == code) {
            return null;
        }
        for (OrderEnum status : OrderEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public static OrderEnum getByName(String name) {
        for (OrderEnum status : OrderEnum.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        return null;
    }

    OrderEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
