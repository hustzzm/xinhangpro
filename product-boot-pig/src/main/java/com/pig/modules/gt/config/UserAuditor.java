package com.pig.modules.gt.config;

import com.pig.basic.shiro.UserVo;
import com.pig.basic.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 自动更新创建人、修改人
 */
@Configuration
@Slf4j
public class UserAuditor implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        UserVo userVo = CommonUtil.getUserVoFormToken();

        return Optional.ofNullable(userVo.getUserCode());
    }
}
