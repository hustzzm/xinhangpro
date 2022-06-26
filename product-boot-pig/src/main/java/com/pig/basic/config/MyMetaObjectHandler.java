package com.pig.basic.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pig.basic.shiro.UserVo;
import com.pig.basic.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        UserVo userVo = CommonUtil.getUserVoFormToken();
        final Date date = new Date();
        this.strictInsertFill(metaObject, "createTime", Date.class, date);
        this.strictInsertFill(metaObject, "createUserId", String.class, userVo.getUserCode());
        this.strictInsertFill(metaObject, "createUserName", String.class, userVo.getUserName());

        this.strictInsertFill(metaObject, "updateTime", Date.class, date);
        this.strictInsertFill(metaObject, "updateUserId", String.class, userVo.getUserCode());
        this.strictInsertFill(metaObject, "updateUserName", String.class, userVo.getUserName());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        UserVo userVo = CommonUtil.getUserVoFormToken();
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateUserId", String.class, userVo.getUserCode());
        this.strictInsertFill(metaObject, "updateUserName", String.class, userVo.getUserName());
    }
}
