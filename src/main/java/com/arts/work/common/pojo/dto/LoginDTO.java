package com.arts.work.common.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDTO {

    private String email;

    private String code;

    private String inviteCode;
}
