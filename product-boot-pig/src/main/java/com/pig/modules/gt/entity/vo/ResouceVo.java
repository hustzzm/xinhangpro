package com.pig.modules.gt.entity.vo;


import lombok.Data;

@Data
public class ResouceVo {
    private String original_type;
    private String algorithm;
    private String ciphertext;
    private String associated_data;
    private String nonce;
}
