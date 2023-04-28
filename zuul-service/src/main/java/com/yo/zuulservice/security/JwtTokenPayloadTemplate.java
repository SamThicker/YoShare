package com.yo.zuulservice.security;

import java.util.Date;

public class JwtTokenPayloadTemplate {
    /*jwt建议格式
    iss: jwt签发者
    sub: jwt所面向的用户
    aud: 接收jwt的一方
    exp: jwt的过期时间，这个过期时间必须要大于签发时间
    nbf: 定义在什么时间之前，该jwt都是不可用的.
    iat: jwt的签发时间
    jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。*/

    public Object getSub() {
        return sub;
    }

    public String getSubtype() {
        return subtype;
    }

    public Date getCreated() {
        return created;
    }

    public Date getExp() {
        return exp;
    }

    /**主体*/
    private Object sub;
    /**主体类型，id、username等*/
    private String subtype;
    /**生成时间*/
    private Date created;
    /**过期时间*/
    private Date exp;


    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + 604800 * 1000);
    }

    public JwtTokenPayloadTemplate(Object sub){
        Long currentTimeStamp = System.currentTimeMillis();
        this.sub = sub;
        this.subtype = "id";
        this.created = new Date(currentTimeStamp);
        this.exp = new Date(currentTimeStamp + 604800 * 1000);
    }


}
