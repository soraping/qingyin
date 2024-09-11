package com.qingyin.cloud.service;

import com.alibaba.fastjson2.JSON;
import com.qingyin.cloud.util.TokenParseUtils;
import com.qingyin.cloud.vo.User.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TokenTest {

    @Test
    public void TokenParseTest() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJxaW5neWluX3VzZXJfdG9rZW5fa2V5Ijoie1wiaWRcIjoyLFwidXNlcm5hbWVcIjpcInZpY" +
                "2ljaVwifSIsImp0aSI6IjdlYmZkNmY0LTM2ZTAtNDNhYi1iYjI5LTNlYjkyN2RhZTk3YSIsImV4cCI6MTcyNTg5NzYwMH0.CfYh2l" +
                "zoQFhVdkIf-awNa1NN2NE3ZmNZlTX7bqsr76WccR6QWcgKQyNf1726sTKQTDZbh869dXx9AwivtgjkCiwNWTbRNB-guXaDXB_u3i6C" +
                "r9tmYOvJBBm5fk3B_FammdYuVCF7u_86uB2aLX0KpSNQ1nUIjeVugKh_cq7ajz84VFXR4Vudaku14qlbt3M8nU9eDJOjv8TWSy2oFN" +
                "WfTMZ2hA2PsXJuLJyeBmClElrSXk8zHagy4fCgbwKvow6GoIBeXxuvSrGRThRr2968rai-SQyVjP5HTx0LvqedLVKbXXmh4JUEYRxd" +
                "1Gucz2GauYe3NYn_nQ9BtAgLR1xrxw";

        LoginUserInfo loginUserInfo = TokenParseUtils.parseUserInfoFromToken(token);

        log.info("token parse login info = {}", JSON.toJSONString(loginUserInfo));
    }

}
