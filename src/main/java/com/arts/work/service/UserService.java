package com.arts.work.service;

import com.arts.work.common.pojo.dto.LoginDTO;
import com.arts.work.common.utils.RestResponse;

public interface UserService {

    RestResponse login(LoginDTO loginDTO);

    RestResponse sendAuthCodeEmail(String emailAddress);

    RestResponse generateInvitationCode(String userId);
}
