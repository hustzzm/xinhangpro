package com.pig.modules.gt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 房间管理(BizRoomManage)实体类
 *
 * @author makejava
 * @since 2022-07-06 22:26:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomType extends AbstractBaseTimeEntity implements Serializable {
    private static final long serialVersionUID = -43096794550669929L;

    /**
     * 房间名称
     */
    private String name;
    /**
     * 房间类型
     */
    private String roomType;
}

