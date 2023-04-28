package com.yo.groupservice.exception;

/**项目异常类，用于统一异常处理*/
public class GlobalException {

    public static class GroupTooMuchException extends Exception{
        public GroupTooMuchException(String s){
            super(s);
        }
    }


}
