package com.qingyin.cloud.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingyin.cloud.constant.AuthorityConstant;
import com.qingyin.cloud.constant.CommonConstant;
import com.qingyin.cloud.entity.User;
import com.qingyin.cloud.enums.ErrorEnum;
import com.qingyin.cloud.exception.LoginException;
import com.qingyin.cloud.mapper.UserMapper;
import com.qingyin.cloud.service.IJwtService;
import com.qingyin.cloud.util.ToolUtils;
import com.qingyin.cloud.vo.User.LoginUserInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class JwtServiceImpl implements IJwtService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String generateToken(String username, String password) throws Exception {
        return generateToken(username, password, 0);
    }

    @Override
    public String generateToken(String username, String password, int expire) throws Exception {

        // 查询用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
                        .last("limit 1")
        );
        String vPsw = password + user.getSalt();
        if(!StringUtils.equals(user.getPassword(), ToolUtils.makeMd5(vPsw))){
            // 密码错误
            log.error("can not find user: [{}], [{}]", username, password);
            throw new LoginException(ErrorEnum.LOGIN_ACCOUNT_ERROR.getCode(), ErrorEnum.LOGIN_ACCOUNT_ERROR.getMsg());
        }

        // 创建 token
        if (expire <= 0) {
            expire = AuthorityConstant.DEFAULT_EXPIRE_DAY;
        }

        // 构建存储信息
        LoginUserInfo loginUserInfo = new LoginUserInfo(
                user.getId(), user.getUsername()
        );

        // 计算超时时间
        ZonedDateTime zdt = LocalDate.now().plus(expire, ChronoUnit.DAYS)
                .atStartOfDay(ZoneId.systemDefault());
        Date expireDate = Date.from(zdt.toInstant());

        return Jwts
                .builder()
                // jwt payload
                .claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo))
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt 过期时间
                .setExpiration(expireDate)
                // jwt 签名 --> 加密
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * <h2>根据本地存储的私钥获取到 PrivateKey 对象</h2>
     * */
    private PrivateKey getPrivateKey() throws Exception{
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(AuthorityConstant.PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }
}
