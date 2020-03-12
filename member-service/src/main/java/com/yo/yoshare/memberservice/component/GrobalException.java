package com.yo.yoshare.memberservice.component;

/**项目异常类，用于统一异常处理*/
public class GrobalException {

    public class UserExistException extends Exception{
    }


    public class VerificationErrorException extends Exception{
    }
}
