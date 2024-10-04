package com.qingyin.cloud.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.qingyin.cloud.api.authority.dto.UserLoginReqDto;
import com.qingyin.cloud.api.authority.dto.UserRegisterReqDto;
import com.qingyin.cloud.entity.User;
import com.qingyin.cloud.enums.ErrorEnum;
import com.qingyin.cloud.exception.LoginException;
import com.qingyin.cloud.mapper.UserMapper;
import com.qingyin.cloud.service.IJwtService;
import com.qingyin.cloud.service.ILoginService;
import com.qingyin.cloud.service.IUserService;
import com.qingyin.cloud.util.TimeUtils;
import com.qingyin.cloud.util.ToolUtils;
import com.qingyin.cloud.util.YmlUtils;
import com.qingyin.cloud.vo.QingyinMessage;
import com.qingyin.cloud.api.authority.dto.UserSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    private IUserService userService;

    @Resource
    private IJwtService jwtService;
    
    @Autowired
    private StreamBridge streamBridgeTemplate;

    @Override
    public String login(UserLoginReqDto userLoginReqDto) throws Exception {
        UserSearchDto userSearchDto = new UserSearchDto();
        userSearchDto.setUsername(userLoginReqDto.getUsername());
        User user = userService.getUserDetail(userSearchDto);
        if(Objects.isNull(user)){
            throw new LoginException(ErrorEnum.LOGIN_ACCOUNT_ERROR.getCode(), ErrorEnum.LOGIN_ACCOUNT_ERROR.getMsg());
        }
        String salt = user.getSalt();
        String loginPassword = ToolUtils.makeMd5(userLoginReqDto.getPassword() + salt);
        if(!StringUtils.equals(user.getPassword(), loginPassword)){
            throw new LoginException(ErrorEnum.LOGIN_ACCOUNT_ERROR.getCode(), ErrorEnum.LOGIN_ACCOUNT_ERROR.getMsg());
        }

        String token = jwtService.generateToken(userLoginReqDto.getUsername(), userLoginReqDto.getPassword());

        return token;
    }

    @Override
    public String register(UserRegisterReqDto userRegisterReqDto) throws Exception {
        UserSearchDto userSearchDto = new UserSearchDto();
        userSearchDto.setUsername(userRegisterReqDto.getUsername());
        User user = userService.getUserDetail(userSearchDto);
        if(!Objects.isNull(user)){
            throw new LoginException(ErrorEnum.LOGIN_UNIQUE_ERROR.getCode(), ErrorEnum.LOGIN_UNIQUE_ERROR.getMsg());
        }
        User saveUser = new User();
        saveUser.setUsername(userRegisterReqDto.getUsername());
        String salt = ToolUtils.randomString(6);
        String password = ToolUtils.makeMd5(userRegisterReqDto.getPassword() + salt);
        saveUser.setSalt(salt);
        saveUser.setPassword(password);
        saveUser.setCreateTime(TimeUtils.timestamp());
        saveUser.setUpdateTime(TimeUtils.timestamp());

        UserMapper userMapper = SpringUtil.getBean(UserMapper.class);
        userMapper.insert(saveUser);
        
        // 发送 mq 消息
        boolean ack = streamBridgeTemplate.send("registerSupplier-out-0", QingyinMessage.buildData(saveUser.getUsername()));
        if(ack) {
        	log.info("register send msg success!");
        }else {
			log.error("register send msg failed!");
		}
        
        
        String token = jwtService.generateToken(userRegisterReqDto.getUsername(), userRegisterReqDto.getPassword());

        return token;
    }
}
