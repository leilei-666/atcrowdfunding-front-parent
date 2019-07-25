package com.leilei.atcrowdfunding.user.component;

import com.leilei.atcrowdfunding.user.utils.HttpUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/*
发送短信的模板
 */
@ConfigurationProperties(prefix = "aliyun.sms")
@Component
@Data
@Slf4j
public class SmsTemplate {
    //固定的属性在application.properties中配置
    private String host;
    private String path;
    private String method;
    private String appcode;
    private Map<String, String> headers = new HashMap<String, String>();

    //发送短信
    public boolean sendSms(String mobile, String code) {
        log.debug("手机号：{}：发送了一个【{}】验证码", mobile, code);
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", "TP1711063");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                //短信发送成功
                return true;
            }
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            log.error("短信发送出现问题：手机号{}，验证码{}，异常原因{}",mobile,code,e);
        }
        return false;
    }
}
