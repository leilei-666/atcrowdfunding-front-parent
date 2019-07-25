package com.leilei.alipaytest.config;

public class AlipayConfig {
	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016101000655105";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCh8vkwYOfcIz1ipwZFCbE2qfCiTIbMZdzSUEiC3nJBtI0xLKunOqTIg09G1U4HGFFcOHo9WBk+caWwGLbRbo8YuP03ElHscZ49tRe0Xe+7bDWR31IiwcUYuJlAoajruAyFrRCd9gnk2BRySQlnI/TsAbB3Y73HDxGNH9BK6OVwWW2HyYMeqEOzujn//fZYvxhP40hX/+C+aM4Is+DUt49p4fUPn6Nn8k35azvmAt1Uvp5m8AJ0FbQHwOKZgJmBXlRVq1DsfNa2YZtaOd9WnM1asugn1vW24FtZP+fjdLDDpYqcC2fIAPUyQFH8dYqC2XZTXpo53HrT712JgkmLhV+LAgMBAAECggEARDoy1TjeNZX7FIa9U3SD3j22DQokeBQp9ciw48GHbTr9iMwC/Q7twTPgwDy5BLAwBqQci5+D8xS58vhWfOERFB9LPlpPfUdYnTKf6/LvPoyV/QCSf+YWfRW4Ksxwd+4wCFki+QlgnbKS2pr+hR9W3d7QU1wH5cbpZAUVIZoAn1XAaKexpOs+eW5tyZkJwOk3KNhgoEBbq/Jk79VYA3z9ouETWraKL4C+rRBjIoacMeM5yqC7SQc5fEVicYZURrgwb1RzwP7sc9k+x6Yq8gtT7Z5tXum/Mj4Qc62JI1UGIgjte0J4Ut4rluaHiG1xqOoLQqyUsAvOCzz5caWZrle1wQKBgQDppRa7wPDiTq6QoRFzWDlvv3IJqGax2UeLMMQZ7qHNVlo0LS/1D6V4JXcoB94Z5uRyrmx7Go3X07iSvKRrOTQPK5XKUyvIUNa/m6MVszm+wCAO1O5EqmkCMVILfKeThwhyXplgO/y3eVo6lWy0kZ0Er1HhJvFHea9l2kCTs1Gy8QKBgQCxccOwKW67DHiT6kZAjzDRqD0V4h8lUtEuDbYuQV4pepddhxbnDVYuBmbc+BILukJHoX7uO+4Dq2kkYxyMg0dynsX2QemV9vehtkXDbIy+haJXsFeCcp0KstxnScKDEhUY8WWb3jeyTGpOYGe+PWFrRgh2t7b61tssrXd+gn9COwKBgEhkgIC4LWrRWkExxshoY5N+TAU2Cmu8mPBFsd0fFT8cLGLSRXO231w0r5DVrzlzUwE9jhsuhRiEX0IH9SHL3Zd9t7Gyx3QPqBpCTOuiVpgFy2kNDK515odpZLRucTXCtxlIulkY/RqUHSLdvYrNO0uCTupmOIge6fuy/iNFgmDhAoGAD/PuZ/QRpHdmj8XMMxmshezeMQ13R1++tqk868Ost4Xx/dKbWIJ2cQTaUptq4+sG8nGue7SfnWzXsxHGC0vyLHfhzFX4ribZ9IvykbM8pBRXmBKei3fKbp/N+OtEtYv2oZtySyUbVMhR9mHsccoK7B4LvMCfjsWqRsYBrDoHPukCgYA6djgaCRxXu6b6xjF94qTZj+KVsaCewskqZTVETt9mR9Nxw9pJKvf0pxepfeqN2LcIXm4HK/qTXKy51ovmLDGiGXBotNFl6slkyjHBDCoWjQfG2/5TwWbwS5B37LmO/OP5bRhsN0Sd+p0buMwHiiIwVQ1nhSdEsHLCAcZAv4Q8kw==";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoPDpXkiqhx3ryPHD8/DZXEce4/68Z3hY+sA1EYBj3WYatV0/vLxwF3QCrCZcTgdfQbYze61cqwWMYE80ql17HZweu7DtHo0sqO+vtrjiqfUa9EThHXti3Z5c5ZGJwCsZIPGhs71WP1SoG70LXD53YrKbB3EQOjsFSr+R++GCPUre4roI+z/6cGWn8mh1B6qVS7d6R8wAczAvwa+LVd/E+bK2mzMHHEbi8szr5+4t4J35H59kyEpXCTV9mBlD4tC52f/uJhmSJI3i0jXPpJ6aFU/Bc2dHFf19uocormehgSmo6Kl4+XTIuBWn6H7pAzuasJTPrpzVnW2DA+nTwYHsaQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	//支付宝会悄悄地给我们发送一个请求，告诉我们支付成功的信息
	public static String notify_url = "http://kffp38vj4k.52http.net/payAsync";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	//同步通知，支付成功，一般跳转到成功页
	public static String return_url = "http://kffp38vj4k.52http.net/orderList";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";
}

