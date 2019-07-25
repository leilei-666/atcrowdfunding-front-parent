package com.leilei.atcrowdfunding.user;

import com.leilei.atcrowdfunding.user.component.SmsTemplate;
import com.leilei.atcrowdfunding.user.mapper.TMemberMapper;
import com.leilei.front.bean.TMember;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AtcrowdfundingUserApplicationTests {
    @Autowired
    SmsTemplate smsTemplate;
    @Autowired
    TMemberMapper memberMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    public void testStringRedisTemplate() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //没有添加，有的话在value后追加
        ops.append("msg","hello2");
        //通过key获取到value
        String msg = ops.get("msg");
        System.out.println(msg);
    }
    @Test
    public void testTemberMapper() {
        long l = memberMapper.countByExample(null);
        System.out.println("总记录的数据为" + l);
    }

    @Test
    public void testSendSms() {
        boolean b = smsTemplate.sendSms("15668372273", "0521");
        System.out.println("短信发送" + b);
    }

    @Test
    public void contextLoads() throws IOException {
        //1.httpClient创建一个可以发送请求的客户端
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //2.创建一个我们将要发送的请求
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        //3.执行请求，收取响应内容
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        try {
            //4.获取响应内容
            //4.1获取响应的状态行
            System.out.println(response1.getStatusLine());
            //4.2获取响应出来的内容--真是正的内容
            HttpEntity entity1 = response1.getEntity();
            String s = EntityUtils.toString(entity1);
            System.out.println("收到的响应:" + s);
        } finally {
            //5.关闭连接
            response1.close();
        }

/*        HttpPost httpPost = new HttpPost("http://targethost/login");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response2 = httpclient.execute(httpPost);

        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity2);
        } finally {
            response2.close();
        }*/
    }

    @Test
    public void post() throws IOException {
        //1.httpClient创建一个可以发送请求的客户端
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //2.创建一个post请求
        HttpPost httpPost = new HttpPost("http://dingxin.market.alicloudapi.com/dx/sendSms");
        //2.1post请求传递参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("mobile", "17865152306"));
        nvps.add(new BasicNameValuePair("param", "code:6666"));
        nvps.add(new BasicNameValuePair("tpl_id", "TP1711063"));
        //2.2设置请求参数
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8")));
        //2.3设置请求头
        httpPost.setHeader("Authorization", "APPCODE 8a8ed607ec2045f69ede362a0cff69c5");
        //3.发送请求
        CloseableHttpResponse response = httpclient.execute(httpPost);
        //4.获取响应信息
        HttpEntity content = response.getEntity();
        StatusLine statusLine = response.getStatusLine();
        System.out.println("响应状态行：" + statusLine);
        System.out.println("响应内容" + EntityUtils.toString(content));
        response.close();
    }

}
