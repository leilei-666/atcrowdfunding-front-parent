package com.leilei.front.exception;

public class UserLoginacctException extends RuntimeException{
   public UserLoginacctException (){
       super("用户账号/手机号已经存在");
   }
}
