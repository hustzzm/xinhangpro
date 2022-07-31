package com.pig.modules.gt.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyResutlVo {

    private String id;
    private String create_time;
    private String resource_type;
    private String event_type;
    private String summary;
    private ResouceVo resource;
}
