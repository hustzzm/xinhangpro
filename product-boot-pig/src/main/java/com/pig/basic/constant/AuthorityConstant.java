package com.pig.basic.constant;

/**
 * @author linqi
 *
 */

public enum AuthorityConstant {
    NOT_AUTHORITY("notAuthority"),
    CREATE_USER_ID("createUserId"),
    CREATE_USER_ID_COLUMN("create_user_id"),
    AUTH("forbid"),
    FORBIDDEN("无访问权限"),
    GETID("getId");
    private String value;

    public String getValue() {
        return value;
    }

    public AuthorityConstant setValue(String value) {
        this.value = value;
        return this;
    }

    AuthorityConstant(String value) {
        this.value = value;
    }
}
