package com.leilei.atcrowdfunding.project.component;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssTemplate {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String upload(byte[] content, String fileName) throws IOException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        fileName=UUID.randomUUID().toString().replace("-","")+"_"+fileName;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        //拼接指定的路径
        //第一个参数桶的名字，第二个参数文件的名字
        ossClient.putObject(bucketName, "pic/"+date+"/"+fileName, new ByteArrayInputStream(content));

// 关闭OSSClient。
        ossClient.shutdown();
        //返回访问地址，桶名+endpoint+文件的名字
        String url = "https://"+bucketName+"."+endpoint.substring(7)+"/pic/"+date+"/"+fileName;
        return url;
    }
}
