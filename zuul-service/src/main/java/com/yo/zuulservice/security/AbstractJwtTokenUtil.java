package com.yo.zuulservice.security;

import com.alibaba.fastjson.JSON;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import java.io.IOException;

public abstract class AbstractJwtTokenUtil {

    /**用于签名*/
    private static RsaSigner signer;



    public static String generateToken(String payload) throws IOException {
        String token = JwtHelper.encode(payload, signer).getEncoded();
        return token;
    }

    public String generateToken(Object payload) throws IOException {
        String jsonPayload = JSON.toJSONString(payload);
        String token = JwtHelper.encode(jsonPayload, signer).getEncoded();
        return token;
    }


    public static void setSigner(RsaSigner signerToSet) {
        signer = signerToSet;
    }

}
