package com.leilei.alipaytest.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.leilei.alipaytest.config.AlipayConfig;
import com.leilei.alipaytest.vo.PayAsyncVo;
import com.leilei.alipaytest.vo.PayResultVo;
import com.leilei.alipaytest.vo.PayVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class PayController {
    @ResponseBody
    @PostMapping("/pay")
    public String pay(PayVo vo) throws AlipayApiException {
        //1.根据支付宝的配置，生成一个客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);

        //2.创建一个支付请求，并设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //3.alipayClient将此次支付请求发送给支付宝，支付宝会给我们响应
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        //会受到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台

        //输出
        System.out.println("支付宝的响应" + result);
        return result;
    }

    @GetMapping("/orderList")
    public String toOrderPage(PayResultVo payResultVo, Model model) {
        model.addAttribute("result", payResultVo);
        return "order";
    }

    /*
    支付宝会给我们这个发送一个请求，感知支付状态
    异步通知：
        支付完成，会隔一段时间给商家发送一次异步通知（防止商家服务器炸了，不知道订单是否成功）
    */
    @ResponseBody
    @RequestMapping("/payAsync")
    public String payStatus(PayAsyncVo vo, HttpServletRequest request) throws UnsupportedEncodingException {
        //获取所有要验签的参数
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        try {
            //验签；
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key,
                    AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

            if (signVerified) {
                //TRADE_SUCCESS    交易成功，支付成功
                //TRADE_FINISHED   交易完成
                if (vo.getTrade_status().equals("TRADE_SUCCESS")) {
                    //支付完成
                    System.out.println("订单" + vo.getOut_trade_no() + "支付完成");
                    System.out.println("数据库已经修改");
                } else if (vo.getTrade_status().equals("TRADE_FINISHED")) {
                    System.out.println("数据库状态修改为交易已完成");
                }
            }
        } catch (AlipayApiException e) {
            System.out.println("验签失败。。");
        }
        //只要给支付宝返回success，支付宝就不会对这笔单交易发异步通知了
        return "success";
    }
}
